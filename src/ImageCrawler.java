import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.Document;

public class ImageCrawler extends Crawler {

    private BufferedImage image;
    private String link;
    private int imageType;
    
    public ImageCrawler(String link) throws InterruptedException, IOException {
        super(link);
        this.link = link;
        setImageType(link.substring(link.lastIndexOf(".")));
        System.out.println(getTitle());
    }

    public void parseURL(URLConnection uc) throws IOException {
        this.image = ImageIO.read(new BufferedInputStream(uc.getInputStream()));
    }

    public void initTitle() {
        setTitle(link.substring(link.lastIndexOf("/")+1));
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

    public InputStream getInputStream() throws IOException {
        URLConnection uc = new URL(link).openConnection();
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
