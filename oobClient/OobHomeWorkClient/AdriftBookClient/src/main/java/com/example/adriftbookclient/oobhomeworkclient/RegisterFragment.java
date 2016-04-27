package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import utils.ScreenSize;
/**
 * Created by Administrator on 2016/4/27.
 */
public class RegisterFragment extends BackStackFragmentWithProgressDialog
        implements View.OnClickListener, TextWatcher
{

    EditText userNameEt;
    EditText passwordEt;
    EditText verifyPasswordEt;
    Button registerBt;
    private RegisterFragmentOnClickListener onClickListener;

    interface RegisterFragmentOnClickListener
    {

        void onClick(View v);
    }
    void setRegisterFragmentOnClickListener(
            RegisterFragmentOnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        userNameEt = (EditText) view
                .findViewById(R.id.fragment_register_user_name_et);
        passwordEt = (EditText) view
                .findViewById(R.id.fragment_register_password_et);
        verifyPasswordEt = (EditText) view
                .findViewById(R.id.fragment_register_verify_password_ev);
        registerBt = (Button) view.findViewById(R.id.fragment_register_register_bt);
        userNameEt.addTextChangedListener(this);
        passwordEt.addTextChangedListener(this);
        verifyPasswordEt.addTextChangedListener(this);
        registerBt.setOnClickListener(this);
        registerBt.setEnabled(false);
//        return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }
    @Override public void onResume()
    {
        if (getActivity() instanceof RegisterFragmentOnClickListener)
            setRegisterFragmentOnClickListener(
                    (RegisterFragmentOnClickListener) getActivity());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) registerBt
                .getLayoutParams();
        params.width = (int) (ScreenSize.getScreenWidth() * 3 / 5);
        params.height = (int) (ScreenSize.getScreenHeight() / 12);
        super.onResume();
    }
    @Override protected View onTitleCreate()
    {
//        return super.onTitleCreate();
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("注册");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    @Override public void onClick(View v)
    {
        if (!passwordEt.getText().toString()
                .equals(verifyPasswordEt.getText().toString()))
        {
            Toast.makeText(getActivity(), "密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        if (onClickListener != null)
            onClickListener.onClick(v);
    }
    @Override public void onDestroy()
    {
        if (onClickListener != null)
            onClickListener = null;
        super.onDestroy();
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
        if (userNameEt.getText().length() >= 2 &&
                passwordEt.getText().length() >= 6 &&
                verifyPasswordEt.getText().length() >= 6)
            registerBt.setEnabled(true);
        else
            registerBt.setEnabled(false);
    }
}
