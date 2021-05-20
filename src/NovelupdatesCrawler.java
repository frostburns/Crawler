import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NovelupdatesCrawler extends HTMLCrawler {

    private static final String prefix = "https:";
    private List<String> chapters;
    
    public NovelupdatesCrawler(String novel) throws IOException {
        super(novel);
    }
    
    public void parseHTML() {
        chapters = new ArrayList<>();
        for (String line: getHTML()) {
            if (line.contains("extnu")) {
                chapters.add(getLinkFrom(line, prefix));
            }
        }
    }

    public List<String> getChapters() {
        return this.chapters;
    }
}