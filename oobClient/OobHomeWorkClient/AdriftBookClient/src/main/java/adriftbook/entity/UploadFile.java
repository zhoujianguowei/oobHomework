package adriftbook.entity;
import java.io.File;
import java.io.Serializable;
/**
 * Created by Administrator on 2016/5/5.
 */
public class UploadFile implements Serializable
{

    private String fileName;
    private String fileAuthor;
    private File imageFile;
    private File file;
    private String description;
    public File getImageFile()
    {
        return imageFile;
    }
    public void setImageFile(File imageFile)
    {
        this.imageFile = imageFile;
    }
    public String getFileName()
    {
        return fileName;
    }
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    public String getFileAuthor()
    {
        return fileAuthor;
    }
    public void setFileAuthor(String fileAuthor)
    {
        this.fileAuthor = fileAuthor;
    }
    public File getFile()
    {
        return file;
    }
    public void setFile(File file)
    {
        this.file = file;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
