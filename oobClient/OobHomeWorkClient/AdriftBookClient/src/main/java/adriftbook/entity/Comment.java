//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : Comment.java
//  @ Date : 2016/4/23
//  @ Author : 
//
//
package adriftbook.entity;
import java.util.Calendar;
public class Comment
{

    private AdriftBook commentBook;
    User commentUser;
    private String commentContent;
    private Calendar reviewDate;
    public static final String COMMENT_CONTENT = "comment_content";
    public static final String REVIEW_DATE = "review_date";
    public Comment()
    {
    }
    public Calendar getReviewDate()
    {
        return reviewDate;
    }
    public void setReviewDate(Calendar reviewDate)
    {
        this.reviewDate = reviewDate;
    }
    public String getCommentContent()
    {
        return commentContent;
    }
    public void setCommentContent(String commentContent)
    {
        this.commentContent = commentContent;
    }
    public AdriftBook getCommentBook()
    {
        return commentBook;
    }
    public void setCommentBook(AdriftBook commentBook)
    {
        this.commentBook = commentBook;
    }
    public User getCommentUser()
    {
        return commentUser;
    }
    public void setCommentUser(User commentUser)
    {
        this.commentUser = commentUser;
    }
}
