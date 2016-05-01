package com.example.adriftbookclient.oobhomeworkclient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
            postAdpater.notifyDataSetChanged();
        }
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
        if(searchEt.hasFocus())
            searchEt.clearFocus();
        if (!popupWindow.isShowing())
            popupWindow.showAsDropDown(settingIv);
    }
    @Override public void onRefresh()
    {
        Handler loadPostsHandler = new Handler(this);
        int requestType = requestBookCb.isChecked() ? 1 : 0;
        int sendBookType = sendBookCb.isChecked() ? 1 : 0;
        int ebookType = ebookCb.isChecked() ? 1 : 0;
        if (requestType == 0 && sendBookType == 0 && ebookType == 0)
        {
            postList.clear();
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
        if(searchEt.hasFocus())
            searchEt.clearFocus();
        showProgressDialog("数据更新中");
        onRefresh();
    }
    @Override public void onClick(View v)
    {
        if(searchEt.hasFocus())
            searchEt.clearFocus();
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
