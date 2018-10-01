package buildit.demo.scrape;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface SiteMap {
    String getHost();

    List<CountedPage> getPages();

    default Optional<CountedPage> find(String child) {
        child = Optional.ofNullable(child).orElse("/");
        if (!child.startsWith("/")) {
            child = "/" + child;
        }
        final String search = child;
        return getPages().stream().filter(x ->
                x.getUrl().getFile().equalsIgnoreCase(search)).findFirst();
    }

    default List<Link> missingInternalLinks() {
        Stream<Link> allInternalLinks = getPages().stream().flatMap(x -> x.getInternalLinks().stream());
        return allInternalLinks.filter(l -> !(find(l.getUrl().getFile()).isPresent())).collect(Collectors.toList());
    }
}