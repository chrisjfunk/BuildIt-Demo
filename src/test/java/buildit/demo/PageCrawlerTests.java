package buildit.demo;

import buildit.demo.scrape.PageCrawler;
import buildit.demo.scrape.SiteMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.URL;
import java.util.function.Function;

import static buildit.demo.Util.urlFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class PageCrawlerTests {
    Function<URL, SiteMap> crawler = new PageCrawler(30, Util::load);

    @Test
    public void itShouldCrawlMultipleInternalPages() throws Exception {
        SiteMap map = crawler.apply(urlFor("home.html"));
        assertThat(map.getHost()).isEqualTo("localhost");
        assertThat(map.getPages().size()).isEqualTo(3);
        assertThat(map.missingInternalLinks().size()).isEqualTo(0);
        assertThat(map.find("zebra.html").get().getDescription()).isEqualTo("Not Found");
    }

    @Test
    public void itShouldIgnoreAnchorsWhenCrawling() throws Exception {
        SiteMap map = crawler.apply(urlFor("anchor.html"));
        assertThat(map.getPages().size()).isEqualTo(2);
        assertThat(map.find("/anchor-target.html").get().getInboundLinkCount()).isEqualTo(2);
    }


}
