import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public abstract class Crawler {

    private String title;

    public Crawler(String link) throws IOException {
        URLConnection uc = new URL(link).openConnection();
        uc.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.connect();
        parseURL(uc);
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getLinkFrom(String line) {
        int begin = line.indexOf("href=\"") + 5;
        if(begin == 4) {
            begin = line.indexOf("src=\"") +4;
        }
        int end = line.indexOf("\"", begin+1);
        return line.substring(begin+1, end);
    }

    public String getLinkFrom(String line, String prefix) {
        return prefix + getLinkFrom(line);
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

    public String convertBracket(String input) {
        StringBuilder sb = new StringBuilder(input);
        int i=0;
        while((i = sb.indexOf("&lt;")) != -1) {
            sb.replace(i, i+4, "<");
        }
        while((i = sb.indexOf("&gt;")) != -1) {
            sb.replace(i, i+4, ">");
        }
        return sb.toString();
    }

    public abstract void parseURL(URLConnection uc) throws IOException;
}
