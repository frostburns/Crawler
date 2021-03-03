import java.util.ArrayList;
import java.util.List;


public class HakoCrawler extends Crawler{

    private static final String prefix = "https://ln.hako.re";
    private List<String> chapters;
    private String novelTitle;

    public HakoCrawler(String novel) {
        super(novel);
        this.novelTitle = super.getTitle().substring(0, super.getTitle().indexOf(" - Cổng Light Novel"));
    }
    
    public void parseHTML() {
        this.chapters = new ArrayList<>();
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
            HakoChapterCrawler crawler = new HakoChapterCrawler(chapter);
            return;
        }
    }

    public String getTitle() {
        return this.novelTitle;
    }

    public static void main(String[] args) {
        HakoCrawler crawler = new HakoCrawler("https://ln.hako.re/truyen/8437-aria-san-ban-ben-thi-thoang-lai-tha-thinh-toi-bang-tieng-nga");
        System.out.println(crawler.novelTitle);
        crawler.getChapterContent();
    }
}