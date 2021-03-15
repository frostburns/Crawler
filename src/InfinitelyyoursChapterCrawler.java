import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InfinitelyyoursChapterCrawler extends HTMLCrawler {

    private List<String> content;

    public InfinitelyyoursChapterCrawler(String chapter) throws IOException {
        super(chapter);
        String title = getTitle();
        setTitle(title.substring(title.indexOf(" — ")+3, title.indexOf(" &#8211; Infinitely Yours")));
        System.out.println(getTitle());
    }
    
    public void parseHTML() {
        content = new ArrayList<>();
        int isParsing = 0;
        for (String line: getHTML()) {

            if (line.contains("<hr />") && isParsing == 2) {
                break;
            }

            if (line.contains("<div id=") && isParsing == 2) {
                break;
            }

            if (line.contains("<br />") && isParsing == 2) {
                content.add("");
                continue;
            }
            
            if (isParsing == 2) {
                content.add(line);
            }
            
            if (line.contains("<hr />") && isParsing == 1) {
                isParsing = 2;
            }

            if (line.contains("<div class=\"entry-content\">")) {
                isParsing = 1;
            }
        }
    }

    public List<String> getContent() {
        return this.content;
    }
}
