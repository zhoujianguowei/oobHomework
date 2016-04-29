package utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import adriftbook.entity.EntityEnum;
import adriftbook.entity.User;
/**
 * Created by Administrator on 2016/4/29.
 */
public class JsonEntityParser
{

    private static JsonEntityParser parser;
    public static synchronized JsonEntityParser getSingleInstance()
    {
        if (parser == null)
            parser = new JsonEntityParser();
        return parser;
    }
    public Object parseJsonEntity(EntityEnum entityEnum,JSONObject jsonObject)
    {
        return parseJsonEntity(entityEnum,jsonObject.toString());
    }
    public Object parseJsonEntity(EntityEnum entityEnum, String jsonStr)
    {
        Object obj = null;
        try
        {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Calendar calendar = Calendar.getInstance();
            switch (entityEnum)
            {
                case AdriftBook:
                    break;
                case User:
                    User user = new User();
                    user.setPassword(jsonObject.getString(User.PASSWORD));
                    user.setUserId(jsonObject.getInt(User.USER_ID));
                    user.setUserName(jsonObject.getString(User.USER_NAME));
                    user.setUserLevel(jsonObject.getInt(User.USER_LEVEL));
                    calendar.setTimeInMillis(jsonObject.getLong(User.REGISTER_DATE));
                    user.setRegisterDate(calendar);
                    obj = user;
                    break;
                case Post:
                    break;
                case Comment:
                    break;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return obj;
    }
}
