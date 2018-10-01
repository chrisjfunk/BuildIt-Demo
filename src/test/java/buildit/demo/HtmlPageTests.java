package buildit.demo;

import buildit.demo.scrape.Page;
import org.junit.Test;

import java.net.MalformedURLException;

import static buildit.demo.Util.load;
import static buildit.demo.Util.urlFor;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HtmlPageTests {

    @Test
    public void itShouldFindInternalLink() throws Exception {
        assertThat(Util.linkFor(page("home.html").getInternalLinks(), "about.html")).isPresent();
    }

    @Test
    public void itShouldFindExternalLink() throws Exception {
        assertThat(Util.linkFor(page("home.html").getExternalLinks(), "https://www.google.com")).isPresent();
    }


    @Test
    public void itShouldFindMediaLink() throws Exception {
        assertThat(Util.linkFor(page("home.html").getMediaLinks(), "smile.jpg")).isPresent();
        assertThat(Util.linkFor(page("home.html").getInternalLinks(), "smile.jpg")).isNotPresent();
    }


    private Page page(String name) throws MalformedURLException {
        return load(urlFor(name)).get();
    }
}
