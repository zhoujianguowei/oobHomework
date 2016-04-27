package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.file.attach.FileChooserFragment;
import com.file.attach.Option;
import com.klicen.navigationbar.NavigationBarFragment;

import utils.ScreenSize;
public class MainActivity extends AppCompatActivity
        implements FileChooserFragment.FileChooserFragmentListItemClickListener,
        LoginFragment.LoginFragmentOnClickListener
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
    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_login_login_bt:
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
        }
    }
}
