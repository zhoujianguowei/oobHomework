package adriftbook.servlet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.User;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
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
    }
}
