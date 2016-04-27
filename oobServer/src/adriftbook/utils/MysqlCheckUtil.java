package adriftbook.utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.EBook;
import adriftbook.entity.Post;
import adriftbook.entity.User;
public class MysqlCheckUtil
{

    public static boolean userExists(String userName)
    {
        String sqlString = "select count(*) from user where username=" + "'"
                + userName + "'";
        ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
        // return false;
        try
        {
            if (rSet.next() && rSet.getInt(1) > 0)
                return true;
        }
        catch (SQLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    public static boolean userExists(String userName, String password)
    {
        // TODO Auto-generated method stub
        String sqlString = "select count(*) from user where username='"
                + userName + "' and userpassword=" + "'" + password + "'";
        ResultSet rSet = null;
        try
        {
            rSet = MysqlDbConnection.getResultSet(sqlString);
            if (rSet.next())
                if (rSet.getInt(1) > 0)
                    return true;
        }
        catch (Exception e)
        {
            // TODO: handle exception
        }
        return false;
    }
    public static User getUserInfo(int userId)
    {
        User user = null;
        try
        {
            String sqlString =
                    "select user_id,username,userpassword,registerdate,level from user where user_id=" +
                            userId + "";
            ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
            if (rSet.next())
            {
                user = new User(rSet.getString("username"),
                        rSet.getString("userpassword"));
                user.setUserId(rSet.getInt("user_id"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rSet.getLong("resiterdate"));
                user.setRegisterDate(calendar);
                user.setUserLevel(rSet.getInt("level"));
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return user;
    }
    public static User getUserInfo(String userName)
    {
        User user = null;
        try
        {
            String sqlString =
                    "select user_id,username,userpassword,registerdate,level from user where username='" +
                            userName + "'";
            ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
            if (rSet.next())
            {
                user = new User(rSet.getString("username"),
                        rSet.getString("userpassword"));
                user.setUserId(rSet.getInt(1));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rSet.getLong(4));
                user.setRegisterDate(calendar);
                user.setUserLevel(rSet.getInt("level"));
            }
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return user;
    }
    /**
     *
     * @param isLogin
     * @param requestType
     * @param m index item database starts to search
     * @param n  max return number
     * @return
     */
    public static ArrayList<Post> getPostsByType(boolean isLogin, int requestType,
                                                 int m, int n)
    {
        ArrayList<Post> posts = new ArrayList<Post>();
        synchronized (MysqlCheckUtil.class)
        {
            String sqlString = "select * from Post where  ";
            if (requestType >= 4)
            {
                sqlString += " posttype=1";
                requestType -= 4;
            }
            if (requestType >= 2)
            {
                if (sqlString.contains("1"))
                    sqlString += " or posttype=2";
                else
                    sqlString += " posttype=2";
                requestType -= 2;
            }
            if (requestType >= 1)
            {
                if (sqlString.contains("1") || sqlString.contains("2"))
                    sqlString += " or posttype=3";
                else
                    sqlString += " posttype=3";
                requestType -= 1;
            }
            sqlString += " order by registerdate desc limits " + m + "," + n + "";
            ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
            String userSqlString = "";
            ResultSet userSet = null;
            String bookSqlString = "";
            ResultSet bookSet = null;
            String commentSqlString = null;
            ResultSet commentSet = null;
            try
            {
                while (rSet.first())
                {
                    int userId = rSet.getInt("user_id");
                    userSqlString = "select * from user where user_id=" +
                            rSet.getInt("user_id") + " limit 0,1";
                    userSet = MysqlDbConnection.getResultSet(userSqlString);
                    User user = new User(userSet.getString("username"),
                            userSet.getString("userpassword"));
                    user.setUserId(userSet.getInt("user_id"));
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(userSet.getLong("registerdate"));
                    user.setRegisterDate(calendar);
                    user.setUserLevel(userSet.getInt("level"));
                    Post post = new Post(user, rSet.getString("posttitle"),
                            rSet.getString("content"));
                    calendar.setTimeInMillis(rSet.getLong("postdate"));
                    post.setPostDate(calendar);
                    post.setPostType(requestType);
                    post.setPostId(rSet.getInt("post_id"));
                    post.setReadCount(rSet.getInt("readcount"));
                    bookSqlString =
                            "select * from book where post_id=" + post.getPostId() +
                                    "";
                    bookSet = MysqlDbConnection.getResultSet(bookSqlString);
                    ArrayList<AdriftBook> books;
                    if (bookSet.isFirst())
                    {
                        books = new ArrayList<AdriftBook>();
                        while (rSet.next())
                        {
                            AdriftBook book = new AdriftBook(
                                    bookSet.getString("bookname"),
                                    bookSet.getString("author"));
                            book.setBookId(bookSet.getInt("book_id"));
                            book.setRating(bookSet.getFloat("rating"));
                            book.setType(bookSet.getInt("type"));
                            book.setBookImageUrl(bookSet.getString("bookimageurl"));
                            if (book.getType() == AdriftBook.EBOOK)
                                ((EBook) book)
                                        .setEbookUrl(bookSet.getString("ebookurl"));
                            books.add(book);
                        }
                        post.getPostContent().setBooks(books);
                    }
                    commentSqlString = "select * from comment where post_id=" +
                            post.getPostId() + "";
                    commentSet = MysqlDbConnection.getResultSet(commentSqlString);
                    ArrayList<Comment> comments;
                    if (commentSet.isFirst())
                    {
                        comments = new ArrayList<Comment>();
                        while (commentSet.next())
                        {
                            User commentUser = MysqlCheckUtil
                                    .getUserInfo(commentSet.getInt("user_id"));
                            Comment comment = new Comment(post, commentUser,
                                    commentSet.getString("commentcontent"));
                            comment.setCommentId(commentSet.getInt("comment_id"));
                            calendar.setTimeInMillis(
                                    commentSet.getLong("reviewdate"));
                            comment.setReviewDate(calendar);
                            comments.add(comment);
                        }
                        post.setComments(comments);
                    }
                    posts.add(post);
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return posts;
    }
}
