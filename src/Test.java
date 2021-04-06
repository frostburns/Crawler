import java.io.IOException;

public class Test {
    
    public static void main(String[] args) throws IOException {
        NovelCrawler crawler = new HakoCrawler("https://ln.hako.re/truyen/4012-hello-hello-and-hello/t7137-hello-hello-and-hello");
        crawler.getChapterContent();
    }
}
