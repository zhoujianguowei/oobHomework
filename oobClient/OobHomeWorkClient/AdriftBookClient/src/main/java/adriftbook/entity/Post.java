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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
public class Post implements Serializable
{

    public static final int REQUEST_BOOK_AREA = 1;
    public static final int SEND_BOOK_AREA = 2;
    public static final int EBOOK_AREA = 3;
    public static final String HOST_POST_LABEL_STATUS = "火";
    public static final String NEW_POST_LABEL_STATUS = "新";
    User postUser;
    private int postId;
    private String postTitle;
    private PostContent postContent;
    private Calendar postDate;
    private int postType;  //1求漂区，2放漂区，3电子书籍
    private int readCount;
    private String labelStatus="";
    public static final String TAG="post";
    public String getLabelStatus()
    {
        return labelStatus;
    }
    public void setLabelStatus(String labelStatus)
    {
        this.labelStatus = labelStatus;
    }
    ArrayList<Comment> comments;
    public static final String POST_ID = "post_id";
    public static final String POST_TITLE = "post_title";
    public static final String POST_DATE = "post_date";
    public static final String POST_TYPE = "post_type";
    public static final String READ_COUNT = "read_count";
    public static final String POSTS_KEY = "posts";
    public static final String POSTS_COUNT_KEY = "posts_count";
    public static final String POST_CONTENT = "content";
    public Post()
    {
    }
    public Post(User postUser, String postTitle, String content)
    {
        this.postUser = postUser;
        this.postTitle = postTitle;
        postContent = new PostContent(content);
    }
    public PostContent getPostContent()
    {
        return postContent;
    }
    public void setPostContent(PostContent postContent)
    {
        this.postContent = postContent;
    }
    public ArrayList<Comment> getComments()
    {
        return comments;
    }
    public void setComments(ArrayList<Comment> comments)
    {
        this.comments = comments;
    }
    public String getPostTitle()
    {
        return postTitle;
    }
    public void setPostTitle(String postTitle)
    {
        this.postTitle = postTitle;
    }
    public int getPostId()
    {
        return postId;
    }
    public void setPostId(int postId)
    {
        this.postId = postId;
    }
    @Override public String toString()
    {
        return "Post{" +
                "postUser=" + postUser +
                ", postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postContent=" + postContent +
                ", postDate=" + postDate +
                ", postType=" + postType +
                ", readCount=" + readCount +
                ", comments=" + comments +
                '}';
    }
    public User getPostUser()
    {
        return postUser;
    }
    public void setPostUser(User postUser)
    {
        this.postUser = postUser;
    }
    public Calendar getPostDate()
    {
        return postDate;
    }
    public void setPostDate(Calendar postDate)
    {
        this.postDate = postDate;
    }
    public int getPostType()
    {
        return postType;
    }
    public void setPostType(int postType)
    {
        this.postType = postType;
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

