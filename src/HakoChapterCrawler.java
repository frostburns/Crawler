import java.util.ArrayList;
import java.util.List;

public class HakoChapterCrawler extends Crawler {

    private static final String prefix = "https://ln.hako.re";
    private List<String> content;
    private String chapterTitle;

    public HakoChapterCrawler(String chapter) {
        super(chapter);
        this.chapterTitle = super.getTitle().substring(super.getTitle().indexOf(" - ")+3,
        super.getTitle().indexOf(" - Cổng Light Novel"));
    }
    
    public void parseHTML() {
        this.content = new ArrayList<>();
        List<String> html = getHTML();
        for(int i=0; i<html.size(); ++i) {
            if(html.get(i).equals("<div id=\"chapter-content\" class=\"long-text no-select\">")) {
                for(String line: html.get(i+1).split("</p><p id=\"[0-9]+\">")) {
                    if(line.startsWith("<img")) {
                        line = getLinkFrom(line);
                    }
                    content.add(line);
                }

                String line = content.get(0);
                content.set(0, line.substring(10));
                line = content.get(content.size()-1);
                content.set(content.size()-1, line.substring(0, line.length()-4));

                for(String l: content) {
                    System.out.println(l);
                }

                return;
            }
        }
    }

    public String getTitle() {
        return this.chapterTitle;
    }

    public List<String> getContent() {
        return this.content;
    }
}