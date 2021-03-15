import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;

public class Text {

    private String text;
    private boolean bold;
    private boolean italic;
    private boolean underline;

    public Text(String text) {
        this(text, false, false, false);
    }

    public Text(String text, boolean bold, boolean italic, boolean underline) {
        this.text = text;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

    public void setBold(boolean state) {
        this.bold = state;
    }

    public void setItalic(boolean state) {
        this.italic = state;
    }

    public void setUnderline(boolean state) {
        this.underline = state;
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

    public boolean isUnderline() {
        return this.underline;
    }

    public static List<Text> parsePlainText(String line) {
        List<Text> ret = new ArrayList<>();
        
        for(String sentence: line.split("<[/a-z]+>")) {
            ret.add(new Text(sentence));
        }

        return ret;
    }

    public static List<Text> parseText(String line) {
        line = StringEscapeUtils.unescapeHtml4(line);
        // System.out.println(line);
        List<Text> ret = new ArrayList<>();
        for(int i=0; i<line.length(); ++i) {
            ret.add(new Text(line.substring(i, i+1)));
        }

        List<Integer> toRemove = new ArrayList<>();
        
        int i = 0;
        int begin = 0;
        while((i = line.indexOf("<strong>", begin)) != -1) {
            int j = line.indexOf("</strong>", begin);
            for(int index=i+8; index<j; ++index) {
                ret.get(index).setBold(true);
            }
            begin = j+9;
            toRemove.add(i);
            toRemove.add(i+8);
            toRemove.add(j);
            toRemove.add(j+9);
        }
        
        i = 0;
        begin = 0;
        while((i = line.indexOf("<b>", begin)) != -1) {
            int j = line.indexOf("</b>", begin);
            for(int index=i+3; index<j; ++index) {
                ret.get(index).setBold(true);
            }
            begin = j+4;
            toRemove.add(i);
            toRemove.add(i+3);
            toRemove.add(j);
            toRemove.add(j+4);
        }
        
        i = 0;
        begin = 0;
        while((i = line.indexOf("<h", begin)) != -1) {
            int j = line.indexOf("</h", begin);
            for(int index=i+3; index<j; ++index) {
                ret.get(index).setBold(true);
            }
            begin = j+5;
            toRemove.add(i);
            toRemove.add(i+4);
            toRemove.add(j);
            toRemove.add(j+5);
        }
        
        i = 0;
        begin = 0;
        while((i = line.indexOf("<em>", begin)) != -1) {
            int j = line.indexOf("</em>", begin);
            for(int index=i+4; index<j; ++index) {
                ret.get(index).setItalic(true);
            }
            begin = j+5;
            toRemove.add(i);
            toRemove.add(i+4);
            toRemove.add(j);
            toRemove.add(j+5);
        }
        
        i = 0;
        begin = 0;
        while((i = line.indexOf("<i>", begin)) != -1) {
            int j = line.indexOf("</i>", begin);
            for(int index=i+3; index<j; ++index) {
                ret.get(index).setItalic(true);
            }
            begin = j+4;
            toRemove.add(i);
            toRemove.add(i+3);
            toRemove.add(j);
            toRemove.add(j+4);
        }
        
        i = 0;
        begin = 0;
        while((i = line.indexOf("<u>", begin)) != -1) {
            int j = line.indexOf("</u>", begin);
            for(int index=i+3; index<j; ++index) {
                ret.get(index).setUnderline(true);
            }
            begin = j+4;
            toRemove.add(i);
            toRemove.add(i+3);
            toRemove.add(j);
            toRemove.add(j+4);
        }

        i = 0;
        begin = 0;
        while((i = line.indexOf("<p", begin)) != -1) {
            int j = line.indexOf("</p>", begin);
            begin = j+4;
            toRemove.add(i);
            toRemove.add(line.indexOf(">", i) + 1);
            toRemove.add(j);
            toRemove.add(j+4);
        }

        i = 0;
        begin = 0;
        while((i = line.indexOf("<span", begin)) != -1) {
            int j = line.indexOf("</span>", begin);
            begin = j+7;
            toRemove.add(i);
            toRemove.add(line.indexOf(">", i) + 1);
            toRemove.add(j);
            toRemove.add(j+7);
        }

        i = 0;
        for(i=0; i<toRemove.size(); i+=2) {
            begin = toRemove.get(i);
            int end = toRemove.get(i+1);
            // System.out.println(begin + "\t" + end);

            for(int index=begin; index<end; ++index) {
                ret.set(index, null);
            }
        }

        return ret;
    }
}
