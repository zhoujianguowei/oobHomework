package adriftbook.servlet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.User;
import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.RequestFilter;
/**
 * Created by Administrator on 2016/4/25.
 */
public class LoginServlet extends HttpServlet
{

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doGet(req, resp);
        doPost(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        Set<String> legalSet = new HashSet<String>();
        legalSet.add("username");
        legalSet.add("password");
        if (!RequestFilter.isRequestParamsLegal(req, resp, legalSet))
            return;
        JSONObject resJson = new JSONObject();
        try
        {
            resJson.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
            String username = CodeTransformUtil.getParameter(req, "username");
            String password = CodeTransformUtil.getParameter(req, "password");
            if (MysqlCheckUtil.userExists(username))
            {
                if (MysqlCheckUtil.userExists(username, password))
                {
                    resJson.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
                    resJson.put(Constant.INFO_KEY,
                            MysqlCheckUtil.getUserInfo(username));
                }
                else
                    resJson.put(Constant.INFO_KEY, "用户名和密码不匹配");
            }
            else
                resJson.put(Constant.INFO_KEY, "没有该用户");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        resp.getOutputStream()
                .write(resJson.toString().getBytes(Constant.DEFAULT_CODE));
    }
}
