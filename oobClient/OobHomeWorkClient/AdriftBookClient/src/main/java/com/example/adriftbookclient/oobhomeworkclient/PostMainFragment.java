package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.klicen.constant.Constant;
import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.util.ArrayList;

import adapter.PostMainAdapter;
import adriftbook.entity.Post;
import adriftbook.entity.User;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainFragment extends BackStackFragmentWithProgressDialog implements
        PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        android.os.Handler.Callback
{

    private CheckBox requestBookCb;
    private CheckBox sendBookCb;
    private CheckBox ebookCb;
    private EditText searchEt;
    ImageView settingIv;
    private ImageView searchIv;
    PullRefreshLayout refreshLayout;
    ListView lv;
    User user;
    ArrayList<Post> postList = new ArrayList<>();
    int postCount = 0;
    private PostMainAdapter postAdpater;
    PopupWindow popupWindow;
    private int currentPage = 1;
    LinearLayout pageContainer;
    private void updateLabelStatus(ArrayList<Post> postList)
    {
        if (postList.isEmpty())
            return;
        int maxReadCount = postList.get(0).getReadCount();
        for (Post post : postList)
            if (post.getReadCount() > maxReadCount)
                maxReadCount = post.getReadCount();
        for (Post post : postList)
            if (post.getReadCount() == maxReadCount)
                post.setLabelStatus(Post.HOST_POST_LABEL_STATUS);
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_post_main, container, false);
        requestBookCb = (CheckBox) view
                .findViewById(R.id.fragment_post_main_requestbook_cb);
        sendBookCb = (CheckBox) view
                .findViewById(R.id.fragment_post_main_sendbook_cb);
        ebookCb = (CheckBox) view.findViewById(R.id.fragment_post_main_ebook_cb);
        searchEt = (EditText) view.findViewById(R.id.fragment_post_main_search_et);
        searchIv = (ImageView) view.findViewById(R.id.fragment_post_main_search_iv);
        requestBookCb.setOnCheckedChangeListener(this);
        sendBookCb.setOnCheckedChangeListener(this);
        ebookCb.setOnCheckedChangeListener(this);
        refreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fragment_post_main_refresh_layout);
        pageContainer = (LinearLayout) view
                .findViewById(R.id.fragment_post_main_page_lr);
        refreshLayout.setOnRefreshListener(this);
        lv = (ListView) view.findViewById(R.id.fragment_post_main_refresh_lv);
        lv.setOnItemClickListener(this);
        searchIv.setOnClickListener(this);
        postAdpater = new PostMainAdapter(getActivity(), postList);
        lv.setAdapter(postAdpater);
        configureDataSet(getArguments());
        return view;
    }
    private void configureDataSet(Bundle bundle)
    {
        postList.clear();
        if (bundle.containsKey(Post.POSTS_COUNT_KEY))
        {
            postList.addAll(
                    (ArrayList<Post>) bundle.getSerializable(Post.POSTS_KEY));
            updateLabelStatus(postList);
            postCount = bundle.getInt(Post.POSTS_COUNT_KEY);
            configurePageContainer(postCount, currentPage);
            postAdpater.notifyDataSetChanged();
        }
    }
    private void configurePageContainer(int postCount, int currentPage)
    {
        pageContainer.removeAllViews();
        int totalPage = (int) Math.ceil(postCount / Constant.PER_REQUEST_ITEMS);
        TextView pageTv = null;
        int pageIndex = 1;
        if (totalPage <= 10)
        {
            while (pageIndex <= totalPage)
            {
                pageTv = (TextView) LayoutInflater.from(getActivity()).inflate(
                        R.layout.fragment_post_main_page_textview, null);
                pageTv.setText(pageIndex + "");
                pageContainer.addView(pageTv);
                pageIndex++;
            }
        } else
        {
            if (currentPage >= 6)
            {
                pageTv = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.fragment_post_main_page_textview,
                                null);
                pageTv.setText("1...");
                pageContainer.addView(pageTv);
                if (totalPage - currentPage >= 4)
                {
                    pageIndex = 1;
                    while (pageIndex <= 8)
                    {
                        pageTv = (TextView) LayoutInflater.from(getActivity())
                                .inflate(R.layout.fragment_post_main_page_textview,
                                        null);
                        pageTv.setText((currentPage - 5 + pageIndex) + "");
                        pageContainer.addView(pageTv);
                        pageIndex++;
                    }
                    pageTv = (TextView) LayoutInflater.from(getActivity())
                            .inflate(R.layout.fragment_post_main_page_textview,
                                    null);
                    pageTv.setText("..." + totalPage);
                    pageContainer.addView(pageTv);
                } else
                {
                    pageIndex = 1;
                    while (pageIndex <= 9)
                    {
                        pageTv = (TextView) LayoutInflater.from(getActivity())
                                .inflate(R.layout.fragment_post_main_page_textview,
                                        null);
                        pageTv.setText((totalPage - 9 + pageIndex) + "");
                        pageContainer.addView(pageTv);
                        pageIndex++;
                    }
                  /*  pageTv = (TextView) LayoutInflater.from(getActivity())
                            .inflate(R.layout.fragment_post_main_page_textview,
                                    null);
                    pageTv.setText("..." + totalPage);
                    pageContainer.addView(pageTv);*/
                }
            } else
            {
                if (totalPage - currentPage >= 4)
                {
                    pageIndex = 1;
                    while (pageIndex <= 9)
                    {
                        pageTv = (TextView) LayoutInflater.from(getActivity())
                                .inflate(R.layout.fragment_post_main_page_textview,
                                        null);
                        pageTv.setText(pageIndex + "");
                        pageContainer.addView(pageTv);
                        pageIndex++;
                    }
                    pageTv = (TextView) LayoutInflater.from(getActivity())
                            .inflate(R.layout.fragment_post_main_page_textview,
                                    null);
                    pageTv.setText("..." + totalPage);
                    pageContainer.addView(pageTv);
                }
            }
        }
        pageIndex = 1;
        int childCount = pageContainer.getChildCount();
        for (; pageIndex <= childCount; pageIndex++)
        {
            TextView child = (TextView) pageContainer.getChildAt(pageIndex - 1);
            child.setOnClickListener(this);
            TextPaint textPaint = child.getPaint();
            if (getPageNum(child) == currentPage)
            {
                textPaint.setFakeBoldText(true);
                child.setTextColor(Color.RED);
            } else
            {
                textPaint.setFakeBoldText(false);
                textPaint.setColor(getResources().getColor(R.color.shallow_blue));
            }
        }
        TextView totalCountPageTv = new TextView(getActivity());
        totalCountPageTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimensionPixelSize(R.dimen.large));
        totalCountPageTv.setTextColor(Color.BLACK);
        totalCountPageTv.setText("共" + totalPage + "页");
        pageContainer.addView(totalCountPageTv);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) totalCountPageTv
                .getLayoutParams();
        params.leftMargin = ScreenSize.getScreenWidth() / 12;
        //填充空间使用，不然HorizontalScrollView无法顺利滚动
        LinearLayout.LayoutParams blankParams=new LinearLayout.LayoutParams(ScreenSize.getScreenWidth()/8,params.height);
        TextView blankTv=new TextView(getActivity());
        pageContainer.addView(blankTv);
        blankTv.setLayoutParams(blankParams);
