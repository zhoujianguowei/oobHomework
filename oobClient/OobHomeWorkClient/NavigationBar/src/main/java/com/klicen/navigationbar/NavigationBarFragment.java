package com.klicen.navigationbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class NavigationBarFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = NavigationBarFragment.class.getName();

    private FrameLayout home;
    private LinearLayout title, menu;

    private Callback mListener;

    public void setmListener(Callback mListener) {
        this.mListener = mListener;
    }

    public static NavigationBarFragment newInstance() {
        NavigationBarFragment fragment = new NavigationBarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NavigationBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_bar, container, false);

        title = (LinearLayout) view.findViewById(R.id.fragment_navigation_bar_title);
        home = (FrameLayout) view.findViewById(R.id.fragment_navigation_bar_home);
        menu = (LinearLayout) view.findViewById(R.id.fragment_navigation_bar_menu);

        title.setOnClickListener(this);
        home.setOnClickListener(this);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.fragment_navigation_bar_home) {
            if (mListener == null) {
                return;
            }
            mListener.onHomeClick();
        } else if (i == R.id.fragment_navigation_bar_title) {
            if (mListener == null) {
                return;
            }
            mListener.onTitleClick();
        } else {
            if (v.getTag().toString().equals("menu")) {
                mListener.onMenuClick(v);
            }
        }
    }

    public void addTitleView(View view) {
        title.removeAllViews();
        if (view == null) {
            return;
        }
        title.addView(view);
    }

    public void addHomeView(View view) {
        home.removeAllViews();
        if (view == null) {
            return;
        }
        home.addView(view);
    }

    public void addMenuView(View view) {
        menu.removeAllViews();
        if (view == null) {
            return;
        }
        view.setOnClickListener(this);
        view.setTag("menu");
        menu.addView(view,
                getResources().getDimensionPixelSize(R.dimen.navigationbar_bar_height),
                getResources().getDimensionPixelSize(R.dimen.navigationbar_bar_height));
    }

    public void addMenuView(View[] views) {
        menu.removeAllViews();
        if (views == null) {
            return;
        }
        for (View view : views) {
            view.setOnClickListener(this);
            view.setTag("menu");
            menu.addView(view,
                    getResources().getDimensionPixelSize(R.dimen.navigationbar_bar_height),
                    getResources().getDimensionPixelSize(R.dimen.navigationbar_bar_height));
        }
    }

    protected interface Callback {
        public void onHomeClick();

        public void onTitleClick();

        public void onMenuClick(View view);
    }

}
