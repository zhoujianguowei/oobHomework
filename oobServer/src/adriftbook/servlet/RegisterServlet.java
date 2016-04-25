package adriftbook.servlet;
import org.json.JSONObject;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import adriftbook.entity.User;
import adriftbook.utils.CodeTransformUtil;
import adriftbook.utils.Constant;
import adriftbook.utils.MysqlCheckUtil;
import adriftbook.utils.MysqlDbConnection;
/**
 * Created by Administrator on 2016/4/23.
 */
public class RegisterServlet extends HttpServlet
{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doGet(req, resp);
        doPost(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
//        super.doPost(req, resp);
      /*  resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<HEAD><TITLE>Hello World</TITLE></HEAD>");
        out.println("<BODY>");
        out.println("<BIG>Hello World</BIG>");
        out.println("</BODY></HTML>");*/
        String username = CodeTransformUtil.getParameter(req, "username");
        String password = CodeTransformUtil.getParameter(req, "password");
        JSONObject resInfo = new JSONObject();
        try
        {
            if (!MysqlCheckUtil.userExists(username))
            {
                User user = new User(username, password);
                insertUser(user);
                resInfo.put(Constant.STATUS_KEY, Constant.SUCCESS_VALUE);
            }
            else
            {
                resInfo.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
                resInfo.put(Constant.INFO_KEY, "用户名已经存在");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        resp.getOutputStream()
                .write(resInfo.toString().getBytes(Constant.DEFAULT_CODE));
    }
    private void insertUser(User user)
    {
        String sqlString =
                "insert into user(username,userpassword,registerdate) values('" +
                        user.getUserName() + "','" + user.getPassword() + "'," +
                        user.getRegisterDate().getTimeInMillis() +
                        ")";
        MysqlDbConnection.execute(sqlString);
    }
}
