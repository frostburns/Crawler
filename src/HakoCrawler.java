import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class HakoCrawler extends HTMLCrawler {
    
    private static final String prefix = "https://ln.hako.re";
    private List<String> chapters;
    
    public HakoCrawler(String novel) throws IOException {
        super(novel);
        String title = getTitle();
        setTitle(title.substring(0, title.indexOf(" - Cổng Light Novel")));
        System.out.println(getTitle());
    }
    
    public void parseHTML() {
        chapters = new ArrayList<>();
        boolean nextIsLink = false;
        for (String line : getHTML()) {

            if (nextIsLink == true && line.contains("<a")) {
                chapters.add(getLinkFrom(line, prefix));
                nextIsLink = false;
            }

            if (line.equals("<div class=\"chapter-name\">")) {
                nextIsLink = true;
            }
        }
    }

    public void getChapterContent() throws IOException {
        XWPFDocument document = new XWPFDocument();

        for (String chapter : chapters) {
            HakoChapterCrawler crawler = new HakoChapterCrawler(chapter);

            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setText(crawler.getTitle());
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(16);

            for (String line : crawler.getContent()) {
                paragraph = document.createParagraph();
                if (line.contains("<img")) {
                    run = paragraph.createRun();
                    ImageCrawler image = new ImageCrawler(getLinkFrom(line));
                    try {
                        int width = Math.min(450, image.getWidth());
                        run.addPicture(image.getInputStream(), image.getImageType(), image.getTitle(),
                                        Units.toEMU(width), Units.toEMU(width / image.getAspectRatio()));
                    }
                    catch(NullPointerException e) {
                        run.setText("[Ảnh lỗi cmnr T_T] " + image.getTitle());
                        run.setFontFamily("Times New Roman");
                        run.setFontSize(10);
                    }
                    catch(InvalidFormatException e) {
                        e.printStackTrace();
                    }
                }
                else {
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
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        new File("hako.re/").mkdirs();
        FileOutputStream output = new FileOutputStream(new File("hako.re/" + fileNameFilter(getTitle()) + ".doc"));
        document.write(output);
        output.close();
        document.close();
        System.out.println(chapters.size() + " Chaps Done UwU");
    }
}