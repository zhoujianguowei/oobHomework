package com.example.adriftbookclient.oobhomeworkclient;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.klicen.constant.Constant;
import com.klicen.navigationbar.NavigationBarFragment;

import adapter.BookBaseAdapter;
import adriftbook.entity.AdriftBook;
import adriftbook.entity.Post;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainActivity extends AppCompatActivity
        implements PostMainFragment.OnPostItemClickListener, Handler.Callback,
        BookBaseAdapter.OnBookListDownloadButtonClickListener,PostDetailFragment.OnBookListItemClickListener
{

    PostMainFragment postMainFragment;
    PostDetailFragment postDetailFragment;
    FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    private void showProgressDialog(String msg)
    {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void dismissProgressDialog()
    {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_main);
        if (postMainFragment == null)
            postMainFragment = new PostMainFragment();
        postMainFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, postMainFragment);
        transaction.commit();
    }
    @Override public void onItemClick(Post post)
    {
        showProgressDialog("玩命加载中...");
        Handler loadPostDetail = new Handler(this);
        new LoadEntityUtils(this, loadPostDetail).loadComments(post);
    }
    @Override public boolean handleMessage(Message msg)
    {
        dismissProgressDialog();
        postDetailFragment = new PostDetailFragment();
        Bundle bundle = (Bundle) msg.obj;
        if (bundle.getString(Constant.STATUS_KEY).equals(Constant.FAIL_VALUE))
        {
            Toast.makeText(this, "数据加载失败", Toast.LENGTH_LONG).show();
            return true;
        }
        postDetailFragment.setArguments((Bundle) msg.obj);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, postDetailFragment);
        transaction.hide(postMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        return true;
    }
    @Override public void onItemClick(String ebookUrl)
    {
        Toast.makeText(this, "下载文件", Toast.LENGTH_LONG).show();
    }

    @Override public void onBookItemClick(AdriftBook book, Bitmap bm)
    {
        CommentAdriftBookFragment commentFragment = new CommentAdriftBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bundle.putParcelable("bitmap", bm);
        commentFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, commentFragment);
        transaction.hide(postDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
