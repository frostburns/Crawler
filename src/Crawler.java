import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public abstract class Crawler {

    private static final int threshold = 5;
    private String title;

    public Crawler(String link) throws InterruptedException, IOException {
        int count = 0;
        while(true) {
            try {
                URLConnection uc = new URL(link).openConnection();
                uc.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                uc.connect();
                parseURL(uc);
                initTitle();
                break;
            }
            catch(IOException e) {
                if(++count == threshold) {
                    throw e;
                }
                TimeUnit.SECONDS.sleep(10);
            }
        }
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String fileNameFilter(String input) {
        StringBuilder sb = new StringBuilder(input);
        String[] chars = {"\\", "/", ":", "*", "?", "\"", "<", ">", "|"};
        for(String c: chars) {
            int i=0;
            while((i = sb.indexOf(c)) != -1) {
                sb.replace(i, i+1, "_");
            }
        }
        return sb.toString();
    }

    public abstract void initTitle();
    public abstract void parseURL(URLConnection uc) throws IOException;
}
