package buildit.demo.scrape;

public interface CountedPage extends Page, Comparable<CountedPage> {
    int getInboundLinkCount();

    @Override
    default int compareTo(CountedPage other) {
        return Integer.compare(other.getInboundLinkCount(), getInboundLinkCount());
    }
}
