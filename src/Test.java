import java.io.IOException;

public class Test {
    
    public static void main(String[] args) throws IOException {
        String link = "https://ln.hako.re/truyen/8534-my-plain-looking-fiancee-is-secretly-sweet-with-me";
        NovelCrawler crawler = new HakoCrawler(link);
        crawler.getChapterContent();
    }
}
