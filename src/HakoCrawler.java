import java.util.ArrayList;
import java.util.List;

public class HakoCrawler extends Crawler{

    private static final String prefix = "https://ln.hako.re";
    private List<String> chapters;
    private String novelTitle;

    public HakoCrawler(String novel) {
        super(novel);
        this.chapters = new ArrayList<>();
        this.novelTitle = getTitle().substring(0, getTitle().indexOf(" - Cổng Light Novel"));
    }

    public void parseHTMLToChapters() {
        boolean nextIsLink = false;
        for(String line: getHTML()) {

            if(nextIsLink == true && line.startsWith("<a")) {
                chapters.add(getLinkFrom(line, prefix));
                nextIsLink = false;
            }

            if(line.equals("<div class=\"chapter-name\">")) {
                nextIsLink = true;
            }
        }
    }

    public void getChapterContent() {
        for(String chapter: chapters) {
            
        }
    }

    public static void main(String[] args) {
        HakoCrawler crawler = new HakoCrawler("https://ln.hako.re/truyen/8437-aria-san-ban-ben-thi-thoang-lai-tha-thinh-toi-bang-tieng-nga");
        crawler.parseHTMLToChapters();
        System.out.println(crawler.novelTitle);
        for(String chapter: crawler.chapters) {
            System.out.println(chapter);
        }
    }
}