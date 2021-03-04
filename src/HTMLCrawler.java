import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public abstract class HTMLCrawler extends Crawler {
    
    private List<String> html;
    
    public HTMLCrawler(String link) throws InterruptedException, IOException {
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
    
    public abstract void parseHTML();

    public List<String> getHTML() {
        return this.html;
    }
}