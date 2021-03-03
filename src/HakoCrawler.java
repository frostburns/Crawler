import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
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

public class HakoCrawler extends Crawler {
    
    private static final String prefix = "https://ln.hako.re";
    private List<String> html;
    private List<String> chapters;
    
    public HakoCrawler(String novel) throws InterruptedException, IOException {
        super(novel);
        for(String line: html) {
            if(line.startsWith("<title>")) {
                String title = line.substring(7, line.length()-8);
                setTitle(title.substring(0, title.indexOf(" - Cổng Light Novel")));
                break;
            }
        }
        // System.out.println(getTitle());
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
    
    public void parseHTML() {
        chapters = new ArrayList<>();
        boolean nextIsLink = false;
        for (String line : html) {

            if (nextIsLink == true && line.startsWith("<a")) {
                chapters.add(getLinkFrom(line, prefix));
                nextIsLink = false;
            }

            if (line.equals("<div class=\"chapter-name\">")) {
                nextIsLink = true;
            }
        }
    }

    public List<String> getHTML() {
        return this.html;
    }

    public void getChapterContent() throws IOException, InterruptedException, InvalidFormatException {
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
                if (line.startsWith("<img")) {
                    run = paragraph.createRun();
                    try {
                        ImageCrawler image = new ImageCrawler(getLinkFrom(line));
                        int width = Math.min(450, image.getWidth());
                        run.addPicture(image.getInputStream(), image.getImageType(), image.getTitle(),
                                        Units.toEMU(width), Units.toEMU(width / image.getAspectRatio()));
                    }
                    catch (NullPointerException e) {
                        run.setText("Ảnh lỗi cmnr T_T");
                        run.setFontFamily("Times New Roman");
                        run.setFontSize(10);
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

            TimeUnit.SECONDS.sleep(1);
        }

        new File("hako.re/").mkdirs();
        FileOutputStream output = new FileOutputStream(new File("hako.re/" + fileNameFilter(getTitle()) + ".doc"));
        document.write(output);
        output.close();
        document.close();
        System.out.println(chapters.size() + " Chaps Done UwU");
    }
}