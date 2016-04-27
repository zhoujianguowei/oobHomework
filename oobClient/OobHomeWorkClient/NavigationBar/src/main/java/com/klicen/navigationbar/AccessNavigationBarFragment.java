package com.klicen.navigationbar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

/**
 * 子类可以直接访问导航栏。
 * <p/>
 * Created on 15-4-2.
 */
public abstract class AccessNavigationBarFragment extends Fragment
        implements NavigationBarFragment.Callback {
    private NavigationBarFragment navigationBarFragment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        navigationBarFragment =
                (NavigationBarFragment) getFragmentManager()
                        .findFragmentByTag(NavigationBarFragment.TAG);
        if (navigationBarFragment == null) {
            throw new IllegalStateException(activity.toString() +
                    " should provide the NavigationBarFragment.TAG tag" +
                    " when it add NavigationBarFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        navigationBarFragment.setmListener(this);
        navigationBarFragment.addTitleView(onTitleCreate());
        navigationBarFragment.addHomeView(onHomeCreate());

        navigationBarFragment.addMenuView(onMenuCreate());
        if (onMenusCreate() != null) {
            navigationBarFragment.addMenuView(onMenusCreate());
        }
    }


    /**
     * 向导航栏添加右部操作菜单。与onMenusCreate不可同时使用。
     *
     * @return 菜单
     */
    protected View onMenuCreate() {
        return null;
    }

    /**
     * 向导航栏添加多个右部操作菜单。与onMenuCreate不可同时使用。
     *
     * @return 菜单
     */
    protected View[] onMenusCreate() {
        return null;
    }

    /**
     * 向导航栏添加左部HOME按钮。
     *
     * @return HOME按钮
     */
    protected View onHomeCreate() {
        return null;
    }

    /**
     * 向导航栏添加中部标题。
     *
     * @return 标题
     */
    protected View onTitleCreate() {
        return null;
    }

    @Override
    public void onHomeClick() {

    }

    @Override
    public void onTitleClick() {

    }

    @Override
    public void onMenuClick(View view) {
        Log.i("info", "onMenuClick");
    }
}
