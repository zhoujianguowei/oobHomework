package adriftbook.utils;
import java.io.File;

import adriftbook.servlet.UploadPostServlet;
/**
 * Created by Administrator on 2016/4/24.
 */
public class Constant
{

    public static final String PROTO = "http";
    public static final String IP = "192.168.137.1";
    public static final String PORT = "8080";
    public static final String CONSTANT_IP =
            PROTO + "://" + IP + ":" + PORT + "/adriftbook/";
    public static final String STATUS_KEY = "status";
    public static final String SUCCESS_VALUE = "success";
    public static final String FAIL_VALUE = "fail";
    public static final String INFO_KEY = "info";
    public static final String SERVER_CODE = "ISO-8859-1";
    public static final String DEFAULT_CODE = "UTF-8";
    public static final int PER_REQUEST_ITEMS = 10;   //每次返回的帖子(post)个数
    public static final String HTTP_CONTENT_TYPE = "Content-Type";//设置请求header字段
    public static final String UPLOAD_BOOK_IMAGE_DIR =
            CONSTANT_IP + "upload/bookImage/";
    public static final String UPLOAD_EBOOK_FILE_DIR =
            CONSTANT_IP + "upload/ebookFile/";
    public static void main(String[] args) throws Exception
    {
//        System.out.println(MysqlCheckUtil.getUserInfo("周建国"));
//        String test = "nice3hao";
//        System.out.println(test);
//        test = "nice" + 3 + "hao";
//        System.out.println(test);
//        System.out.println(CommonUtils.transformSpecifyRadixBytesStringToFileName(
//                CommonUtils.transformFileNameToSpecifyRadixByteString("我好人?niceday够哦哦的飞机开始感受",
//                        34), 34));
//        System.out.println(Integer.toString(37,36));
//        SimpleDateFormat dateFormat=new SimpleDateFormat();
//        dateFormat.applyPattern("MM月d日H时mm分");
//        System.out.println(dateFormat.format(Calendar.getInstance().getTime()));
        System.out.println(CommonUtils.transformSpecifyRadixBytesStringToFileName(
                "yrzy39zy2kzyrzy21zy22zyrzy2xzy1xz1gz1ez1fz1kzyrzy23zy28z1jzyqzy2wzy3iz1lzyqzy33zy2nzwz1fz1izyrzy3izy3kz1fz1mzyrzy3izy3kz1jz1gzypzy2lzy38z2nz26z1vz2iz1vzyrzy23zy26zyrzy3bzy39zypzy20zy34zypzy2kzy3fzyrzy2ezy2uzyozy25zy27z2pz1czyqzy3izy3izyozy3ezy2kz1czyqzy3hzy2hzyqzy3bzy3bzypzy3hzy3iz16z21z20z1gz1ez1ez1ez1cz1xz2bz29z17.pdf",
                UploadPostServlet.TRANSFORM_RADIX));
//        File file = new File(
//                "d:/yrzy39zy2kzyrzy21zy22zyrzy2xzy1xz1gz1ez1fz1kzyrzy23zy28z1jzyqzy2wzy3iz1lzyqzy33zy2nzwz1fz1izyrzy3izy3kz1fz1mzyrzy3izy3kz1jz1gzypzy2lzy38z2nz26z1vz2iz1vzyrzy23zy26zyrzy3bzy39zypzy20zy34zypzy2kzy3fzyrzy2ezy2uzyozy25zy27z2pz1czyqzy3izy3izyozy3ezy2kz1czyqzy3hzy2hzyqzy3bzy3bzypzy3hzy3iz16z21z20z1gz1ez1ez1ez1cz1xz2bz29z17.pdf");
        File file=new File("d:/good.pdf");
        if (!file.exists())
            file.createNewFile();
    }
}
