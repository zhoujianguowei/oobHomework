package com.klicen.navigationbar;

import android.view.View;
import android.widget.ImageView;

/**
 * 实现了导航栏返回功能
 * <p/>
 * Created on 15-4-14.
 */
public abstract class BackStackFragmentEx extends AccessNavigationBarFragment {

    @Override
    protected final View onHomeCreate() {
        ImageView home = new ImageView(getActivity());
        home.setImageResource(R.mipmap.icon_back_black);
        return home;
    }

    @Override
    public final void onHomeClick() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            getActivity().finish();
        }
    }

}