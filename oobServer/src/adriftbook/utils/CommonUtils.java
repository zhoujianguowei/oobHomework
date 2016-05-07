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
        if (bytes == null)
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
     * 将文件名转换成指定进制字符串形式
     * @param filename
     * @return
     */
    public static String transformFileNameToSpecifyRadixByteString(String filename,
                                                                   int radix)
    {
        if (radix < Character.MIN_VALUE + 1 || radix > Character.MAX_VALUE - 2)
            return null;
        String radixStr = "";
        String byteSplit = Integer.toString(radix + 1, Character.MAX_RADIX);
        String negativeFlag = Integer.toString(radix, Character.MAX_RADIX);
        int index = filename.lastIndexOf(".");
        String extensionName = "";
        if (index > 0)
            extensionName = filename.substring(index);
        byte[] bytes = null;
        try
        {
            if (index > 0)
                bytes = filename.substring(0, index)
                        .getBytes(Constant.DEFAULT_CODE);
            else
                bytes = filename.substring(0).getBytes(Constant.DEFAULT_CODE);
            for (byte b : bytes)
            {
                if (b < 0)
                    radixStr += negativeFlag + Integer.toString(Math.abs(b), radix) +
                            byteSplit;
                else
                    radixStr += Integer.toString(Math.abs(b), radix) + byteSplit;
            }
            if (radixStr.endsWith(byteSplit))
                radixStr = radixStr.substring(0, radixStr.length() - 1);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        radixStr += extensionName;
        return radixStr;
    }
    public static String transformSpecifyRadixBytesStringToFileName(
            String radixBytesStr,
            int radix)
    {
        if (radix < Character.MIN_VALUE + 1 || radix > Character.MAX_VALUE - 2)
            return null;
        String byteSplit = Integer.toString(radix + 1, Character.MAX_RADIX);
        String negativeFlag = Integer.toString(radix, Character.MAX_RADIX);
        int index = radixBytesStr.lastIndexOf(".");
        String extensionName = "";
        if (index > 0)
        {
            extensionName = radixBytesStr.substring(index);
            radixBytesStr = radixBytesStr.substring(0, index);
        }
        String[] transformBytestStr = radixBytesStr.split(byteSplit);
        if (transformBytestStr.length < 1)
            return null;
        byte[] bytes = new byte[transformBytestStr.length];
        int i = 0;
        for (String byteStr : transformBytestStr)
        {
            if (byteStr.startsWith(negativeFlag))
            {
                byteStr = byteStr.substring(1);
                bytes[i++] = (byte) -Integer.valueOf(byteStr, radix);
            } else
                bytes[i++] = Integer.valueOf(byteStr, radix)
                        .byteValue();
        }
        try
        {
            return new String(bytes, Constant.DEFAULT_CODE) + extensionName;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
