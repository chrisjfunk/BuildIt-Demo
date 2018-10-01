package buildit.demo.scrape;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
@Slf4j
@RequiredArgsConstructor
public class PageCrawler implements Function<URL, SiteMap> {
    private final int maxTimeToWaitInSeconds;
    private final Function<URL, Optional<Page>> pageLoader;
    private final ConcurrentHashMap<String, PageWithCount> pageGraph = new ConcurrentHashMap<String, PageWithCount>();

    @Override
    public SiteMap apply(URL url) {
        log.info("Building SiteMap for " + url + " hopefully within " + maxTimeToWaitInSeconds + " seconds.");
        pageGraph.clear();
        CountDownLatch finished = new CountDownLatch(1);
        Thread task = new Thread(() -> {
            visit(url);
            finished.countDown();
        });
        task.run();
        try {
            finished.await(maxTimeToWaitInSeconds, SECONDS);
        } catch (InterruptedException e) {
            log.info("This site is too big, stopping now to save a few trees.");
        }
        return new InternalSiteMap(url.getHost(), pageGraph.values());
    }

    private void visit(URL url) {
        final String urlNoAnchor = StringUtils.isEmpty(url.getRef()) ? url.toString() : url.toString().substring(0, url.toString().indexOf("#"));
        log.info("Visiting " + url);
        PageWithCount p = alreadyVisited(urlNoAnchor).orElseGet(() -> ensureLoaded(urlNoAnchor));
        p.getCount().incrementAndGet();
        log.info("Visited " + url + " " + p.getCount().get() + " times ");
    }

    private Optional<PageWithCount> alreadyVisited(String url) {
        return Optional.ofNullable(pageGraph.get(url));
    }

    private PageWithCount ensureLoaded(String url) {
        PageWithCount newCountedPage = new PageWithCount();
        return Optional.ofNullable(pageGraph.putIfAbsent(url, newCountedPage))
                .orElseGet(() -> loadPage(newCountedPage, url));
    }

    private PageWithCount loadPage(PageWithCount newCountedPage, String url) {
        try {
            newCountedPage.setPage(pageLoader.apply(new URL(url)));
        } catch (MalformedURLException e) {
            throw new IllegalStateException(url);
        }
        if (newCountedPage.getPage().isPresent() && newCountedPage.getPage().get().getInternalLinks() != null) {
            newCountedPage.getPage().get().getInternalLinks().parallelStream().forEach(l -> visit(l.getUrl()));
        }
        return newCountedPage;
    }

    @Data
    private static class PageWithCount {
        private final AtomicInteger count = new AtomicInteger();
        private Optional<Page> page = Optional.empty();
    }

    @Getter
    private static class InternalSiteMap implements SiteMap {
        private final String host;
        private final List<CountedPage> pages;

        public InternalSiteMap(String host, Collection<PageWithCount> fromCounts) {
            this.host = host;
            pages = fromCounts.stream()
                    .filter(x -> x.getPage().isPresent())
                    .map(this::toCountedPage).collect(Collectors.toList());
            Collections.sort(pages);
        }

        private CountedPage toCountedPage(final PageWithCount pageWithCount) {
            return new MyCountedPage(pageWithCount.count.get(), pageWithCount.page.get());
        }

        @RequiredArgsConstructor
        @ToString
        private static class MyCountedPage implements CountedPage {
            @Getter
            private final int inboundLinkCount;
            @Delegate
            private final Page page;
        }
    }
}
