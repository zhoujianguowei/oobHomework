package adapter;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.adriftbookclient.oobhomeworkclient.BitmapCache;
import com.example.adriftbookclient.oobhomeworkclient.PostDetailFragment;
import com.example.adriftbookclient.oobhomeworkclient.R;
import com.klicen.constant.Constant;

import java.util.ArrayList;

import adriftbook.entity.AdriftBook;
/**
 * Created by Administrator on 2016/5/2.
 */
public class BookBaseAdapter extends BaseAdapter implements View.OnClickListener
{

    private Context context;
    private ArrayList<AdriftBook> bookList;
    private RequestQueue requestQueue;
    private BitmapCache bitmapCache;
    private OnBookItemClickListener onBookItemClickListener;
    public void setOnBookItemClickListener(
            OnBookItemClickListener onBookItemClickListener)
    {
        this.onBookItemClickListener = onBookItemClickListener;
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
    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        convertView = LayoutInflater.from(context)
                .inflate(R.layout.post_detail_book_item, parent, false);
        ImageView bookIv = (ImageView) convertView
                .findViewById(R.id.post_detail_book_item_iv);
        TextView bookId = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_id);
        TextView bookName = (TextView) convertView
                .findViewById(R.id.post_detail_book_item_book_name);
        TextView author=(TextView)convertView.findViewById(R.id.post_detail_book_item_book_author);
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
                            PostDetailFragment.PER_BOOK_ITEM_IMAGE_WIDTH,
                            PostDetailFragment.PER_BOOK_ITEM_IMAGE_HEIGHT);
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
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                PostDetailFragment.PER_BOOK_ITEM_HEIGHT);
        convertView.setLayoutParams(params);
        return convertView;
    }
    @Override public void onClick(View v)
    {
        if (onBookItemClickListener != null)
            onBookItemClickListener.onClick(v);
    }
    public interface OnBookItemClickListener
    {

        void onClick(View v);
    }
}
