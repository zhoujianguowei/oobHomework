package adriftbook.utils;
/**
 * Created by Administrator on 2016/4/24.
 */
public class Constant
{

    public static final String PROTO = "http";
    public static final String IP = "192.168.137.1";
    public static final String PORT = "8080";
    public static final String CONSTANT_IP = PROTO + "://" + IP + ":" + PORT + "/";
    public static final String STATUS_KEY = "status";
    public static final String SUCCESS_VALUE = "success";
    public static final String FAIL_VALUE = "fail";
    public static final String INFO_KEY = "info";
    public static final String SERVER_CODE = "ISO-8859-1";
    public static final String DEFAULT_CODE = "UTF-8";
    public static final int PER_REQUEST_ITEMS = 8;   //每次返回的帖子(post)个数
    public static void main(String[] args)
    {
//        System.out.println(MysqlCheckUtil.getUserInfo("周建国"));
        String test = "nice3hao";
        System.out.println(test);
        test = "nice" + 3 + "hao";
        System.out.println(test);
    }
}
