package com.klicen.constant;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
public class BitmapUtil
{

    /**
     * 获得指定大小比例的缩略图
     *
     * @param bm
     *            原图片位图对象
     * @return 缩略图
     */
    public static Bitmap getThumbBitmap(Bitmap bm, float width, float height)
    {
        // Log.i("info", "before compress byteCount = " + bm.getByteCount());
        if (bm != null)
        {
            Options opts = new Options();
            opts.inJustDecodeBounds = true;
            byte[] data = Bitmap2Bytes(bm);
            BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            int originWidth = opts.outWidth;
            int originHeight = opts.outHeight;
            float x = originWidth / width;
            float y = originHeight / height;
            if (Math.max(x, y) > 1)
            {
                opts.inSampleSize = (int) ((x + y) / 2);
                opts.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
            } else
            {
                Bitmap createBm = Bitmap
                        .createBitmap(bm, 0, 0, (int) originWidth, (int)originHeight);
                Canvas canvas = new Canvas(createBm);
                Matrix matrix = new Matrix();
                float scaleValue=Math.min(width/originWidth,height/originHeight);
                matrix.setScale(scaleValue,scaleValue);
                canvas.drawBitmap(bm, matrix, new Paint());
                bm = createBm;
            }
        }
        return bm;
    }
    public static Bitmap getThumbBitmap(String path, float width, float height)
    {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);
        float x = opts.outWidth / width;
        float y = opts.outHeight / height;
        opts.inSampleSize = (int) ((x + y) / 2);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, opts);
    }
    /**
     * 将位图转为字节数组(JEPG)
     *
     * @param bm
     *            位图对象
     * @return 图片字节数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        try
        {
            baos.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return b;
    }
    public static Bitmap Bytes2Bitamp(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else
        {
            return null;
        }
    }
    /**
     * 获得圆角图片(ARGB_8888)
     *
     * @param bitmap
     *            原位图对象
     * @param roundPx
     *            圆角半径
     * @return 圆角位图
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
    {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, w, h);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    /**
     * 位图质量压缩法，获得指定大小的图片，单位KB
     *
     * @param bm
     *            需要压缩的位图对象
     * @param size
     *            压缩后的大小(kb)
     * @return 压缩后的位图对象
     */
    public static Bitmap getThumbBitmapByQuality(Bitmap bm, int size)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        int options = 80;
        while (baos.toByteArray().length / 1024 > size)
        {
            // 循环判断如果压缩后图片是否大于size KB,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 8;// 每次都减少8
        }
        byte[] b = baos.toByteArray();
        ByteArrayInputStream isBm = new ByteArrayInputStream(
                b);// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory
                .decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    public static byte[] getBitmapCompressedBytesByQuality(Bitmap bm, int size)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        int options = 80;
        while (baos.toByteArray().length / 1024 > size)
        {
            // 循环判断如果压缩后图片是否大于size KB,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 8;// 每次都减少8
        }
        return baos.toByteArray();
    }
    /**
     * Drawable对象转Bitmap对象
     */
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                        .getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                        : Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    /**
     * Bitmap转化为drawable
     *
     * @param bitmap
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap)
    {
        return new BitmapDrawable(res, bitmap);
    }
    /**
     * Drawable 缩放
     */
    public static Drawable zoomDrawable(Resources res, Drawable drawable, float w,
                                        float h)
    {
        Bitmap oldbmp = drawableToBitmap(drawable);
        Bitmap newbmp = getThumbBitmap(oldbmp, w, h);
        return new BitmapDrawable(res, newbmp);
    }
}
