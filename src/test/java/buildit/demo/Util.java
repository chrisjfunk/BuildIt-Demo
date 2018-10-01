package buildit.demo;

import buildit.demo.scrape.HtmlPage;
import buildit.demo.scrape.Link;
import buildit.demo.scrape.MissingPage;
import buildit.demo.scrape.Page;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class Util {
    //stub the file loader to load from local test resource directory
    public static Optional<Page> load(URL url) {
        try {
            return Optional.of(new HtmlPage(url, Jsoup.parse(new File("src/test/resources", url.getFile()), "UTF-8", "http://localhost")));
        } catch (FileNotFoundException e) {
            return Optional.of(new MissingPage(url));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static URL urlFor(String page) throws MalformedURLException {
        if (!page.startsWith("http")) {
            return new URL("http://localhost/" + page);
        }
        return new URL(page);
    }

    public static Optional<Link> linkFor(List<Link> links, String name) {
        return links.stream().filter(child(name)).findFirst();
    }

    public static Predicate<? super Link> child(String name) {
        if (name.startsWith("http")) {
            String finalName = name;
            return l -> l.getUrl().toString().equalsIgnoreCase(finalName);
        }
        name = Optional.ofNullable(name).orElse("/");
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        final String search = name;
        return l -> l.getUrl().getFile().equalsIgnoreCase(search);
    }
}
