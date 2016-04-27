package com.klicen.constant;

import java.security.MessageDigest;

public class EncryptionUtil {

    /**
     * MD5加密，32位
     */
    public static String MD5(String str) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] byteArray = str.getBytes("utf-8");
            byte[] md5Bytes = md5.digest(byteArray);

            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
            // return hexValue.substring(8, 24).toString(); //16位加密，从第9位到25位
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
