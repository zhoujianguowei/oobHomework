package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jianguo.adriftbookclient.customviews.MyScrollView;
import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.util.ArrayList;

import adapter.UploadFileAdapter;
import adriftbook.entity.Post;
import adriftbook.entity.UploadFile;
import adriftbook.entity.User;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/5/4.
 */
public class SendPostFragment extends BackStackFragmentWithProgressDialog implements
        View.OnClickListener, AdapterView.OnItemSelectedListener, TextWatcher
{

    Spinner postType;
    EditText postTitle;
    EditText postContent;
    Button addFileBt;
    ListView fileLv;
    Button startUploadFileBt;
    User user;
    public static final String TAG = "sendpostfragment";
    private static final int POST_CONTENT_HEIGHT = ScreenSize.getScreenHeight() / 4;
    private String[] arrayString = new String[]{"求漂区", "放漂区", "电子书籍"};
    boolean isSpinnerFirstSetAdapter = true;
    ArrayList<UploadFile> uploadFileList = new ArrayList<>();
    UploadFileAdapter uploadFileAdapter;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_send_post, container, false);
        MyScrollView myScrollView = (MyScrollView) view
                .findViewById(R.id.fragment_send_post_myscrollview);
        myScrollView.setDownwardScroll(true);
        myScrollView.setUpwardScroll(true);
        user = (User) getArguments().getSerializable(User.TAG);
        postType = (Spinner) view
                .findViewById(R.id.fragment_send_post_post_type_spinner);
        postTitle = (EditText) view
                .findViewById(R.id.fragment_send_post_post_title_et);
        postContent = (EditText) view
                .findViewById(R.id.fragment_send_post_post_content_et);
        fileLv = (ListView) view
                .findViewById(R.id.fragment_send_post_upload_book_lv);
        uploadFileAdapter = new UploadFileAdapter(getActivity(), uploadFileList,
                Post.REQUEST_BOOK_AREA);
        fileLv.setAdapter(uploadFileAdapter);
        addFileBt = (Button) view
                .findViewById(R.id.fragment_send_post_upload_file_bt);
        startUploadFileBt = (Button) view
                .findViewById(R.id.fragment_send_post_start_upload_bt);
        //没有选择发帖类型，不可编辑
        postTitle.setFocusable(false);
        postTitle.setFocusableInTouchMode(false);
        postContent.setFocusable(false);
        postContent.setFocusableInTouchMode(false);
        addFileBt.setEnabled(false);
        startUploadFileBt.setEnabled(false);
        postTitle.addTextChangedListener(this);
        postContent.addTextChangedListener(this);
        postType.setOnItemSelectedListener(this);
        String[] spinnerFirstArrays = getResources()
                .getStringArray(R.array.post_type_arrays);
        postType.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, spinnerFirstArrays));
        addFileBt.setOnClickListener(this);
        postTitle.addTextChangedListener(this);
        postContent.addTextChangedListener(this);
        startUploadFileBt.setOnClickListener(this);
        RelativeLayout.LayoutParams postContentParams = (RelativeLayout.LayoutParams) postContent
                .getLayoutParams();
        postContentParams.height = POST_CONTENT_HEIGHT;
        return view;
    }
    @Override public void onClick(View v)
    {
        if (getActivity() instanceof SendPostFragmentOnClickListener)
        {
            /**
             * 需要与post的postype保持一致
             */
            ((SendPostFragmentOnClickListener) getActivity())
                    .sendPostFragmentOnClick(
                            v, postType.getSelectedItemPosition() + 1);
        }
    }
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        tv.setText("发帖");
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id)
    {
        if (position == 0 && isSpinnerFirstSetAdapter)
        {
            //没有选择发帖类型，不可编辑
            postTitle.setFocusable(false);
            postTitle.setFocusableInTouchMode(false);
            postContent.setFocusable(false);
            postContent.setFocusableInTouchMode(false);
        } else
        {
            if (isSpinnerFirstSetAdapter)
            {
                postTitle.setFocusable(true);
                postTitle.setFocusableInTouchMode(true);
                postContent.setFocusable(true);
                postContent.setFocusableInTouchMode(true);
                postType.setAdapter(new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, arrayString));
                if (position != 0)
                {
                    postType.setSelection(position - 1);
                    handleSelectedPost(position - 1);
                }
                isSpinnerFirstSetAdapter = false;
                return;
            }
        }
        if (!isSpinnerFirstSetAdapter)
        {
            postTitle.setText("");
            postContent.setText("");
            uploadFileList.clear();
            handleSelectedPost(position);
        }
    }
    /**
     *
     * @param selectedFile 添加的文件
     * @param operationType 如果为0那么删除所有文件，其它添加文件
     */
    public void refreshBookLv(UploadFile selectedFile, int operationType)
    {
        if (operationType == 0)
            uploadFileList.clear();
        else
            uploadFileList.add(selectedFile);
        if (uploadFileList.size() > 0)
            addFileBt.setText("继续添加");
        else
            addFileBt.setText("上传资源");
        if (uploadFileList.size() >= 4)
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fileLv
                    .getLayoutParams();
            params.height = 3 * PostDetailFragment.PER_BOOK_ITEM_HEIGHT;
        }
        uploadFileAdapter.notifyDataSetChanged();
    }
    /**
     * 对于不同类型的post处理类型有所差异
     * @param position
     */
    private void handleSelectedPost(int position)
    {
        switch (arrayString[position].trim())
        {
            case "求漂区":
                uploadFileAdapter.setPostType(Post.REQUEST_BOOK_AREA);
                break;
            case "放漂区":
                uploadFileAdapter.setPostType(Post.SEND_BOOK_AREA);
                break;
            case "电子书籍":
                uploadFileAdapter.setPostType(Post.EBOOK_AREA);
                break;
        }
    }
    @Override public void onNothingSelected(AdapterView<?> parent)
    {
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
        if (postTitle.getText().toString().trim().length() < 4 ||
                postContent.getText().toString().trim().length() < 6)
        {
            addFileBt.setEnabled(false);
            startUploadFileBt.setEnabled(false);
        } else
        {
            addFileBt.setEnabled(true);
            startUploadFileBt.setEnabled(true);
        }
    }
    interface SendPostFragmentOnClickListener
    {

        void sendPostFragmentOnClick(View v, int postType);
    }
}
