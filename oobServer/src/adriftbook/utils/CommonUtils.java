package adriftbook.utils;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import javafx.beans.binding.StringBinding;
/**
 * Created by Administrator on 2016/4/30.
 */
public class CommonUtils
{

    public static Map<String, String> getVolleyPostRequestParams(
            HttpServletRequest req)
    {
        String postContent = getVolleyRequestString(req);
        System.out.println("comment:"+postContent);
        HashMap<String, String> requestParams = new HashMap<>();
        String[] nameValuePairs = postContent.split("&");
        for (int i = 0; i < nameValuePairs.length; i++)
        {
            if (nameValuePairs[i] == null || nameValuePairs[i].trim().equals(""))
                continue;
            String[] namValue = nameValuePairs[i].split("=");
            requestParams.put(namValue[0], namValue[1]);
        }
        return requestParams;
    }
    public static byte[] getRequestPostData(HttpServletRequest req)
    {
        byte[] postData = null;
        RequestFacade facade = null;
        if (req instanceof RequestFacade)
        {
            facade = (RequestFacade) req;
            try
            {
                Field requestField = facade.getClass().getDeclaredField("request");
                requestField.setAccessible(true);
                Request requestObj = (Request) requestField.get(facade);
                Field postDataField = requestObj.getClass()
                        .getDeclaredField("postData");
                postDataField.setAccessible(true);
                postData = (byte[]) postDataField.get(requestObj);
            }
            catch (IllegalAccessException | NoSuchFieldException e)
            {
                e.printStackTrace();
            }
        }
        return postData;
    }
    public static String getVolleyRequestString(HttpServletRequest req)
    {
        return getVolleyRequestBody(getRequestPostData(req));
    }
    public static String getVolleyRequestBody(byte[] postData)
    {
        String res = null;
        try
        {
            res = new String(postData, Constant.DEFAULT_CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * get post requestbody,and its content is string type
     * @param inputStream
     * @return
     */
    public static String getDatas(ServletInputStream inputStream)
    {
        // TODO Auto-generated method stub
        StringBuilder builder = new StringBuilder();
        String readLine = null;
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(inputStream,
                    Constant.DEFAULT_CODE));
            while ((readLine = reader.readLine()) != null)
                builder.append(readLine);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return builder.toString();
    }
    /**
     * 将文件名转换成8进制字符串形式
     * @param filename
     * @return
     */
    public static String transformFileToByte(String filename)
    {
        String resStr = "";
        int index = filename.lastIndexOf(".");
        String extensionName = filename.substring(index + 1);
        try
        {
            byte[] bytes = filename.substring(0, index)
                    .getBytes(Constant.DEFAULT_CODE);
            for (byte b : bytes)
            {
                resStr += Integer.toString(Math.abs(b), 8);
            }
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        resStr += "." + extensionName;
        return resStr;
    }
}
