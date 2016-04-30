package adriftbook.servlet;
import com.sun.istack.internal.Nullable;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.Post;
import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.RequestFilter;
import javafx.geometry.Pos;
/**
 * Created by Administrator on 2016/4/25.
 */
public class GetPostsServlet extends HttpServlet
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
        Set<String> legalSet = new HashSet<String>();
        legalSet.add("requestbooktype");
        legalSet.add("sendbooktype");
        legalSet.add("ebooktype");
        legalSet.add("page");
        if (!RequestFilter.isRequestParamsLegal(req, resp, legalSet))
            return;
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
            HashSet<String> tagSet = new HashSet<>();
            tagSet.add("tag");
            if (MysqlCheckUtil
                    .containsSpecifyRequestParam(req.getParameterNames(), tagSet))
                posts = getPosts(isLogin == 1 ? true : false, requestType, page,
                        CodeTransformUtil.getParameter(req, "tag"));
            else
                posts = getPosts(isLogin == 1 ? true : false, requestType, page,
                        null);
            resJs = new JSONObject();
            if (posts.size() > 0)
            {
                resJs.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
                resJs.put(Constant.INFO_KEY, MysqlCheckUtil.getPostJsonArray(posts));
            }
            else
                resJs.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    ArrayList<Post> getPosts(boolean isLogin, int requestType, int page,
                             @Nullable String tag)
    {
        ArrayList<Post> posts = MysqlCheckUtil
                .getPostsByType(isLogin, requestType, 0, -1);
        return posts;
    }
}
