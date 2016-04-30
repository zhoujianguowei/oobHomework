package adriftbook.utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.EBook;
import adriftbook.entity.Post;
import adriftbook.entity.PostContent;
import adriftbook.entity.User;
import adriftbook.servlet.GetPostsServlet;
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
                calendar.setTimeInMillis(rSet.getLong("registerdate"));
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
    public static boolean containsSpecifyRequestParam(
            Enumeration<String> requestEnumeration, Set<String> requestSet)
    {
        while (requestEnumeration.hasMoreElements())
        {
            String element = requestEnumeration.nextElement();
            if (requestSet.contains(element))
                requestSet.remove(element);
            else
                break;
        }
        return requestSet.size() == 0 ? true : false;
    }
    public static JSONObject getJsonObj(Object obj, int... params) throws Exception
    {
        JSONObject resJson = new JSONObject();
        if (obj instanceof User)
        {
            User user = (User) obj;
            resJson.put(User.USER_ID, user.getUserId());
            resJson.put(User.USER_NAME, user.getUserName());
            resJson.put(User.USER_LEVEL, user.getUserLevel());
            resJson.put(User.REGISTER_DATE,
                    user.getRegisterDate().getTimeInMillis());
            resJson.put(User.PASSWORD, user.getPassword());
        }
        else if (obj instanceof Comment)
        {
            Comment comment = (Comment) obj;
            resJson.put(Comment.COMMENT_CONTENT, comment.getCommentContent());
            resJson.put(Comment.REVIEW_DATE,
                    comment.getReviewDate().getTimeInMillis());
            JSONObject userJson = getJsonObj(MysqlCheckUtil.getUserInfo(params[0]));
            resJson.put("user", userJson);
        }
        else if (obj instanceof Post)
        {
            Post post = (Post) obj;
            resJson.put(Post.POST_TITLE, post.getPostTitle());
            resJson.put(Post.POST_DATE, post.getPostDate().getTimeInMillis());
            resJson.put(Post.READ_COUNT, post.getReadCount());
            resJson.put(Post.POST_TYPE, post.getPostType());
            resJson.put(Post.POST_CONTENT,
                    post.getPostContent().getPostContentDetail());
            if (params.length > 0)
            {
                JSONObject userJson = getJsonObj(
                        MysqlCheckUtil.getUserInfo(params[0]));
                resJson.put("user", userJson);
            }
           /* if(params.length>1)
            {
                JSONArray commentJArray=new JSONArray();
            resJson.put("comment",)
            }*/
        }
        else if (obj instanceof AdriftBook)
        {
            AdriftBook book = (AdriftBook) obj;
            resJson.put(AdriftBook.BOOK_ID, book.getBookId());
            resJson.put(AdriftBook.AUTHOR, book.getAuthor());
            resJson.put(AdriftBook.BOOK_IMAGE_URL, book.getBookImageUrl());
            resJson.put(AdriftBook.BOOK_NAME, book.getBookName());
            resJson.put(AdriftBook.RATING, book.getRating());
            resJson.put(AdriftBook.TYPE, book.getType());
            resJson.put(AdriftBook.REVIEW_COUNT, book.getReviewPeopleCount());
            if (obj instanceof EBook)
                resJson.put(AdriftBook.EBOOK_URL,
                        ((EBook) book).getReviewPeopleCount());
        }
        return resJson;
    }
    public static JSONArray getPostJsonArray(ArrayList<Post> posts)
    {
        JSONArray postJArray = new JSONArray();
        for (Post post : posts)
            try
            {
                postJArray.put(getJsonObj(post, post.getPostUser().getUserId()));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        return postJArray;
    }
    /**
     *
     * @param requestType
     * @param m index item database starts to search
     * @param n  max return number
     * @return key posts denotes required posts and count denotes posts count
     */
    public static HashMap<String, Object> getPostsByType(int requestType,
                                                         int m, int n, String tag)
    {
        ArrayList<Post> posts = new ArrayList<Post>();
        Integer postsCount = 0;
        HashMap<String, Object> resMap = new HashMap<>();
        synchronized (MysqlCheckUtil.class)
        {
            String sqlString = "select * from post where  ";
            String append = "";
            if (requestType >= GetPostsServlet.REQUESTBOOKMASTER)
            {
                append = " posttype=1";
                requestType -= GetPostsServlet.REQUESTBOOKMASTER;
            }
            if (requestType >= GetPostsServlet.SENDBOOKMASTER)
            {
                if (append.contains("1"))
                    append += " or posttype=2";
                else
                    append += " posttype=2";
                requestType -= GetPostsServlet.SENDBOOKMASTER;
            }
            if (requestType >= GetPostsServlet.EBOOKMASTER)
            {
                if (append.contains("1") || append.contains("2"))
                    append += " or posttype=3";
                else
                    append += " posttype=3";
                requestType -= GetPostsServlet.EBOOKMASTER;
            }
            sqlString += append;
            try
            {
                ResultSet rSet = MysqlDbConnection
                        .getResultSet("select count(*) from post where " + append);
                if (rSet.next())
                    postsCount = rSet.getInt(1);
                sqlString += " order by postdate desc limit " + m + "," + n + "";
                rSet = MysqlDbConnection.getResultSet(sqlString);
                Calendar calendar = Calendar.getInstance();
                while (rSet.next())
                {
                    int userId = rSet.getInt("user_id");
                    User user = MysqlCheckUtil.getUserInfo(userId);
                    Post post = new Post();
                    calendar.setTimeInMillis(rSet.getLong("postdate"));
                    post.setPostUser(user);
                    post.setPostTitle(rSet.getString("posttitle"));
                    post.setPostDate(calendar);
                    post.setPostType(rSet.getInt("posttype"));
                    post.setPostId(rSet.getInt("post.post_id"));
                    post.setReadCount(rSet.getInt("readcount"));
                    PostContent postContent = new PostContent(
                            rSet.getString("content"));
                    post.setPostContent(postContent);
                    posts.add(post);
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        Collections.sort(posts, new PostComparator());
        resMap.put(Post.POSTS_COUNT_KEY, postsCount);
        resMap.put(Post.POSTS_KEY, posts);
        return resMap;
    }
}
