package adriftbook.utils;
import java.util.Calendar;
/**
 * Created by Administrator on 2016/4/24.
 */
public class Constant
{

    public static final String PROTO = "http";
    public static final String IP = "192.168.137.1";
    public static final String PORT = "8080";
    public static final String CONSTANT_IP = PROTO + "://" + IP + ":" + PORT + "/";
    public static final String STATUS_KEY ="status";
    public static final String SUCCESS_VALUE ="success";
    public static  final String FAIL_VALUE ="fail";
    public static final String INFO_KEY ="info";
    public static final String SERVER_CODE="ISO-8859-1";
    public static final String DEFAULT_CODE="UTF-8";
    public static void main(String [] args)
    {

        System.out.println(MysqlCheckUtil.getInfo("周建国"));
    }


}
