package adriftbook.entity;
import java.io.InputStream;
/**
 * Created by Administrator on 2016/5/6.
 */
public class SubUploadFile extends UploadFile
{

    private InputStream imageInputStream;
    private InputStream fileInputStream;
    public InputStream getImageInputStream()
    {
        return imageInputStream;
    }
    public void setImageInputStream(InputStream imageInputStream)
    {
        this.imageInputStream = imageInputStream;
    }
    public InputStream getFileInputStream()
    {
        return fileInputStream;
    }
    public void setFileInputStream(InputStream fileInputStream)
    {
        this.fileInputStream = fileInputStream;
    }

}
