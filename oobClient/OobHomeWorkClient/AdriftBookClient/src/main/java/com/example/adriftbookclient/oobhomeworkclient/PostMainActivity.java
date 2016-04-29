package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.klicen.navigationbar.NavigationBarFragment;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainActivity extends AppCompatActivity
{

    PostMainFragment postMainFragment;
    FragmentManager fragmentManager;
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);
        if (postMainFragment == null)
            postMainFragment = new PostMainFragment();
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, postMainFragment);
        transaction.commit();
    }
}
