package adapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.adriftbookclient.oobhomeworkclient.BitmapCache;
import com.example.adriftbookclient.oobhomeworkclient.R;
import com.klicen.constant.Constant;

import java.lang.reflect.Method;
import java.util.ArrayList;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.EBook;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/5/2.
 */
public class BookBaseAdapter extends BaseAdapter
{

    public static final int PER_BOOK_ITEM_IMAGE_WIDTH =
            ScreenSize.getScreenWidth() / 3;
    public static final int PER_BOOK_ITEM_IMAGE_HEIGHT = (int) (
            PER_BOOK_ITEM_IMAGE_WIDTH * 4.0 / 3);
    ;
    private Context context;
    private ArrayList<AdriftBook> bookList;
    private RequestQueue requestQueue;
    private BitmapCache bitmapCache;
    private OnBookListDownloadButtonClickListener onBookListDownloadButtonClickListener;
    public void setOnBookListDownloadButtonClickListener(
            OnBookListDownloadButtonClickListener onBookListDownloadButtonClickListener)
    {
        this.onBookListDownloadButtonClickListener = onBookListDownloadButtonClickListener;
    }
    public Bitmap getSpecifyBitmap(String url)
    {
        url = Constant.UPLOAD_BOOK_IMAGE_DIR + url;
        String cacheKey = url;
        try
        {
            Method method = ImageLoader.class
                    .getDeclaredMethod("getCacheKey", String.class, int.class,
                            int.class,
                            ImageView.ScaleType.class);
            method.setAccessible(true);
            cacheKey = (String) method.invoke(ImageLoader.class,
                    new Object[]{url, PER_BOOK_ITEM_IMAGE_WIDTH,
                            PER_BOOK_ITEM_IMAGE_HEIGHT,
                            ImageView.ScaleType.CENTER_INSIDE});
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmapCache.getBitmap(cacheKey);
    }
    public BookBaseAdapter(Context context, ArrayList<AdriftBook> bookList)
    {
        this.context = context;
        this.bookList = bookList;
        requestQueue = Volley.newRequestQueue(context);
        bitmapCache = BitmapCache.getSingleInstance();
    }
    @Override public int getCount()
    {
        return bookList.size();
    }
    @Override public Object getItem(int position)
    {
        return bookList.get(position);
    }
    @Override public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context)
                .inflate(R.layout.post_detail_book_item, parent, false);
        ImageView bookIv = (ImageView) convertView
                .findViewById(R.id.post_detail_book_item_iv);
        TextView bookId = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_id);
        TextView bookName = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_name);
        TextView author = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_author);
        TextView rating = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_rating);
        Button ebookUrl = (Button) convertView
                .findViewById(R.id.post_detail_book_item_ebookurl);
        AdriftBook currnetBook = (AdriftBook) getItem(position);
        if (!TextUtils.isEmpty(currnetBook.getBookImageUrl()))
        {
            String imageUrl = Constant.UPLOAD_BOOK_IMAGE_DIR +
                    bookList.get(position).getBookImageUrl();
            ImageLoader imageLoader = new ImageLoader(requestQueue,
                    bitmapCache);
            ImageLoader.ImageListener listener = ImageLoader
                    .getImageListener(bookIv, R.mipmap.default_req,
                            R.mipmap.wrong);
            imageLoader
                    .get(imageUrl, listener,
                            PER_BOOK_ITEM_IMAGE_WIDTH,
                            PER_BOOK_ITEM_IMAGE_HEIGHT);
        }
        bookId.setText(Html.fromHtml(
                "<font color=red>编号：<font>" + currnetBook.getBookId() + ""));
        bookName.setText(Html.fromHtml(
                "<font color=red>书名：<font>" + currnetBook.getBookName() + ""));
        author.setText(Html.fromHtml(
                "<font color=red>作者：<font>" + currnetBook.getAuthor() + ""));
        rating.setText(Html.fromHtml(
                "<font color=red>评分：<font>" + currnetBook.getRating() + ""));
        if (currnetBook.getType() == AdriftBook.ENTITYBOOK)
            ebookUrl.setVisibility(View.GONE);
      /*  else
        {
            ebookUrl.setText(((EBook) currnetBook).getEbookUrl());
        }*/
        ebookUrl.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick(View v)
            {
                if (onBookListDownloadButtonClickListener != null)
                    onBookListDownloadButtonClickListener
                            .onItemClick(
                                    Constant.UPLOAD_BOOK_IMAGE_DIR +
                                            ((EBook) bookList.get(position))
                                                    .getBookImageUrl());
            }
        });
      /*  AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                PostDetailFragment.PER_BOOK_ITEM_HEIGHT);
        convertView.setLayoutParams(params);*/
        return convertView;
    }
    public OnBookListDownloadButtonClickListener getOnBookListDownloadButtonClickListener()
    {
        return onBookListDownloadButtonClickListener;
    }
    public interface OnBookListDownloadButtonClickListener
    {

        void onItemClick(String ebookUrl);
    }
}
