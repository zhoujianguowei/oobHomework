package adriftbook.servlet;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.Post;
import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import javafx.geometry.Pos;
/**
 * Created by Administrator on 2016/4/25.
 */
public class MainFrameServlet extends HttpServlet
{

    public static final int REQUESTBOOKMASTER = 1 << 2;
    public static final int SENDBOOKMASTER = 1 << 1;
    public static final int EBOOKMASTER = 1 << 0;
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doGet(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doPost(req, resp);
        int requestBookType, sendBookType, ebookType, isLogin;
        int page = 1, requestType;
        ArrayList<Post> posts;
        JSONObject resJs = null;
        try
        {
            requestBookType = Integer.parseInt(
                    CodeTransformUtil.getParameter(req, "requestbooktype"));
            sendBookType = Integer
                    .parseInt(CodeTransformUtil.getParameter(req, "sendbooktype"));
            ebookType = Integer
                    .parseInt(CodeTransformUtil.getParameter(req, "ebooktype"));
            isLogin = Integer
                    .parseInt(CodeTransformUtil.getParameter(req, "islogin"));
            page = Integer.parseInt(CodeTransformUtil.getParameter(req, "page"));
            requestType = requestBookType << 2 + sendBookType << 1 + ebookType;
            posts = getPosts(isLogin == 1 ? true : false, requestType, page);
            resJs = new JSONObject();
            if (posts.size() > 0)
            {
                resJs.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
                resJs.put(Constant.INFO_KEY, posts);
            }
            else
                resJs.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    ArrayList<Post> getPosts(boolean isLogin, int requestType, int page)
    {
        ArrayList<Post> posts = new ArrayList<Post>();
        ArrayList<Post> requestbookPosts = new ArrayList<Post>();
        ArrayList<Post> sendBookPosts = new ArrayList<Post>();
        ArrayList<Post> ebookPosts = new ArrayList<Post>();
        String sqlString = "";
        //求漂区
      /*  if(requestbookType==1)
        {

            requestbookPosts= MysqlCheckUtil.getPostsByType(isLogin,requestbookType,(page-1)*
                    Constant.PER_REQUEST_ITEMS,Constant.PER_REQUEST_ITEMS);
        }
        if(sendBookType==1)
            sendBookPosts=MysqlCheckUtil.getPostsByType(isLogin,sendBookType,(page-1))*/
        return posts;
    }
}
