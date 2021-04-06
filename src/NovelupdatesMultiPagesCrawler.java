import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
// import java.util.concurrent.TimeUnit;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class NovelupdatesMultiPagesCrawler extends HTMLCrawler implements NovelCrawler {

    private List<String> chapters;
    
    public NovelupdatesMultiPagesCrawler(String novel) throws IOException {
        super(novel);
        String title = getTitle();
        setTitle(title.substring(0, title.indexOf(" - Novel Updates")));
        System.out.println(getTitle());
    }
    
    public void parseHTML() {
        List<String> pages = new ArrayList<>();
        pages.add(getLink());
        for (String line : getHTML()) {
            if (line.contains("<div class=\"digg_pagination\"")) {
                int end = line.indexOf("</a><a href=\"./?pg=2\" rel=\"next\"");
                int begin = line.substring(0, end).lastIndexOf(">") + 1;
                int lastPage = Integer.parseInt(line.substring(begin, end));
                
                for (int i=2; i<=lastPage; ++i) {
                    pages.add(getLink() + "?pg=" + i);
                }
                break;
            }
        }
        
        chapters = new ArrayList<>();
        for (String novel: pages) {
            try {
                NovelupdatesCrawler crawler = new NovelupdatesCrawler(novel);
                chapters.addAll(crawler.getChapters());
            }
            catch (IOException e) {
            }
        }
        Collections.reverse(chapters);
    }
    
    public void getChapterContent() throws IOException {
        XWPFDocument document = new XWPFDocument();

        for (String chapter : chapters) {
            InfinitelyyoursChapterCrawler crawler = new InfinitelyyoursChapterCrawler(chapter);

            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setText(crawler.getTitle());
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(16);

            for (String line : crawler.getContent()) {
                if (line.contains("<img")) {
                    int begin = line.indexOf("<img");
                    int end = line.indexOf(">", begin)+1;
                    line = line.substring(0, begin) + getAltFrom(line) + line.substring(end, line.length());
                }
                
                paragraph = document.createParagraph();
                List<Text> sentences = Text.parseText(line);
                for(Text sentence: sentences) {
                    if(sentence != null) {
                        run = paragraph.createRun();
                        run.setText(sentence.getText());
                        run.setFontFamily("Times New Roman");
                        run.setFontSize(12);
                        run.setBold(sentence.isBold());
                        run.setItalic(sentence.isItalic());
                        if(sentence.isUnderline()) {
                            run.setUnderline(UnderlinePatterns.SINGLE);
                        }
                    }
                }
            }

            // try {
            //     TimeUnit.SECONDS.sleep(1);
            // }
            // catch(InterruptedException e) {
            //     e.printStackTrace();
            // }
        }

        new File("novelupdates/").mkdirs();
        FileOutputStream output = new FileOutputStream(new File("novelupdates/" + fileNameFilter(getTitle()) + ".doc"));
        document.write(output);
        output.close();
        document.close();
        System.out.println(chapters.size() + " Chaps Done UwU");
    }
}