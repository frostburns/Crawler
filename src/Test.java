import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class Test {
    
    public static void main(String[] args) throws IOException, InterruptedException, InvalidFormatException {
        NovelupdatesMultiPagesCrawler crawler = new NovelupdatesMultiPagesCrawler("https://www.novelupdates.com/series/2311/");
    }
}
