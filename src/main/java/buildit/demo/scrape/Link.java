package buildit.demo.scrape;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.net.URL;

@Getter
@RequiredArgsConstructor
@ToString
public class Link {
    private final URL url;
    private final String description;
}
