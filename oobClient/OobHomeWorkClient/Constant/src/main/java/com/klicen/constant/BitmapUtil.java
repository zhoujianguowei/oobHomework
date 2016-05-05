package com.klicen.constant;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
public class BitmapUtil
{

    /**
     * 默认Bitmap压缩文件大小
     */
    private static final int DEFAULT_COMPRESS_BITMAP_SIZE = 5* 1024 * 100;
    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
     * more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used more
     * @return a copy of imgIn, but muttable.
     */
    public static Bitmap convertToMutable(Bitmap imgIn)
    {
        try
        {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(
                    Environment.getExternalStorageDirectory() + File.separator +
                            "temp.tmp");
            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Config type = imgIn.getConfig();
            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel
                    .map(FileChannel.MapMode.READ_WRITE.READ_WRITE, 0,
                            imgIn.getRowBytes() * height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released
            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();
            // delete the temp file
            file.delete();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return imgIn;
    }
    /**
     * 获得指定大小比例的缩略图
     *只有createBitmap返回的Bitmap的时候产生的是mutable
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
            }/* else
            {
                if (!bm.isMutable())
                    bm = convertToMutable(bm);
                Bitmap createBm = Bitmap
                        .createBitmap(bm, 0, 0, (int) originWidth,
                                (int) originHeight);
                Canvas canvas = new Canvas(createBm);
                Matrix matrix = new Matrix();
                float scaleValue = Math
                        .min(width / originWidth, height / originHeight);
                matrix.setScale(scaleValue, scaleValue);
                canvas.drawBitmap(bm, matrix, new Paint());
                bm = createBm;
            }*/
        }
        return bm;
    }
    public static File saveAndCompressBitmap(String location, String fileName,
                                             Bitmap bm, int compressSize)
    {
        if (compressSize <= 0)
            compressSize = DEFAULT_COMPRESS_BITMAP_SIZE;
        File newFile = null;
        if (location != null && fileName != null)
            newFile = new File(location, fileName);
        else
            newFile = new File(
                    Environment.getExternalStorageDirectory() + File.separator +
                            "#new#.png");
        try
        {
            newFile.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(newFile);
            byte[] bmBytes = getBitmapCompressedBytesByQuality(bm, compressSize);
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.write(bmBytes);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return newFile;
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
        int options = 83;
        while (baos.toByteArray().length > size)
        {
            // 循环判断如果压缩后图片是否大于size KB,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options - 8 > 0)
                options -= 8;// 每次都减少8
            else
                break;
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
        int options = 83;
        while (baos.toByteArray().length > size)
        {
            // 循环判断如果压缩后图片是否大于size KB,大于继续压缩
            baos.reset();// 重置baos即清空baos
            bm.compress(Bitmap.CompressFormat.JPEG, options,
                    baos);// 这里压缩options%，把压缩后的数据存放到baos中
            if (options - 10 < 1)
                break;
            if (options < 21)
                options -= 10;
            else
                options -= 20;// 每次都减少8
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
