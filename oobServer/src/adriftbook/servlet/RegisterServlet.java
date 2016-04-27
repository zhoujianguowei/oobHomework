package adriftbook.servlet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;

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
    private boolean isRequestParamLegal(HttpServletRequest req,
                                        HttpServletResponse resp)
    {
        HashSet<String> paramsSet = new HashSet<>();
        paramsSet.add("username");
        paramsSet.add("password");
        int matchCount = 0;
        int paramSize = paramsSet.size();
        Enumeration<String> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements())
        {
            String param = enumeration.nextElement();
            if (paramsSet.contains(param))
            {
                matchCount++;
                paramsSet.remove(param);
            }
        }
        if (matchCount != paramSize)
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
                jsonObject.put(Constant.INFO_KEY, "bad request params");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    resp.getOutputStream().write(jsonObject.toString()
                            .getBytes(Constant.DEFAULT_CODE));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        if (!isRequestParamLegal(req, resp))
            return;
        String username = CodeTransformUtil.getParameter(req, "username");
        String password = CodeTransformUtil.getParameter(req, "password");
        JSONObject resInfo = new JSONObject();
        if (username == null || password == null)
            return;
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
