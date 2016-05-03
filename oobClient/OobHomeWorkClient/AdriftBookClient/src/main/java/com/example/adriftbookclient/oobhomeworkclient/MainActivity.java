package com.example.adriftbookclient.oobhomeworkclient;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.file.attach.FileChooserFragment;
import com.file.attach.Option;
import com.klicen.constant.Constant;
import com.klicen.navigationbar.NavigationBarFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import adriftbook.entity.EntityEnum;
import adriftbook.entity.User;
import utils.JsonEntityParser;
import utils.MyStringRequest;
import utils.ScreenSize;
public class MainActivity extends SupActivityHandleFragment
        implements FileChooserFragment.FileChooserFragmentListItemClickListener,
        LoginFragment.LoginFragmentOnClickListener,
        RegisterFragment.RegisterFragmentOnClickListener
{

    FileChooserFragment fileChooserFragment;
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    FragmentTransaction transaction;
    private String currentTag = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenSize.initial(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        loginFragment = new LoginFragment();
        transaction.add(R.id.activity_main_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_main_content_fr_container, loginFragment,
                LoginFragment.TAG);
//            transaction.addToBackStack(null);
        transaction.commit();
        setTapFragment(fragmentManager,LoginFragment.TAG);
    }
    @Override public void onListItemClickListener(Option option)
    {
        Toast.makeText(this, option.getName(), Toast.LENGTH_LONG).show();
    }
    @Override public void onClick(View v, Bundle bundle)
    {
        switch (v.getId())
        {
            case R.id.fragment_login_login_bt:
                startLoginRequet(bundle);
                break;
            case R.id.fragment_login_register_bt:
                transaction = fragmentManager.beginTransaction();
                if (registerFragment == null)
                    registerFragment = new RegisterFragment();
                transaction.add(
                        R.id.activity_main_navigation_bar_fr_container,
                        new NavigationBarFragment(), NavigationBarFragment.TAG);
                transaction.add(R.id.activity_main_content_fr_container,
                        registerFragment, RegisterFragment.TAG);
                transaction.hide(loginFragment);
                transaction.show(registerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                setTapFragment(fragmentManager,RegisterFragment.TAG);
                break;
            case R.id.fragment_register_register_bt:
                startRegisterRequest(bundle);
                break;
        }
    }
    private void startRegisterRequest(final Bundle bundle)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String registerUrl = null;
        try
        {
            registerUrl = Constant.CONSTANT_IP + "register?username=" +
                    URLEncoder.encode(bundle.getString(LoginFragment.USER_NAME),
                            Constant.DEFAULT_CODE) +
                    "&password=" + URLEncoder.encode(bundle
                    .getString(LoginFragment.PASSWORD), Constant.DEFAULT_CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        registerFragment.showProgressDialog("注册中...");
        MyStringRequest registerRequest = new MyStringRequest(registerUrl,
                new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        registerFragment.dismissProgressDialog();
                        JSONObject jsonObject = null;
                        Log.e("res", response);
                        try
                        {
                            jsonObject = new JSONObject(new String(
                                    response.getBytes(Constant.DEFAULT_CODE)));
                            String status = jsonObject
                                    .getString(Constant.STATUS_KEY);
                            if (status.equals(Constant.SUCCESS_VALUE))
                                Toast.makeText(MainActivity.this, "用户注册成功",
                                        Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(MainActivity.this,
                                        jsonObject.getString(Constant.INFO_KEY),
                                        Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException | UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override public void onErrorResponse(VolleyError error)
            {
                registerFragment.dismissProgressDialog();
                Toast.makeText(MainActivity.this, error.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        requestQueue.add(registerRequest);
    }
    private void startLoginRequet(Bundle bundle)
    {
        final RequestQueue loginQueue = Volley.newRequestQueue(this);
        String loginUrl = null;
        try
        {
            loginUrl = Constant.CONSTANT_IP + "login?username=" +
                    URLEncoder.encode(bundle.getString(LoginFragment.USER_NAME),
                            Constant.DEFAULT_CODE) +
                    "&password=" + URLEncoder.encode(bundle
                    .getString(LoginFragment.PASSWORD), Constant.DEFAULT_CODE);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        loginFragment.showProgressDialog("登陆中...");
        MyStringRequest loginRequest = new MyStringRequest(loginUrl,
                new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        JSONObject jsonObject = null;
                        try
                        {
                            jsonObject = new JSONObject(new String(
                                    response.getBytes(Constant.DEFAULT_CODE)));
                            String status = jsonObject
                                    .getString(Constant.STATUS_KEY);
                            if (status.equals(Constant.SUCCESS_VALUE))
                            {
                                loginFragment.storeUserInfoRecord();
                                Intent intent = new Intent(MainActivity.this,
                                        PostMainActivity.class);
                                User user = (User) JsonEntityParser
                                        .getSingleInstance()
                                        .parseJsonEntity(
                                                EntityEnum.User,
                                                jsonObject.getJSONObject(
                                                        Constant.INFO_KEY));
                                intent.putExtra(User.TAG, user);
                                loadRequiredPosts(intent);
                            } else
                            {
                                loginFragment.dismissProgressDialog();
                                Toast.makeText(MainActivity.this,
                                        jsonObject.getString(Constant.INFO_KEY),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException | UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override public void onErrorResponse(VolleyError error)
            {
                loginFragment.dismissProgressDialog();
                Toast.makeText(MainActivity.this, "服务器内部错误" + error.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        loginQueue.add(loginRequest);
    }
    private void loadRequiredPosts(final Intent intent)
    {
        Handler handler = new Handler()
        {
            @Override public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                loginFragment.dismissProgressDialog();
                Bundle bundle = (Bundle) msg.obj;
                if (bundle.getString(Constant.STATUS_KEY)
                        .equals(Constant.FAIL_VALUE))
                {
                    Toast.makeText(MainActivity.this,
                            bundle.getString(Constant.INFO_KEY), Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        };
        new LoadEntityUtils(getApplicationContext(), handler)
                .loadPosts(1, 1, 1, 1, null);
        loginFragment.showProgressDialog("获取文件列表");
    }
    @Override protected void onSaveInstanceState(Bundle outState)
    {
//        super.onSaveInstanceState(outState);
    }
}