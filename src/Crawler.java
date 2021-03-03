import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public abstract class Crawler {

    private List<String> html;
    private String title;

    public Crawler(String link) throws IOException {
        URL url= new URL(link);
        URLConnection uc = url.openConnection();
        uc.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        
        this.html = new ArrayList<>();
        String line = "";
        while((line = reader.readLine()) != null) {
            this.html.add(line);
        }
        setTitle();
        parseHTML();
        
        reader.close();
    }
    
    private void setTitle() {
        for(String line: html) {
            if(line.startsWith("<title>")) {
                title = line.substring(7, line.length()-8);
                return;
            }
        }
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

    public List<String> getHTML() {
        return this.html;
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

    public abstract void parseHTML();
}
