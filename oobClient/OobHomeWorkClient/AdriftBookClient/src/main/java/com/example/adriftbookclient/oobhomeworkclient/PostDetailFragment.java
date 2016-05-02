package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.util.ArrayList;

import adapter.BookBaseAdapter;
import adriftbook.entity.AdriftBook;
import adriftbook.entity.Post;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/5/1.
 */
public class PostDetailFragment extends BackStackFragmentWithProgressDialog
        implements
        View.OnClickListener, Handler.Callback
{

    TextView postContentTv;
    ArrayList<AdriftBook> bookList = new ArrayList<>();
    ListView bookInfoLv;
    BookBaseAdapter bookAdapter;
    LinearLayout commentsContainer;
    public static final int PER_BOOK_ITEM_HEIGHT = ScreenSize.getScreenHeight() / 5;
    public static final int PER_BOOK_ITEM_IMAGE_WIDTH =
            ScreenSize.getScreenWidth() / 4;
    public static final int PER_BOOK_ITEM_IMAGE_HEIGHT =
            ScreenSize.getScreenHeight() / 6;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_post_detail, null);
        postContentTv = (TextView) view
                .findViewById(R.id.fragment_post_detail_post_content);
        bookInfoLv = (ListView) view.findViewById(R.id.fragment_post_detail_book_lv);
        commentsContainer = (LinearLayout) view
                .findViewById(R.id.fragment_post_detail_comments_lr);
        Bundle bundle = getArguments();
        postContentTv.setText("\t" + bundle.getString(Post.POST_CONTENT));
        bookAdapter = new BookBaseAdapter(getActivity(), bookList);
        configureBookList(bundle);
        bookInfoLv.setAdapter(bookAdapter);
        return view;
    }
    private void configureBookList(final Bundle bundle)
    {
        bookList.clear();
        bookList.addAll(
                (ArrayList<AdriftBook>) bundle
                        .getSerializable(AdriftBook.BOOKS_KEY));
        RelativeLayout.LayoutParams bookInfoLvParams = (RelativeLayout.LayoutParams) bookInfoLv
                .getLayoutParams();
        bookInfoLvParams.height = bookList.size() >= 4 ?
                3 * PER_BOOK_ITEM_HEIGHT : bookList.size() * PER_BOOK_ITEM_HEIGHT;
        bookInfoLv.setLayoutParams(bookInfoLvParams);
        bookAdapter.notifyDataSetChanged();
    }
    @Override public void onResume()
    {
        super.onResume();
    }
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("帖子详情");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    @Override public void onClick(View v)
    {
    }
    @Override public boolean handleMessage(Message msg)
    {
        return true;
    }
}
