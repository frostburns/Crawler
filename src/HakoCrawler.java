import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class HakoCrawler extends Crawler {

    private static final String prefix = "https://ln.hako.re";
    private List<String> chapters;
    private String novelTitle;

    public HakoCrawler(String novel) throws IOException {
        super(novel);
        this.novelTitle = super.getTitle().substring(0, super.getTitle().indexOf(" - Cổng Light Novel"));
    }

    public void parseHTML() {
        this.chapters = new ArrayList<>();
        boolean nextIsLink = false;
        for (String line : getHTML()) {

            if (nextIsLink == true && line.startsWith("<a")) {
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
        FileOutputStream out = new FileOutputStream(new File(fileNameFilter(novelTitle) +".docx"));

        for(String chapter: chapters) {
            HakoChapterCrawler crawler = new HakoChapterCrawler(chapter);

            XWPFParagraph paragraph = document.createParagraph();
            paragraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = paragraph.createRun();
            run.setText(crawler.getTitle());
            run.setBold(true);
            run.setFontFamily("Times New Roman");
            run.setFontSize(14);
            
            for(String line: crawler.getContent()) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText(line);
                run.setFontFamily("Times New Roman");
                run.setFontSize(14);
            }
        }

        document.write(out);
        out.close();
        document.close();
        System.out.println("Done :3");
    }

    public String getTitle() {
        return this.novelTitle;
    }

    public static void main(String[] args) throws IOException {
        HakoCrawler crawler = new HakoCrawler("https://ln.hako.re/truyen/3660-2311");
        System.out.println(crawler.novelTitle);
        crawler.getChapterContent();
    }
}