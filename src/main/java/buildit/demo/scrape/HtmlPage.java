package buildit.demo.scrape;

import lombok.Getter;
import lombok.ToString;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString()
public class HtmlPage extends Link implements Page {
    public static final String WITH_HREF = "a[href]";
    public static final String WITH_SRC = "[src]";
    private final List<Link> internalLinks;
    private final List<Link> externalLinks;
    private final List<Link> mediaLinks;

    public HtmlPage(URL url, Document html) {
        super(url, html.title());
        internalLinks = Collections.unmodifiableList(html.select(WITH_HREF)
                .stream().filter(this::internal).map(HtmlPage::link)
                .collect(Collectors.toList()));
        externalLinks = Collections.unmodifiableList(html.select(WITH_HREF)
                .stream().filter(this::external).map(HtmlPage::link)
                .collect(Collectors.toList()));
        mediaLinks = Collections.unmodifiableList(html.select(WITH_SRC)
                .stream().map(HtmlPage::mediaLink)
                .collect(Collectors.toList()));
    }


    private boolean internal(Element link) {
        try {
            return url(link.attr("abs:href")).getHost().equals(getUrl().getHost());
        } catch (MalformedURLException badUrl) {
            return false;
        }
    }

    private boolean external(Element link) {
        try {
            return !url(link.attr("abs:href")).getHost().equals(getUrl().getHost());
        } catch (MalformedURLException badUrl) {
            return false;
        }
    }

    private static Link link(Element link) {
        try {
            return new Link(url(link.attr("abs:href")), link.text());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static Link mediaLink(Element l) {
        try {
            return new Link(url(l.attr("abs:src")), l.tagName());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private static URL url(String url) throws MalformedURLException {
        return new URL(url);
    }

}