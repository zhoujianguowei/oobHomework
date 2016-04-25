package adriftbook.utils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

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
    public static User getInfo(String userName)
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
}
