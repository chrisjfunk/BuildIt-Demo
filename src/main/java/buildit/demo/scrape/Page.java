package buildit.demo.scrape;

import java.net.URL;
import java.util.List;

public interface Page {
    String getDescription();

    URL getUrl();

    List<Link> getInternalLinks();

    List<Link> getExternalLinks();

    List<Link> getMediaLinks();
}
