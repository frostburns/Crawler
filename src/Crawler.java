import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public abstract class Crawler {

    private static final int threshold = 5;
    private String title;
    private String link;

    public Crawler(String link) throws IOException {
        // System.out.println(link);
        this.link = link;
        int count = 0;
        while(true) {
            try {
                URLConnection uc = new URL(link).openConnection();
                uc.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36");
                uc.connect();
                parseURL(uc);
                initTitle();
                break;
            }
            catch(IOException e) {
                System.out.println(e.getMessage());
                if (++count == threshold) {
                    throw e;
                }
                try {
                    TimeUnit.SECONDS.sleep(10);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLink() {
        return this.link;
    }

    public static String fileNameFilter(String input) {
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
