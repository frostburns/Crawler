import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NovelupdatesMultiPagesCrawler extends HTMLCrawler {

    private List<String> chapters;
    
    public NovelupdatesMultiPagesCrawler(String novel) throws IOException {
        super(novel);
    }
    
    public void parseHTML() {
        List<String> pages = new ArrayList<>();
        pages.add(getLink());
        for (String line : getHTML()) {
            if (line.startsWith("<div class=\"digg_pagination\"")) {
                int end = line.indexOf("</a><a href=\"./?pg=2\" rel=\"next\"");
                int begin = line.substring(0, end).lastIndexOf(">") + 1;
                int lastPage = Integer.parseInt(line.substring(begin, end));
                
                for (int i=2; i<=lastPage; ++i) {
                    pages.add(getLink() + "?pg=" + i);
                }
                break;
            }
        }
        
        chapters = new ArrayList<>();
        for (String novel: pages) {
            try {
                NovelupdatesCrawler crawler = new NovelupdatesCrawler(novel);
                chapters.addAll(crawler.getChapters());
            }
            catch (IOException e) {
            }
        }
    }
    
    public void getChapterContent() {
        
    }
}