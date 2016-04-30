//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : AdriftBook.java
//  @ Date : 2016/4/23
//  @ Author : 
//
//
package adriftbook.entity;
public class AdriftBook
{

    public static final int ENTITYBOOK = 0;
    public AdriftBook()
    {
    }
    @Override public String toString()
    {
        return "AdriftBook{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", author='" + author + '\'' +
                ", rating=" + rating +
                ", type=" + type +
                ", bookImageUrl='" + bookImageUrl + '\'' +
                ", post=" + post +
                ", reviewPeopleCount=" + reviewPeopleCount +
                '}';
    }
    public static final int EBOOK = 1;
    int bookId;
    String bookName;
    String author;
    float rating;
    protected int type;  //0实体书,1电子书
    String bookImageUrl;
    Post post;
    public static final String BOOK_ID = "book_id";
    public static final String BOOK_NAME = "book_name";
    public static final String AUTHOR = "author";
    public static final String RATING = "rating";
    public static final String TYPE = "type";
    public static final String BOOK_IMAGE_URL = "book_image_url";
    public static final String EBOOK_URL = "ebook_url";
    public static final String REVIEW_COUNT="review_count";
    public int getBookId()
    {
        return bookId;
    }
    public void setBookId(int bookId)
    {
        this.bookId = bookId;
    }
    public int getType()
    {
        return type;
    }
    public void setType(int type)
    {
        this.type = type;
    }
    public String getBookImageUrl()
    {
        return bookImageUrl;
    }
    public Post getPost()
    {
        return post;
    }
    public void setPost(Post post)
    {
        this.post = post;
    }
    public void setBookImageUrl(String bookImageUrl)
    {
        this.bookImageUrl = bookImageUrl;
    }
    public AdriftBook(String bookName)
    {
        this.bookName = bookName;
    }
    public AdriftBook(String bookName, String author)
    {
        this(bookName);
        this.author = author;
        type = 0;
    }
    public int getReviewPeopleCount()
    {
        return reviewPeopleCount;
    }
    public void setReviewPeopleCount(int reviewPeopleCount)
    {
        this.reviewPeopleCount = reviewPeopleCount;
    }
    private int reviewPeopleCount;//对该书评论的个数
    public String getBookName()
    {
        return bookName;
    }
    public void setBookName(String bookName)
    {
        this.bookName = bookName;
    }
    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
    public float getRating()
    {
        return rating;
    }
    public void setRating(float rating)
    {
        this.rating = rating;
    }
}
