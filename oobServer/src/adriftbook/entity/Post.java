//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : Post.java
//  @ Date : 2016/4/23
//  @ Author : 
//
//
package adriftbook.entity;
import java.util.Calendar;
public class Post
{

    private int postId;
    private String title;
    private Calendar postDate;
    private int type;  //1求漂区，2放漂区，3电子书籍
    private int readCount;
    private String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public Calendar getPostDate()
    {
        return postDate;
    }
    public void setPostDate(Calendar postDate)
    {
        this.postDate = postDate;
    }
    public int getType()
    {
        return type;
    }
    public void setType(int type)
    {
        this.type = type;
    }
    public int getReadCount()
    {
        return readCount;
    }
    public void setReadCount(int readCount)
    {
        this.readCount = readCount;
    }
}
