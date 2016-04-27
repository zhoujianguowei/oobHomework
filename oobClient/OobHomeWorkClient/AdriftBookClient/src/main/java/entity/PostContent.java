package entity;
import java.util.ArrayList;
/**
 * Created by Administrator on 2016/4/25.
 */
public class PostContent
{

    String contentTitle;
    ArrayList<AdriftBook> books;
    PostContent(String contentTitle)
    {
        this.contentTitle = contentTitle;
        books = new ArrayList<AdriftBook>();
    }
    public ArrayList<AdriftBook> getBooks()
    {
        return books;
    }
    public void setBooks(ArrayList<AdriftBook> books)
    {
        this.books = books;
    }
    public String getContentTitle()
    {
        return contentTitle;
    }
    public void setContentTitle(String contentTitle)
    {
        this.contentTitle = contentTitle;
    }
}
