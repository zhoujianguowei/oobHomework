package adriftbook.entity;
import java.util.ArrayList;
/**
 * Created by Administrator on 2016/4/25.
 */
class PostContent
{

    String contentTitle;
    String books;
    PostContent(String contentTitle)
    {
        this.contentTitle = contentTitle;
    }
    public String getContentTitle()
    {
        return contentTitle;
    }
    public void setContentTitle(String contentTitle)
    {
        this.contentTitle = contentTitle;
    }
    public String getBooks()
    {
        return books;
    }
    public void setBooks(String books)
    {
        this.books = books;
    }
}
