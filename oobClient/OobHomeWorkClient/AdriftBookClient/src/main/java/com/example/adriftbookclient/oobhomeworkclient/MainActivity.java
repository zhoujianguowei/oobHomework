package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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

import utils.MyStringRequest;
import utils.ScreenSize;
public class MainActivity extends AppCompatActivity
        implements FileChooserFragment.FileChooserFragmentListItemClickListener,
        LoginFragment.LoginFragmentOnClickListener,
        RegisterFragment.RegisterFragmentOnClickListener
{

    FileChooserFragment fileChooserFragment;
    FragmentManager fragmentManager;
    LoginFragment loginFragment;
    RegisterFragment registerFragment;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenSize.initial(getApplicationContext());
        fragmentManager = getSupportFragmentManager();
        if (loginFragment == null)
        {
            transaction = fragmentManager.beginTransaction();
            loginFragment = new LoginFragment();
            transaction.add(R.id.activity_main_navigation_bar_fr_container,
                    new NavigationBarFragment(), NavigationBarFragment.TAG);
            transaction.add(R.id.activity_main_content_fr_container, loginFragment);
//            transaction.addToBackStack(null);
            transaction.commit();
        }
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
                        registerFragment);
                transaction.hide(loginFragment);
                transaction.show(registerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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
                        Log.e("res",response);
                        try
                        {
                            jsonObject = new JSONObject(new String(response.getBytes(Constant.DEFAULT_CODE)));
                            Toast.makeText(MainActivity.this,
                                    jsonObject.getString(Constant.INFO_KEY),
                                    Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException|UnsupportedEncodingException e)
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
    }
}
