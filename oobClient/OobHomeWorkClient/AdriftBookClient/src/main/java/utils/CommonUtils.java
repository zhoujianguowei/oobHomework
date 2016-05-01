package utils;
import com.klicen.constant.Constant;

import java.io.UnsupportedEncodingException;
/**
 * Created by Administrator on 2016/4/30.
 */
public class CommonUtils
{

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