//        totalCountPageTv.setLayoutParams(params);
    }
    private int getPageNum(TextView tv)
    {
        String s = tv.getText().toString();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) >= '0' && s.charAt(i) <= '9')
                builder.append(s.charAt(i));
        return Integer.parseInt(builder.toString());
    }
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("主界面");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    @Override public void onResume()
    {
        super.onResume();
        RelativeLayout.LayoutParams searchEtParam = (RelativeLayout.LayoutParams) searchEt
                .getLayoutParams();
        searchEtParam.width = (int) (ScreenSize.getScreenWidth() * 1.0f / 2);
        RelativeLayout.LayoutParams refreshLayoutParams = (RelativeLayout.LayoutParams) refreshLayout
                .getLayoutParams();
//        refreshLayoutParams.height=ScreenSize.getScreenHeight()*2/3;
    }
    @Override protected View onMenuCreate()
    {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_post_main_pop_setting, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(view, view.getMeasuredWidth(),
                view.getMeasuredHeight());
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.anim.fab_in);
        //to enforece popupWindow dismiss outside of popupWindow, you have to set this
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        settingIv = new ImageView(getActivity());
        settingIv.setImageResource(R.mipmap.setting_icon);
        settingIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return settingIv;
    }
    @Override public void onMenuClick(View view)
    {
        if (searchEt.hasFocus())
            searchEt.clearFocus();
        if (!popupWindow.isShowing())
            popupWindow.showAsDropDown(settingIv);
    }
    @Override public void onRefresh()
    {
        postList.clear();
        Handler loadPostsHandler = new Handler(this);
        int requestType = requestBookCb.isChecked() ? 1 : 0;
        int sendBookType = sendBookCb.isChecked() ? 1 : 0;
        int ebookType = ebookCb.isChecked() ? 1 : 0;
        if (requestType == 0 && sendBookType == 0 && ebookType == 0)
        {
            postAdpater.notifyDataSetChanged();
            dismissProgressDialog();
            return;
        }
        String tag = searchEt.getText().toString();
        new LoadEntityUtils(getActivity(), loadPostsHandler)
                .loadPosts(requestType, sendBookType, ebookType, currentPage, tag);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (searchEt.hasFocus())
            searchEt.clearFocus();
        showProgressDialog("数据加载中");
        currentPage = 1;
        onRefresh();
    }
    @Override public void onClick(View v)
    {
        if (searchEt.hasFocus())
            searchEt.clearFocus();
        if (v.getParent() != null && v.getParent().equals(pageContainer))
        {
            currentPage = getPageNum((TextView) v);
            showProgressDialog("数据加载中");
            onRefresh();
        }
    }
    @Override public boolean handleMessage(Message msg)
    {
        Bundle bundle = (Bundle) msg.obj;
        if (bundle.getString(Constant.STATUS_KEY).equals(Constant.SUCCESS_VALUE))
            configureDataSet(bundle);
        else
            Toast.makeText(getActivity(), bundle.getString(Constant.INFO_KEY),
                    Toast.LENGTH_LONG).show();
        refreshLayout.setRefreshing(false);
        dismissProgressDialog();
        return true;
    }
}
