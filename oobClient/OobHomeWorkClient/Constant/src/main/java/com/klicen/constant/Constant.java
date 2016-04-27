package com.klicen.constant;
public class Constant
{

    /**
     * now is obsolute
     */
    public final static String FILE_CHOOSER_LISTENER_KEY = "file_chooser_listener_key";//用于设置选择文件时候的监听key
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

}
