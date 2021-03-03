import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Test {
    
    public static void main(String[] args) throws IOException, InterruptedException, InvalidFormatException {
        HakoCrawler crawler = new HakoCrawler("https://ln.hako.re/truyen/3660-2311");
        crawler.getChapterContent();
    }
}
