package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.klicen.constant.BitmapUtil;
/**
 * Created by Administrator on 2016/5/2.
 */
public class BitmapCache implements ImageLoader.ImageCache
{

    private LruCache<String, Bitmap> mCache;
    private static BitmapCache bitmapCache;
    public synchronized static BitmapCache getSingleInstance()
    {
        if (bitmapCache == null)
            bitmapCache = new BitmapCache();
        return bitmapCache;
    }
    public BitmapCache()
    {
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize)
        {
            @Override
            protected int sizeOf(String key, Bitmap bitmap)
            {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url)
    {
        return mCache.get(url);
    }
    @Override
    public void putBitmap(String url, Bitmap bitmap)
    {
        mCache.put(url, BitmapUtil
                .getThumbBitmap(bitmap, PostDetailFragment.PER_BOOK_ITEM_IMAGE_WIDTH,
                        PostDetailFragment.PER_BOOK_ITEM_IMAGE_HEIGHT));
    }
}