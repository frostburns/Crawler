import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.Document;

public class ImageCrawler extends Crawler {

    private BufferedImage image;
    private int imageType;
    
    public ImageCrawler(String link) throws IOException {
        super(link);
        setImageType(link.substring(link.lastIndexOf(".")));
        System.out.println(getTitle());
    }

    public void parseURL(URLConnection uc) {
        try {
            this.image = ImageIO.read(new BufferedInputStream(uc.getInputStream()));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void initTitle() {
        setTitle(getLink().substring(getLink().lastIndexOf("/")+1));
    }

    private void setImageType(String suffix) {
        if(suffix.equals(".jpg")) {
            this.imageType = Document.PICTURE_TYPE_JPEG;
        }
        if(suffix.equals(".png")) {
            this.imageType = Document.PICTURE_TYPE_PNG;
        }
    }

    public int getImageType() {
        return this.imageType;
    }

    public void save(String dir) throws IOException {
        this.save(dir, getTitle());
    }

    public void save(String dir, String fileName) throws IOException {
        ImageIO.write(image, "jpg", new File(Crawler.fileNameFilter(fileName)));
    }

    public InputStream getInputStream() throws IOException {
        URLConnection uc = new URL(getLink()).openConnection();
        uc.addRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.connect();
        return uc.getInputStream();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public double getAspectRatio() {
        return (double) getWidth() / getHeight();
    }
}
