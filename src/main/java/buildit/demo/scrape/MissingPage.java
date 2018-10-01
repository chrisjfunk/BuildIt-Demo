package buildit.demo.scrape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class MissingPage implements Page {
    private final URL url;

    @Override
    public String getDescription() {
        return "Not Found";
    }

    @Override
    public List<Link> getInternalLinks() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Link> getExternalLinks() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Link> getMediaLinks() {
        return Collections.EMPTY_LIST;
    }
}
