package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.klicen.constant.Constant;
import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.util.HashMap;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import utils.MyStringRequest;
import utils.ScreenSize;
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
    Button ebookUrl;
    RatingBar ratingBar;
    TextView commentContent;
    Button deliverComment;
    AdriftBook book;
    public static final String TAG = "commentadriftbookfragment";
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
        bookId.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.middle));
        bookIv = (ImageView) view.findViewById(R.id.post_detail_book_item_iv);
        bookName = (TextView) view
                .findViewById(R.id.post_detail_book_item_book_name);
        bookName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.middle));
        author = (TextView) view
                .findViewById(R.id.post_detail_book_item_book_author);
        author.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.middle));
        ebookUrl = (Button) view.findViewById(R.id.post_detail_book_item_ebookurl);
//        if (book.getType() == AdriftBook.ENTITYBOOK)
        ebookUrl.setVisibility(View.GONE);
     /*   else
        {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) ebookUrl
                    .getLayoutParams();
            param.height = ScreenSize.getScreenHeight() / 11;
            ebookUrl.setLayoutParams(param);
            ebookUrl.setOnClickListener(this);
        }*/
        ratingBar = (RatingBar) view.findViewById(
                R.id.fragment_comment_adriftbook_ratingbar);
        ratingBar.setMax(100);
        commentContent = (TextView) view
                .findViewById(R.id.fragment_comment_adrifbook_comment_content);
        RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) commentContent
                .getLayoutParams();
        contentParams.height = ScreenSize.getScreenHeight() / 4;
        deliverComment = (Button) view
                .findViewById(R.id.fragment_comment_adriftbook_deliver_comment);
        commentContent.addTextChangedListener(this);
        deliverComment.setEnabled(false);
        deliverComment.setOnClickListener(this);
        bookId.setText(book.getBookId() + "");
        if (bundle.containsKey("bitmap"))
            bookIv.setImageBitmap(
                    (Bitmap) bundle.getParcelable("bitmap"));
        bookName.setText(book.getBookName());
        author.setText(book.getAuthor());
        ratingBar.setRating(5);
        return view;
    }
    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_comment_adriftbook_deliver_comment:
                startDeliverComment();
                break;
        }
    }
    private void startDeliverComment()
    {
        String deliverCommentUrl = Constant.CONSTANT_IP + "/comment?user_id=" +
                PostMainActivity.CURRENT_USER.getUserId() + "&book_id=" +
                book.getBookId();
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody
                .put(Comment.COMMENT_CONTENT, commentContent.getText().toString());
        requestBody.put(AdriftBook.RATING, ratingBar.getRating() + "");
        MyStringRequest deliverCommentRequest = new MyStringRequest(
                Request.Method.POST, deliverCommentUrl, requestBody,
                new Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        Log.e("comment", response);
                    }
                }, new Response.ErrorListener()
        {
            @Override public void onErrorResponse(VolleyError error)
            {
            }
        });
        RequestQueue commentQueqe = Volley.newRequestQueue(getActivity());
        commentQueqe.add(deliverCommentRequest);
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
        if (commentContent.getText().length() <= 6)
            deliverComment.setEnabled(false);
        else
            deliverComment.setEnabled(true);
    }
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        Toast.makeText(getActivity(), "rating:" + rating, Toast.LENGTH_SHORT).show();
    }
    interface CommentAdriftBookOnFinisheListener
    {

        void commentBookFinish();
    }
}
