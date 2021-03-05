import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NettruyenChapterCrawler extends HTMLCrawler {

    private static final String prefix = "http:";
    private List<String> content;

    public NettruyenChapterCrawler(String chapter) throws IOException {
        super(chapter);
        System.out.println(getTitle());
    }
    
    public void parseHTML() {
        content = new ArrayList<>();
        for(String line: getHTML()) {
            if(line.startsWith("<div id='page_1'")) {
                for(String link: line.split("</div><div")) {
                    content.add(HTMLCrawler.getLinkFrom(link, prefix, '\''));
                }
                return;
            }
        }
    }

    public List<String> getContent() {
        return this.content;
    }

    @Override
    public void initTitle() {
        List<String> html = getHTML();
        for(int i=0; i<html.size(); ++i) {
            if(html.get(i).contains("<title>")) {
                String title = html.get(i+1);
                setTitle(title.substring(0, title.indexOf(" Next Chap")));
                break;
            }
        }
    }
}
