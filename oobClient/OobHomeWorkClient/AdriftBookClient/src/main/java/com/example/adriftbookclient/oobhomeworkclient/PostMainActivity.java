package com.example.adriftbookclient.oobhomeworkclient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.file.attach.FileChooserActivity;
import com.klicen.constant.BitmapUtil;
import com.klicen.constant.Constant;
import com.klicen.navigationbar.NavigationBarFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import adapter.BookBaseAdapter;
import adriftbook.entity.AdriftBook;
import adriftbook.entity.Post;
import adriftbook.entity.UploadFile;
import adriftbook.entity.User;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainActivity extends SupActivityHandleFragment
        implements PostMainFragment.OnPostItemClickListener, Handler.Callback,
        BookBaseAdapter.OnBookListDownloadButtonClickListener,
        PostDetailFragment.OnBookListItemClickListener,
        PostMainFragment.OnPopupWindowItemClickListener,
        SendPostFragment.SendPostFragmentOnClickListener,
        AddFileFragment.AddFileFragmentOnClickListener
{

    public static final int CAPTURE_PICTURE_REQUEST = 1;//打开相机
    private static final int SELECT_FILE_REQUESTCODE = 2;//选择文件
    PostMainFragment postMainFragment;
    PostDetailFragment postDetailFragment;
    SendPostFragment sendPostFragment;
    AddFileFragment addFileFragment;
    FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    public static User user;
    public static final String SETTING_CONTENT = "setting_content";
    private String captureImagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == CAPTURE_PICTURE_REQUEST)
            {
                File captureFile = new File(captureImagePath);
                if (!captureFile.exists())
                    return;
                BitmapUtil.getClipBitmapFile(captureFile.getParent(),
                        captureFile.getName(), BitmapFactory.decodeFile(
                                captureImagePath), ScreenSize.getScreenWidth() / 6,
                        ScreenSize.getScreenHeight() / 7);
                addFileFragment
                        .refreshSelectFile(captureFile, AddFileFragment.IMAGE_TYPE);
            } else if (requestCode == SELECT_FILE_REQUESTCODE)
            {
                File selectFile = new File(data.getStringExtra("path"));
                addFileFragment
                        .refreshSelectFile(selectFile, AddFileFragment.FILE_TYPE);
            }
        }
    }
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
        user = (User) getIntent().getSerializableExtra(User.TAG);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, postMainFragment,
                PostMainFragment.TAG);
        transaction.commit();
        setTapFragment(fragmentManager, PostMainFragment.TAG);
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
        transaction.add(R.id.activity_post_content_container, postDetailFragment,
                PostDetailFragment.TAG);
        transaction.hide(postMainFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        setTapFragment(fragmentManager, PostDetailFragment.TAG);
        return true;
    }
    @Override public void onItemClick(String ebookUrl)
    {
        Toast.makeText(this, "下载文件", Toast.LENGTH_LONG).show();
    }
    @Override public void onBookItemClick(AdriftBook book, Bitmap bm, Post post)
    {
        CommentAdriftBookFragment commentFragment = new CommentAdriftBookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("book", book);
        bundle.putParcelable("bitmap", BitmapUtil
                .getThumbBitmap(bm, PostDetailFragment.PER_BOOK_ITEM_IMAGE_WIDTH,
                        PostDetailFragment.PER_BOOK_ITEM_IMAGE_HEIGHT));
        bundle.putSerializable(Post.TAG, post);
        commentFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, commentFragment,
                CommentAdriftBookFragment.TAG);
        transaction.hide(postDetailFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        setTapFragment(fragmentManager, CommentAdriftBookFragment.TAG);
    }
    @Override public void onPopupItemClick(View view)
    {
        sendPostFragment = new SendPostFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(User.TAG, user);
        bundle.putString(SETTING_CONTENT,
                ((TextView) view).getText().toString().trim());
        sendPostFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        fragmentTransaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        fragmentTransaction
                .add(R.id.activity_post_content_container, sendPostFragment,
                        SendPostFragment.TAG);
        fragmentTransaction.hide(postMainFragment);
        fragmentTransaction.show(sendPostFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
//        setSelectedFragmentTag(SendPostFragment.TAG);
        setTapFragment(fragmentManager, SendPostFragment.TAG);
    }
    @Override public void sendPostFragmentOnClick(View v, int postType)
    {
        switch (v.getId())
        {
            case R.id.fragment_send_post_add_file_bt:
                addFileFragment = new AddFileFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Post.POST_TYPE, postType);
                addFileFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction
                        .add(R.id.activity_post_navigation_bar_fr_container,
                                new NavigationBarFragment(),
                                NavigationBarFragment.TAG);
                fragmentTransaction
                        .add(R.id.activity_post_content_container,
                                addFileFragment,
                                AddFileFragment.TAG);
                fragmentTransaction.hide(sendPostFragment);
                fragmentTransaction.show(addFileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                setSelectedFragmentTag(AddFileFragment.TAG);
                setTapFragment(fragmentManager, AddFileFragment.TAG);
                break;
        }
    }
    @Override protected void onResume()
    {
        super.onResume();
    }
    @Override public void addFileFragmentOnClick(View v, UploadFile uploadFile)
    {
        switch (v.getId())
        {
            //拍照处理：
            case R.id.fragment_add_file_select_image_bt:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                SimpleDateFormat dateFormat = new SimpleDateFormat();
                dateFormat.applyPattern("yyyy:MM:dd HH:mm:ss");
                File captureImageFileParent = new File(
                        Environment.getExternalStorageDirectory(), "capture");
                if (!captureImageFileParent.exists())
                    captureImageFileParent.mkdirs();
                File captureImageFile = new File(captureImageFileParent,
                        user.getUserName() + " " +
                                dateFormat.format(Calendar.getInstance().getTime()) +
                                ".jpg");
                captureImagePath = captureImageFile.getAbsolutePath();
                Uri uri = Uri.fromFile(captureImageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, CAPTURE_PICTURE_REQUEST);
                break;
            case R.id.fragment_add_file_select_file_bt:
                Intent fileIntent = new Intent(this, FileChooserActivity.class);
                startActivityForResult(fileIntent,
                        PostMainActivity.SELECT_FILE_REQUESTCODE);
                break;
            case R.id.fragment_add_file_deliver_file_bt:
                sendPostFragment.refreshBookLv(uploadFile, 1);
                fragmentManager.popBackStack();
                setTapFragment(fragmentManager, SendPostFragment.TAG);
                break;
        }
    }
}
