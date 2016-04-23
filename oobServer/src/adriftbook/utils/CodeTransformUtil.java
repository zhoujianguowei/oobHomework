package adriftbook.utils;
import javax.servlet.http.HttpServletRequest;
/**
 * Created by Administrator on 2016/4/23.
 * 用于服务端和客户端之间的编码转换
 */
public class CodeTransformUtil
{

    private static String serverCode = "ISO-8859-1";
    private static String defaulCode = "UTF-8";
    public static void setServerCode(String serverCode)
    {
        CodeTransformUtil.serverCode = CodeTransformUtil.serverCode;
    }
    public static void setDefaulCode(String defaultCode)
    {
        CodeTransformUtil.defaulCode = defaultCode;
    }
    public static String getParameter(HttpServletRequest request,
                                      String parameter)
    {
        byte[] decodedBytes = null;
        String res = null;
        try
        {
            decodedBytes = request.getParameter(parameter)
                    .getBytes(serverCode);
            res = new String(decodedBytes, defaulCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            // TODO: handle exception
        }
        // return new String(decodedBytes, defaulCode);
        return res;
    }
}
