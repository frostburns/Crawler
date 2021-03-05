import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public abstract class HTMLCrawler extends Crawler {
    
    private List<String> html;
    
    public HTMLCrawler(String link) throws IOException {
        super(link);
    }
    
    public void parseURL(URLConnection uc) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        html = new ArrayList<>();
        String line = "";
        while((line = reader.readLine()) != null) {
            html.add(line);
        }
        reader.close();
        
        parseHTML();
    }

    public void initTitle() {
        for(String line: html) {
            if(line.startsWith("<title>")) {
                setTitle(line.substring(7, line.length()-8));
                break;
            }
        }
    }
    
    public abstract void parseHTML();

    public List<String> getHTML() {
        return this.html;
    }

    public static String convertBracket(String input) {
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
    
    public static String getLinkFrom(String line, char bracket) {
        int begin = line.indexOf("href=" + bracket) + 5;
        if(begin == 4) {
            begin = line.indexOf("src=" + bracket) +4;
        }
        int end = line.indexOf(bracket, begin+1);
        return line.substring(begin+1, end);
    }

    public static String getLinkFrom(String line) {
        return getLinkFrom(line, '\"');
    }

    public static String getLinkFrom(String line, String prefix) {
        return prefix + getLinkFrom(line);
    }

    public static String getLinkFrom(String line, String prefix, char bracket) {
        return prefix + getLinkFrom(line, bracket);
    }
}