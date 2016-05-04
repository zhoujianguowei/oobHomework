package adriftbook.utils;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * Created by Administrator on 2016/4/30.
 */
public class CommonUtils
{

    public static Map<String, String> getVolleyPostRequestParams(
            HttpServletRequest req)
    {
        HashMap<String, String> requestParams = new HashMap<>();
        String postContent = null;
        String[] nameValuePairs = null;
        try
        {
            postContent = URLDecoder.decode(new String(getRequestPostData(req),
                    Constant.DEFAULT_CODE), Constant.DEFAULT_CODE);
            nameValuePairs = postContent.split("&");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < nameValuePairs.length; i++)
        {
            if (nameValuePairs[i] == null || nameValuePairs[i].trim().equals(""))
                continue;
            String[] nameValue = nameValuePairs[i].split("=");
            requestParams.put(nameValue[0], nameValue[1]);
        }
        return requestParams;
    }
    private static void getByteStr(byte[] bytes)
    {
        if(bytes==null)
            return;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length && bytes[i] != '\0'; i++)
            builder.append(bytes[i] + " ");
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
               /* postDataField.set(requestObj,null);
                Method readChunkedPostBody =requestObj.getClass().getDeclaredMethod("readChunkedPostBody",null);
                readChunkedPostBody.setAccessible(true);
                postData= (byte[]) readChunkedPostBody.invoke(requestObj,null);*/
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        getByteStr(postData);
        return postData;
    }
    /**
     * get post requestbody,and its content is string type
     * @param inputStream
     * @return
     */
    public static String getDatasFromServletInputStream(
            InputStream inputStream)
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
