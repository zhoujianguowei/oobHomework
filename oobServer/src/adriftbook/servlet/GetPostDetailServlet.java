package adriftbook.servlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.EBook;
import adriftbook.entity.User;
import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.MysqlDbConnection;
import adriftbook.utils.RequestFilter;
/**
 * Created by Administrator on 2016/5/1.
 */
public class GetPostDetailServlet extends HttpServlet
{

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doGet(req, resp);
        doPost(req, resp);
    }
    private ArrayList<AdriftBook> getBooks(int postId)
    {
        ArrayList<AdriftBook> adriftBooks = new ArrayList<>();
        String sqlString = "select * from book where post_id=" + postId;
        ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
        try
        {
            while (rSet.next())
            {
                AdriftBook book = null;
                int bookType = rSet.getInt("type");
                if (bookType == AdriftBook.ENTITYBOOK)
                {
                    book = new AdriftBook();
                }
                else if (bookType == AdriftBook.ENTITYBOOK)
                {
                    book = new EBook();
                    ((EBook) book).setEbookUrl(rSet.getString("ebookurl"));
                }
                book.setBookId(rSet.getInt("book_id"));
                book.setBookName(rSet.getString("bookname"));
                book.setAuthor(rSet.getString("author"));
                book.setRating(rSet.getFloat("rating"));
                book.setType(bookType);
                book.setBookImageUrl(rSet.getString("bookimageurl"));
                book.setReviewPeopleCount(rSet.getInt("reviewPeopleCount"));
                adriftBooks.add(book);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return adriftBooks;
    }
    private ArrayList<Comment> getComments(int bookId)
    {
        ArrayList<Comment> commentList = new ArrayList<>();
        String sqlString = "select * from comment where book_id=" + bookId;
        ResultSet rSet = MysqlDbConnection.getResultSet(sqlString);
        try
        {
            while (rSet.next())
            {
                Comment comment = new Comment();
                comment.setCommentContent(rSet.getString("commentcontent"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(rSet.getLong("reviewdate"));
                comment.setReviewDate(calendar);
                User commentUser = MysqlCheckUtil
                        .getUserInfo(rSet.getInt("user_id"));
                comment.setCommentUser(commentUser);
                commentList.add(comment);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return commentList;
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doPost(req, resp);
        HashSet<String> postDetailSet = new HashSet<>();
        postDetailSet.add("post_id");
        if (!RequestFilter.isRequestParamsLegal(req, resp, postDetailSet))
            return;
        int postId = Integer
                .parseInt(CodeTransformUtil.getParameter(req, "post_id"));
        ArrayList<AdriftBook> bookList = getBooks(postId);
        JSONObject rJObj = new JSONObject();
        try
        {
            rJObj.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
            JSONArray bookJArray = new JSONArray();
            for (AdriftBook book : bookList)
            {
                ArrayList<Comment> commentList = getComments(book.getBookId());
                JSONObject bookJObj = new JSONObject();
                bookJObj.put("book", MysqlCheckUtil.getJsonObj(book));
                bookJObj.put("comments",
                        MysqlCheckUtil.getCommentJsonArray(commentList));
                bookJArray.put(bookJObj);
            }
            rJObj.put(Constant.INFO_KEY, bookJArray);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        MysqlDbConnection.closeConnection();
        resp.getOutputStream()
                .write(rJObj.toString().getBytes(Constant.DEFAULT_CODE));
    }
}
