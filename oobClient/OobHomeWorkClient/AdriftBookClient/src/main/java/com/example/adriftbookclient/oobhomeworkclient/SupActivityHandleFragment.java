package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Stack;
public class SupActivityHandleFragment extends AppCompatActivity
{

    protected String currentTag;
    public static final String CURRENT_FRAGMENT_TAG_KEY = "current_fragment_tag_key";
    String[] tags = new String[]{"registerfragment", "loginfragment",
            "postmainfragment", "postdetailfragment",
            "commentadriftbookfragment", "sendpostfragment", "addfilefragment"};
    protected Stack<HashMap<String, Fragment>> stackFragment = new Stack<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && (currentTag = savedInstanceState
                .getString(CURRENT_FRAGMENT_TAG_KEY)) != null)
        {
//            setSelectedFragmentTag(currentTag);
            setTapFragment(getSupportFragmentManager(), currentTag);
        }
    }
    @Override protected void onResume()
    {
        super.onResume();
        setTapFragment(getSupportFragmentManager(), currentTag);
    }
    protected final void setSelectedFragmentTag(String selectedFragmentTag)
    {
        currentTag = selectedFragmentTag;
    }
    @Override protected void onSaveInstanceState(Bundle outState)
    {
        if (currentTag != null)
            outState.putString(CURRENT_FRAGMENT_TAG_KEY, currentTag);
        super.onSaveInstanceState(outState);
    }
    protected void setTapFragment(FragmentManager fragmentManager,
                                  String fragmentTag)
    {
        if (fragmentManager == null)
            return;
        setSelectedFragmentTag(fragmentTag);
    /*    LoginFragment loginFragment = (LoginFragment) fragmentManager
                .findFragmentByTag(LoginFragment.TAG);
        RegisterFragment registerFragment = (RegisterFragment) fragmentManager
                .findFragmentByTag(
                        RegisterFragment.TAG);
        PostMainFragment postMainFragment = (PostMainFragment) fragmentManager
                .findFragmentByTag(PostMainFragment.TAG);
        PostDetailFragment postDetailFragment = (PostDetailFragment) fragmentManager
                .findFragmentByTag(
                        PostDetailFragment.TAG);
        CommentAdriftBookFragment commentAdriftBookFragment = (CommentAdriftBookFragment) fragmentManager
                .findFragmentByTag(CommentAdriftBookFragment.TAG);*/
        FragmentTransaction transaction = null;
        for (int i = 0; i < tags.length; i++)
        {
            Fragment fragment = fragmentManager.findFragmentByTag(tags[i]);
            if (tags[i].equals(fragmentTag) && fragment != null)
            {
                if (fragment.isHidden())
                {
                    transaction = fragmentManager.beginTransaction();
                    transaction.show(fragment);
                    transaction.commit();
                }
            } else if (fragment != null)
            {
                transaction = fragmentManager.beginTransaction();
                transaction.hide(fragment);
                transaction.commit();
            }
        }
    }
}
