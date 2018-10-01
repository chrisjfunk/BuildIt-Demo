package buildit.demo.scrape;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.net.URL;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public class PageLoader implements Function<URL, Optional<Page>> {
    @Override
    public Optional<Page> apply(URL url) {
        Optional<Page> result = Optional.empty();
        try {
            return Optional.of(new HtmlPage(url, Jsoup.connect(url.toString()).get()));
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                return Optional.of(new MissingPage(url));
            }
        } catch (Exception e) {
            log.error("Error Loading " + url, e);
        }
        return result;
    }
}