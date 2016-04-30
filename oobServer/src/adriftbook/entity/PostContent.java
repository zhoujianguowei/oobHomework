package adriftbook.entity;
import java.util.ArrayList;
/**
 * Created by Administrator on 2016/4/25.
 */
public class PostContent
{

    String postContentDetail;
    ArrayList<AdriftBook> books;
    PostContent(String postContentDetail)
    {
        this.postContentDetail = postContentDetail;
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
    public String getPostContentDetail()
    {
        return postContentDetail;
    }
    public void setPostContentDetail(String postContentDetail)
    {
        this.postContentDetail = postContentDetail;
    }
}
