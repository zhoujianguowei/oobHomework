package com.example.adriftbookclient.oobhomeworkclient;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import adriftbook.entity.User;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/4/27.
 */
public class LoginFragment extends BackStackFragmentWithProgressDialog
        implements View.OnClickListener, TextWatcher
{

    EditText userNameEt;
    EditText passwordEt;
    Button loginBt;
    Button registerBt;
    private CheckBox rememberCb;
    private LoginFragmentOnClickListener loginFragmentOnClickListener;
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String USER_INFO_RECORD = "user_info";
    public static final String TAG="loginfragment";
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        userNameEt = (EditText) view.findViewById(R.id.fragment_login_user_name_et);
        passwordEt = (EditText) view.findViewById(R.id.fragment_login_password_et);
        loginBt = (Button) view.findViewById(R.id.fragment_login_login_bt);
        registerBt = (Button) view.findViewById(R.id.fragment_login_register_bt);
        rememberCb = (CheckBox) view.findViewById(R.id.fragment_login_remember_cb);
        loginBt.setEnabled(false);
        loginBt.setOnClickListener(this);
        rememberCb.setOnClickListener(this);
        registerBt.setOnClickListener(this);
        userNameEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);
        return view;
    }
    void setLoginFragmentOnClickListener(
            LoginFragmentOnClickListener loginFragmentOnClickListener)
    {
        this.loginFragmentOnClickListener = loginFragmentOnClickListener;
    }
    @Override public void onResume()
    {
        super.onResume();
        if (getActivity() instanceof LoginFragmentOnClickListener)
            setLoginFragmentOnClickListener(
                    (LoginFragmentOnClickListener) getActivity());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) loginBt
                .getLayoutParams();
        params.width = (int) (ScreenSize.getScreenWidth() * 3 / 5);
        params.height = (int) (ScreenSize.getScreenHeight() / 12);
        SharedPreferences preferences = getActivity()
                .getSharedPreferences(USER_INFO_RECORD,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.contains(User.USER_NAME) && rememberCb.isChecked())
        {
            userNameEt.setText(preferences.getString(User.USER_NAME, ""));
            passwordEt.setText(preferences.getString(User.PASSWORD, ""));
        } else
        {
            editor.clear();
        }
        editor.commit();
    }
    @Override public void onDestroy()
    {
        if (loginFragmentOnClickListener != null)
            loginFragmentOnClickListener = null;
        super.onDestroy();
    }
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("登陆");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    public void storeUserInfoRecord()
    {
        SharedPreferences preferences = getActivity()
                .getSharedPreferences(USER_INFO_RECORD,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (!rememberCb.isChecked())
        {
            editor.clear();
            editor.apply();
            return;
        }
        editor.putString(User.USER_NAME, userNameEt.getText().toString());
        editor.putString(User.PASSWORD, passwordEt.getText().toString());
        editor.apply();
    }
    @Override public void onClick(View v)
    {
        if (loginFragmentOnClickListener != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(USER_NAME, userNameEt.getText().toString());
            bundle.putString(PASSWORD, passwordEt.getText().toString());
            loginFragmentOnClickListener.onClick(v, bundle);
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
        if (TextUtils.isEmpty(userNameEt.getText()) ||
                TextUtils.isEmpty(passwordEt.getText()))
            loginBt.setEnabled(false);
        else if (userNameEt.getText().length() < 2 ||
                passwordEt.getText().length() < 6)
            loginBt.setEnabled(false);
        else
            loginBt.setEnabled(true);
    }
    interface LoginFragmentOnClickListener
    {

        void onClick(View v, Bundle bundle);
    }
}
