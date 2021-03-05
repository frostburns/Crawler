import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Test {
    
    public static void main(String[] args) throws IOException, InterruptedException, InvalidFormatException {
        NettruyenCrawler crawler = new NettruyenCrawler("http://www.nettruyen.com/truyen-tranh/xin-chao-ban-hoc-thu-dong-37484");
        crawler.getChapterContent();
    }
}
