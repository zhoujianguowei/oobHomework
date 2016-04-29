package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainFragment extends BackStackFragmentWithProgressDialog
{

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_post_main, container, false);
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
}
