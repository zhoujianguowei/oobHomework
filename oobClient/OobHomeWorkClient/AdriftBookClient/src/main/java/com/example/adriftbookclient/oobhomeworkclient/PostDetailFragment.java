package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import adapter.BookBaseAdapter;
import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.Post;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/5/1.
 */
public class PostDetailFragment extends BackStackFragmentWithProgressDialog
        implements
        Handler.Callback, AdapterView.OnItemClickListener
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
    ArrayList<Comment> commentList = new ArrayList<>();
    public static final String TAG="postdetailfragment";
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
        bookInfoLv.setOnItemClickListener(this);
        return view;
    }
    private void configureComments()
    {
        commentList.clear();
        for (AdriftBook book : bookList)
            commentList.addAll(book.getComments());
        commentsContainer.removeAllViews();
        for (int i = 0; i < commentList.size(); i++)
        {
            Comment comment = commentList.get(i);
            View commentItem = LayoutInflater.from(getActivity())
                    .inflate(R.layout.comment_item, null);
            TextView commentContent = (TextView) commentItem
                    .findViewById(R.id.comment_item_comment_content);
            TextView commentUserDate = (TextView) commentItem
                    .findViewById(R.id.comment_item_comment_user_date);
            commentContent.setText("\t" + comment.getCommentContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern("yy:MM:dd");
            commentUserDate
                    .setText(comment.getCommentUser().getUserName() + "\t\t" +
                            dateFormat.format(comment.getReviewDate().getTime()));
            commentsContainer.addView(commentItem);
            LinearLayout.LayoutParams commentParams = (LinearLayout.LayoutParams) commentItem
                    .getLayoutParams();
            commentParams.topMargin = (int) (ScreenSize.getScreenDensity() * 20);
            commentParams.leftMargin = commentParams.rightMargin = (int) (
                    ScreenSize.getScreenDensity() * 8);
        }
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
        configureComments();
    }
    @Override public void onResume()
    {
        super.onResume();
        if (getActivity() instanceof BookBaseAdapter.OnBookListDownloadButtonClickListener)
            bookAdapter.setOnBookListDownloadButtonClickListener(
                    (BookBaseAdapter.OnBookListDownloadButtonClickListener) getActivity());
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
    @Override public boolean handleMessage(Message msg)
    {
        return true;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (getActivity() instanceof OnBookListItemClickListener)
        {
            AdriftBook book = (AdriftBook) bookAdapter.getItem(position);
            ((OnBookListItemClickListener) getActivity())
                    .onBookItemClick(book, bookAdapter.getSpecifyBitmap(
                            book.getBookImageUrl()));
        }
    }
    interface OnBookListItemClickListener
    {

        void onBookItemClick(AdriftBook book, Bitmap bm);
    }
}
