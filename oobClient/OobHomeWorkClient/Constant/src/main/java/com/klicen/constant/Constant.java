package com.klicen.constant;
public class Constant
{

    /**
     * now is obsolute
     */
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

}
