package adriftbook.servlet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.CommonUtils;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.MysqlDbConnection;
import adriftbook.utils.RequestFilter;
/**
 * Created by Administrator on 2016/5/2.
 */
public class DoCommentServlet extends HttpServlet
{

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        doPost(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        HashSet<String> filterSet = new HashSet<>();
        filterSet.add("user_id");
        filterSet.add("book_id");
        if (!RequestFilter.isRequestParamsLegal(req, resp, filterSet))
            return;
        /*Map<String,String> requestParams=CommonUtils.getVolleyPostRequestParams(req);
        System.out.println("params:"+requestParams);*//*Map<String,String> requestParams=CommonUtils.getVolleyPostRequestParams(req);
        System.out.println("params:"+requestParams);*/
        System.out.println(CommonUtils.getVolleyRequestString(req));
        JSONObject obj = new JSONObject();
        int userId = Integer
                .parseInt(CodeTransformUtil.getParameter(req, "user_id"));
        int bookId = Integer
                .parseInt(CodeTransformUtil.getParameter(req, "book_id"));
//        System.out.println("body:"+commentContent);
        try
        {
            obj.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
            if (MysqlCheckUtil.isCommentExists(userId, bookId))
                obj.put(Constant.INFO_KEY, "你已经对该书评论过了");
            else
            {
                obj.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
//                MysqlCheckUtil.doComment(userId,bookId,);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        MysqlDbConnection.closeConnection();
        resp.getOutputStream().write(obj.toString().getBytes(Constant.DEFAULT_CODE));
    }
}
