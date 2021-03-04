import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HakoChapterCrawler extends HTMLCrawler {

    private List<String> content;

    public HakoChapterCrawler(String chapter) throws InterruptedException, IOException {
        super(chapter);
        for(String line: getHTML()) {
            if(line.startsWith("<title>")) {
                String title = line.substring(7, line.length()-8);
                setTitle(title.substring(title.indexOf(" - ")+3, title.indexOf(" - Cổng Light Novel")));
                break;
            }
        }
        System.out.println(getTitle());
    }
    
    public void parseHTML() {
        content = new ArrayList<>();
        List<String> html = getHTML();
        for(int i=0; i<html.size(); ++i) {
            if(html.get(i).equals("<div id=\"chapter-content\" class=\"long-text no-select\">")) {
                for(String line: html.get(i+1).split("</p><p id=\"[0-9]+\">")) {
                    content.add(convertBracket(line));
                }

                String line = content.get(0);
                content.set(0, line.substring(10));
                line = content.get(content.size()-1);
                content.set(content.size()-1, line.substring(0, line.length()-4));

                return;
            }
        }
    }

    public List<String> getContent() {
        return this.content;
    }
}