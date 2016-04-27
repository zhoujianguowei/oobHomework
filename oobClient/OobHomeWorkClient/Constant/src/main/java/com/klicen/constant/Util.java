package com.klicen.constant;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Util {

    /**
     * concat all arrays one by one.
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{(byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF)};
    }

    public static byte[] long2Byte(long x) {
        byte[] bb = new byte[8];
        bb[0] = (byte) (x >> 56);
        bb[1] = (byte) (x >> 48);
        bb[2] = (byte) (x >> 40);
        bb[3] = (byte) (x >> 32);
        bb[4] = (byte) (x >> 24);
        bb[5] = (byte) (x >> 16);
        bb[6] = (byte) (x >> 8);
        bb[7] = (byte) (x >> 0);
        return bb;
    }

    /**
     * zlib decompress
     */
    public static byte[] decompress(byte[] data) {
        Log.i("info", "data.length = " + data.length);
        byte[] output = new byte[0];

        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[1024];
            Log.i("info", "start inflate");
            while (!decompresser.finished()) {
                int i = decompresser.inflate(buf);
                o.write(buf, 0, i);
            }
            output = o.toByteArray();
            Log.i("info", "end inflate,output.length = " + output.length);
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        decompresser.end();
        return output;
    }

    /**
     * zlib compress
     */
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());
    private static final Calendar cal = Calendar.getInstance();

    /**
     * format a date by "yyyy-MM-dd hh:mm:ss" format.
     */
    public static String formatDate(long milli) {
        if (milli == 0) {
            return "";
        }
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(milli));
    }

    /**
     * format a date by the given format.
     */
    public static String formatDate(long milli, String format) {
        sdf.applyPattern(format);
        return sdf.format(new Date(milli));
    }

    /**
     * get a UTC time in milliseconds
     *
     * @param date the special date string,the format is "yyyy-MM-dd HH:mm:ss"
     */
    public static long getTimeMillis(String date) {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
    }

    public static long getTimeMillis(String date, long defaultReture) {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return defaultReture;
        }
        return cal.getTimeInMillis();
    }

    public static long getTimeMillis(String date, String format) {
        try {
            sdf.applyPattern(format);
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.getTimeInMillis();
    }

    public static int getIntervalDays(String date1, String date2) {
        try {
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");

            cal.setTime(sdf.parse(date1));
            int a = cal.get(Calendar.DAY_OF_MONTH);

            cal.setTime(sdf.parse(date2));
            int b = cal.get(Calendar.DAY_OF_MONTH);

            return Math.abs(a - b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIntervalDays(long date1, long date2) {
        cal.setTimeInMillis(date1);
        int a = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTimeInMillis(date2);
        int b = cal.get(Calendar.DAY_OF_MONTH);

        return Math.abs(a - b);
    }

    /**
     * copy file.
     *
     * @return true if success, false otherwise.
     */
    public static boolean copyFile(String src, String dst) {
        boolean isok = true;
        try {
            int byteread = 0;
            File oldfile = new File(src);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(src);
                FileOutputStream fs = new FileOutputStream(dst);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                fs.flush();
                fs.close();
                inStream.close();
            } else {
                isok = false;
            }
        } catch (Exception e) {
            isok = false;
        }
        if (!isok) {
            File file = new File(dst);
            if (file.exists()) {
                file.delete();
            }
        }
        return isok;
    }

}
