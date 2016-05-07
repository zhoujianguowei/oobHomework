package com.example.adriftbookclient.oobhomeworkclient;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
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
/**
 * Created by Administrator on 2016/4/28.
 */
public class PostMainActivity extends SupActivityHandleFragment
        implements PostMainFragment.OnPostItemClickListener, Handler.Callback,
        BookBaseAdapter.OnBookListDownloadButtonClickListener,
        PostDetailFragment.OnBookListItemClickListener,
        PostMainFragment.OnPopupWindowItemClickListener,
        SendPostFragment.SendPostFragmentOnClickListener,
        AddFileFragment.AddFileFragmentOnClickListener,
        SendPostFragment.OnPostSentFinishListerner
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
    public static int UPLOAD_NOTIFICATION_ID = 1;
    public static int UPLOAD_NOTIFICAITON_STATUS_ID = 2;
    NotificationManager notificationManager;
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
                                captureImagePath), 400,
                        300);
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
        notificationManager = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (postMainFragment == null)
            postMainFragment = new PostMainFragment();
        postMainFragment.setRetainInstance(true);
        postMainFragment.setArguments(getIntent().getExtras());
        user = (User) getIntent().getSerializableExtra(User.TAG);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.activity_post_navigation_bar_fr_container,
                new NavigationBarFragment(), NavigationBarFragment.TAG);
        transaction.add(R.id.activity_post_content_container, postMainFragment,
                PostMainFragment.TAG);
        transaction.commit();
//        setTapFragment(fragmentManager, PostMainFragment.TAG);
        addFragmentTag(PostMainFragment.TAG);
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
//        setTapFragment(fragmentManager, PostDetailFragment.TAG);
        addFragmentTag(PostDetailFragment.TAG);
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
                .getThumbBitmap(bm, BookBaseAdapter.PER_BOOK_ITEM_IMAGE_WIDTH,
                        BookBaseAdapter.PER_BOOK_ITEM_IMAGE_HEIGHT));
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
//        setTapFragment(fragmentManager, CommentAdriftBookFragment.TAG);
        addFragmentTag(CommentAdriftBookFragment.TAG);
    }
    @Override public void onPopupItemClick(View view)
    {
        switch (view.getId())
        {
            case R.id.fragment_post_main_pop_window_sendpost_tv:
                sendPostFragment = new SendPostFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(User.TAG, user);
                bundle.putString(SETTING_CONTENT,
                        ((TextView) view).getText().toString().trim());
                sendPostFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                fragmentTransaction
                        .add(R.id.activity_post_navigation_bar_fr_container,
                                new NavigationBarFragment(),
                                NavigationBarFragment.TAG);
                fragmentTransaction
                        .add(R.id.activity_post_content_container, sendPostFragment,
                                SendPostFragment.TAG);
                fragmentTransaction.hide(postMainFragment);
                fragmentTransaction.show(sendPostFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//        setSelectedFragmentTag(SendPostFragment.TAG);
//                setTapFragment(fragmentManager, SendPostFragment.TAG);
                addFragmentTag(SendPostFragment.TAG);
                break;
            case R.id.fragment_post_main_pop_window_download_tv:
                break;
        }
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
//                setTapFragment(fragmentManager, AddFileFragment.TAG);
                addFragmentTag(AddFileFragment.TAG);
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
                fragmentManager.popBackStack();
//                setTapFragment(fragmentManager, SendPostFragment.TAG);
//                addFragmentTag(SendPostFragment.TAG);
                sendPostFragment.refreshBookLv(uploadFile, 1);
                break;
        }
    }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (SendPostFragment.getPostStatus() ==
                SendPostFragment.POST_STATUS_SEND_ONGOING)
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("文件上传中,取消上传吗？");
            builder.setPositiveButton("等待上传", null).setNegativeButton("取消上传",
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SendPostFragment.httpClient.cancelAllRequests(true);
                            SendPostFragment.setPostStatus(
                                    SendPostFragment.POST_STATUS_SEND_FINISH);
                            notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override public void onPostSendFinish(int requestStatus, byte[] responseBody)
    {
        showSpecifyTag(PostMainFragment.TAG);
        Log.i("上传状态", "finish");
//        AlertDialog.Builder builder=AlertDialog.
//        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setView();
        notificationManager.cancel(UPLOAD_NOTIFICATION_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext());
        builder.setSmallIcon(R.mipmap.upload_status_small_icon);
        builder.setTicker("传输结束");
        builder.setAutoCancel(true);
        builder.setContentTitle("文件上传状态");
        builder.setContentIntent(PendingIntent
                .getActivity(getApplicationContext(), 1,
                        new Intent(this, this.getClass()),
                        PendingIntent.FLAG_UPDATE_CURRENT));
        switch (requestStatus)
        {
            case REQUEST_FAIL:
                builder.setContentText("文件上传失败");
                break;
            case REQUEST_SUCCESS:
                builder.setContentText("文件上传成功");
                postMainFragment.onRefresh();
                break;
        }
        notificationManager.notify(UPLOAD_NOTIFICAITON_STATUS_ID, builder.build());
//        setTapFragment(fragmentManager, PostMainFragment.TAG);
//        addFragmentTag(PostMainFragment.TAG);
    }
}
