package buildit.demo;

import buildit.demo.scrape.Link;
import buildit.demo.scrape.PageCrawler;
import buildit.demo.scrape.PageLoader;
import buildit.demo.scrape.SiteMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StopWatch;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

import static java.lang.System.out;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

    @Value("${app.wait.seconds}")
    private int secondsToWait;

    @Value("${app.threads}")
    private int threads;

    @Value("${app.url}")
    private String url;


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", threads + "");
        out.println("Crawling " + url + " for a max of " + secondsToWait + " seconds using " + threads + " threads...");
        StopWatch stopWatch = new StopWatch("Web Crawler");
        stopWatch.start("Crawling " + url);
        SiteMap siteMap = crawler().apply(new URL(url));
        stopWatch.stop();
        Files.write(Paths.get("siteMap.json"),
                objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(siteMap).getBytes());
        List<Link> missingInternalLinks = siteMap.missingInternalLinks();
        if (missingInternalLinks.size() > 0) {
            out.println("Had some trouble with the following links, probabaly due to a http communication issue");
            missingInternalLinks.stream().map(x -> x.getUrl()).forEach(out::println);
        }
        out.println(stopWatch.prettyPrint());
        out.println("Crawled " + siteMap.getPages().size() + " pages from " + url + " open siteMap.json to see results");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Function<URL, SiteMap> crawler() {
        return url -> new PageCrawler(secondsToWait, new PageLoader()).apply(url);
    }
}
