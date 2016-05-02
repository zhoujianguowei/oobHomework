package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import adriftbook.entity.AdriftBook;
/**
 * Created by Administrator on 2016/5/2.
 */
public class CommentAdriftBookFragment extends BackStackFragmentWithProgressDialog
        implements
        View.OnClickListener, TextWatcher, RatingBar.OnRatingBarChangeListener
{

    ImageView bookIv;
    TextView bookId;
    TextView bookName;
    TextView author;
    RatingBar ratingBar;
    TextView commentContent;
    Button deliverComment;
    AdriftBook book;
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("评论");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return null;
        View view = inflater.from(getActivity())
                .inflate(R.layout.fragment_comment_adriftbook, container, false);
        Bundle bundle = getArguments();
        book = (AdriftBook) bundle.getSerializable("book");
        bookId = (TextView) view.findViewById(R.id.post_detail_book_item_book_id);
        bookIv = (ImageView) view.findViewById(R.id.post_detail_book_item_iv);
        bookName = (TextView) view
                .findViewById(R.id.post_detail_book_item_book_name);
        author = (TextView) view
                .findViewById(R.id.post_detail_book_item_book_author);
        ratingBar = (RatingBar) view.findViewById(
                R.id.fragment_comment_adriftbook_ratingbar);
        ratingBar.setMax(100);
        commentContent = (TextView) view
                .findViewById(R.id.fragment_comment_adrifbook_comment_content);
        deliverComment = (Button) view
                .findViewById(R.id.fragment_comment_adriftbook_deliver_comment);
        deliverComment.setEnabled(false);
        deliverComment.setOnClickListener(this);
        ratingBar.setOnClickListener(this);
        bookId.setText(book.getBookId() + "");
        if (bundle.containsKey("bitmap"))
            bookIv.setImageBitmap(
                    (Bitmap) bundle.getParcelable("bitmap"));
        bookName.setText(book.getBookName());
        author.setText(book.getAuthor());
        return view;
    }
    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_comment_adriftbook_deliver_comment:
                Toast.makeText(getActivity(), "发表评论", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
    }
    @Override public void afterTextChanged(Editable s)
    {
        if (s.length() <= 6)
            deliverComment.setEnabled(false);
        else
            deliverComment.setEnabled(true);
    }
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
    }
}
