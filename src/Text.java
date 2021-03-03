import java.util.ArrayList;
import java.util.List;

public class Text {

    private String text;
    private boolean bold;
    private boolean italic;

    public Text(String text) {
        this(text, false, false);
    }

    public Text(String text, boolean bold, boolean italic) {
        this.text = text;
        this.bold = bold;
        this.italic = italic;
    }

    public void setBold(boolean state) {
        this.bold = state;
    }

    public void setItalic(boolean state) {
        this.italic = state;
    }

    public String getText() {
        return this.text;
    }

    public boolean isBold() {
        return this.bold;
    }

    public boolean isItalic() {
        return this.italic;
    }

    // if the text is both bold & italic, i'm dead :)
    public static List<Text> parseToText(String line) {
        List<Text> ret = new ArrayList<>();
        for(int i=0; i<line.length(); ++i) {
            ret.add(null);
        }

        int i = 0;
        int begin = 0;
        while((i = line.indexOf("<strong>", begin)) != -1) {
            int j = line.indexOf("</strong>", begin);
            ret.set(i, new Text(line.substring(i+8, j), true, false));
            begin = j+8;
        }

        begin = 0;
        while((i = line.indexOf("<em>", begin)) != -1) {
            int j = line.indexOf("</em>", begin);
            ret.set(i, new Text(line.substring(i+4, j), false, true));
            begin = j+4;
        }

        return ret;
    }
}
