import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NettruyenCrawler extends HTMLCrawler {

    private List<String> chapters;

    public NettruyenCrawler(String manga) throws IOException {
        super(manga);
        System.out.println(getTitle());
    }

    public void parseHTML() {
        chapters = new ArrayList<>();
        boolean nextIsLink = false;
        for (String line : getHTML()) {

            if (nextIsLink == true && line.startsWith("<a")) {
                chapters.add(getLinkFrom(line));
                nextIsLink = false;
            }

            if (line.equals("<div class=\"col-xs-5 chapter\">")) {
                nextIsLink = true;
            }
        }
    }

    public void getChapterContent() throws IOException {
        for(String chapter: chapters) {
            NettruyenChapterCrawler crawler = new NettruyenChapterCrawler(chapter);
            for(String link: crawler.getContent()) {
                System.out.println(link);
                // ImageCrawler image = new ImageCrawler(link);
            }
        }
    }

    @Override
    public void initTitle() {
        List<String> html = getHTML();
        for(int i=0; i<html.size(); ++i) {
            if(html.get(i).contains("<title>")) {
                String title = html.get(i+1);
                setTitle(title.substring(0, title.indexOf(" [Tới Chap")));
                break;
            }
        }
    }
}