package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import adriftbook.entity.User;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainFragment extends BackStackFragmentWithProgressDialog implements
        PullRefreshLayout.OnRefreshListener
{

    private CheckBox requestBookCb;
    private CheckBox sendBookCb;
    private CheckBox ebookCb;
    private EditText searchEt;
    private ImageView searchIv;
    PullRefreshLayout refreshLayout;
    ListView lv;
    User user;
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
        lv = (ListView) view.findViewById(R.id.fragment_post_main_refresh_lv);
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
        user = (User) getArguments().get(User.TAG);

    }
    @Override protected View onMenuCreate()
    {
        return super.onMenuCreate();
    }
    @Override public void onRefresh()
    {
    }
}
