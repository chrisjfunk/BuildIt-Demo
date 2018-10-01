package buildit.demo;

import buildit.demo.scrape.CountedPage;
import buildit.demo.scrape.Link;
import buildit.demo.scrape.SiteMap;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"app.url=https://buildit.wiprodigital.com/", "logging.level.root=INFO"})
public class IntegrationTest {
    @Autowired
    private ObjectMapper mapper;
    @Test
    public void crawl() throws Exception {
        MySiteMap mySiteMap = mapper.readValue(new File("siteMap.json"), MySiteMap.class);
        assertThat(mySiteMap.getHost()).isEqualTo("buildit.wiprodigital.com");
        assertThat(mySiteMap.getPages().size()).isGreaterThan(1);
        assertThat(mySiteMap.missingInternalLinks().size()).isEqualTo(0);
    }
}


@Data
@NoArgsConstructor
class MySiteMap implements SiteMap {
    String host;
    @JsonDeserialize(contentAs = MyCountedPage.class)
    List<CountedPage> pages;
}

@Data
class MyCountedPage implements CountedPage {
    int inboundLinkCount;
    String description;
    URL url;
    @JsonDeserialize(contentAs = MyLink.class)
    List<Link> internalLinks;
    @JsonDeserialize(contentAs = MyLink.class)
    List<Link> externalLinks;
    @JsonDeserialize(contentAs = MyLink.class)
    List<Link> mediaLinks;
}

class MyLink extends Link {
    @JsonCreator
    public MyLink(@JsonProperty("url") URL url, @JsonProperty("description") String description) throws Exception {
        super(url, description);
    }
}