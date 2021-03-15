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
            if(line.contains("<title>")) {
                setTitle(line.substring(line.indexOf("<title>")+7, line.length()-8));
                break;
            }
        }
    }
    
    public abstract void parseHTML();

    public List<String> getHTML() {
        return this.html;
    }
    
    public static String getLinkFrom(String line) {

        int begin = line.indexOf("href=") + 6;
        if(begin == 5) {
            begin = line.indexOf("src=") +5;
        }

        char bracket = '\"';
        if (line.contains("href=\'") || line.contains("src=\'")) {
            bracket = '\'';
        }
        
        int end = line.indexOf(bracket, begin);
        return line.substring(begin, end);
    }
    
    public static String getAltFrom(String line) {

        int begin = line.indexOf("alt=") + 5;

        char bracket = '\"';
        if (line.contains("alt=\'")) {
            bracket = '\'';
        }

        int end = line.indexOf(bracket, begin);
        return line.substring(begin, end);
    }

    public static String getLinkFrom(String line, String prefix) {
        return prefix + getLinkFrom(line);
    }
}