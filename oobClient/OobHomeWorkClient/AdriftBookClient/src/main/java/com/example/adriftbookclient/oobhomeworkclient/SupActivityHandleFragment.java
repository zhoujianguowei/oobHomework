package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Stack;
public class SupActivityHandleFragment extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener
{

    String[] tags = new String[]{"registerfragment", "loginfragment",
            "postmainfragment", "postdetailfragment",
            "commentadriftbookfragment", "sendpostfragment", "addfilefragment"};
    protected int preBackStackRecordsCount = -1;
    protected Stack<String> fragmentTagStack = new Stack<>();
    public static final int REQUEST_SUCCESS=1;
    public static final int REQUEST_FAIL=2;
    FragmentManager fragmentManager;
    /**
     * 当fragment从backstack中弹出时候，重置stack
     */
    protected String popFragmentTag()
    {
        String nextTag = null;
        if (fragmentTagStack.size() > 1)
        {
            fragmentTagStack.pop();
            nextTag = this.fragmentTagStack.peek();
        }
        setTapFragment(nextTag);
        return nextTag;
    }
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        preBackStackRecordsCount = fragmentManager.getBackStackEntryCount();
        fragmentManager.addOnBackStackChangedListener(this);
    }
    protected void addFragmentTag(String fragmentTag)
    {
        if (fragmentTagStack.size() > 0 &&
                fragmentTagStack.peek().equals(fragmentTag))
            return;
        fragmentTagStack.push(fragmentTag);
        setTapFragment(fragmentTag);
    }
    @Override protected void onDestroy()
    {
        super.onDestroy();
        getSupportFragmentManager().removeOnBackStackChangedListener(this);
        fragmentTagStack.clear();
    }
    @Override protected void onResume()
    {
        super.onResume();
        if (fragmentTagStack.size() > 0)
            setTapFragment(fragmentTagStack.peek());
    }
    private void setTapFragment(
            String fragmentTag)
    {
        if (fragmentManager == null || fragmentTag == null)
            return;
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
    @Override public void onBackStackChanged()
    {
        int currentBackstackRecordsCount = fragmentManager.getBackStackEntryCount();
        //当前是pop操作
        if (currentBackstackRecordsCount < preBackStackRecordsCount)
            popFragmentTag();
        Log.i("stack",fragmentTagStack.toString());
        preBackStackRecordsCount=currentBackstackRecordsCount;
    }
}
