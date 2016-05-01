package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
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
        PullRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener
{

    private CheckBox requestBookCb;
    private CheckBox sendBookCb;
    private CheckBox ebookCb;
    private EditText searchEt;
    private ImageView searchIv;
    PullRefreshLayout refreshLayout;
    ListView lv;
    User user;
    ArrayList<Post> postList;
    int postCount = 0;
    private PostMainAdapter postAdpater;
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
        refreshLayout = (PullRefreshLayout) view
                .findViewById(R.id.fragment_post_main_refresh_layout);

        refreshLayout.setOnRefreshListener(this);
        lv = (ListView) view.findViewById(R.id.fragment_post_main_refresh_lv);
        lv.setOnItemClickListener(this);
        Bundle bundle = getArguments();
        if (bundle.containsKey(Post.POSTS_COUNT_KEY))
        {
            postList = (ArrayList<Post>) bundle.getSerializable(Post.POSTS_KEY);
            updateLabelStatus(postList);
            postCount = bundle.getInt(Post.POSTS_COUNT_KEY);
            postAdpater = new PostMainAdapter(getActivity(), postList);
            lv.setAdapter(postAdpater);
        }
        return view;
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
        RelativeLayout.LayoutParams searchEtParam= (RelativeLayout.LayoutParams) searchEt.getLayoutParams();
        searchEtParam.width= (int) (ScreenSize.getScreenWidth()*1.0f/2);
        RelativeLayout.LayoutParams refreshLayoutParams= (RelativeLayout.LayoutParams) refreshLayout.getLayoutParams();
//        refreshLayoutParams.height=ScreenSize.getScreenHeight()*2/3;
    }
    @Override protected View onMenuCreate()
    {
        return super.onMenuCreate();
    }
    @Override public void onRefresh()
    {
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
    }
}
