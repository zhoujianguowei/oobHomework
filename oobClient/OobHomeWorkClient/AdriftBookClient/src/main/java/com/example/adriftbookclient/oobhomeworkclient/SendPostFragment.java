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
import android.widget.Toast;

import com.jianguo.adriftbookclient.customviews.MyScrollView;
import com.klicen.constant.Constant;
import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

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

    Spinner postTypeSpinner;
    EditText postTitle;
    EditText postContent;
    Button addFileBt;
    ListView fileLv;
    Button startUploadFileBt;
    User user;
    public static final String TAG = "sendpostfragment";
    private static final int POST_CONTENT_HEIGHT = ScreenSize.getScreenHeight() / 5;
    private String[] arrayString = new String[]{"求漂区", "放漂区", "电子书籍"};
    boolean isSpinnerFirstSetAdapter = true;
    ArrayList<UploadFile> uploadFileList = new ArrayList<>();
    UploadFileAdapter uploadFileAdapter;
    private int currentPostType;
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
        postTypeSpinner = (Spinner) view
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
                .findViewById(R.id.fragment_send_post_add_file_bt);
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
        postTypeSpinner.setOnItemSelectedListener(this);
        String[] spinnerFirstArrays = getResources()
                .getStringArray(R.array.post_type_arrays);
        postTypeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, spinnerFirstArrays));
        addFileBt.setOnClickListener(this);
        postTitle.addTextChangedListener(this);
        postContent.addTextChangedListener(this);
        startUploadFileBt.setOnClickListener(this);
        postContent.setMinHeight(ScreenSize.getScreenHeight() / 10);
     /*   RelativeLayout.LayoutParams postContentParams = (RelativeLayout.LayoutParams)
                postContent
                        .getLayoutParams();
        postContentParams.height= POST_CONTENT_HEIGHT;*/
        return view;
    }
    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_send_post_start_upload_bt:
                if (uploadFileList.isEmpty())
                {
                    Toast.makeText(getActivity(), "你还没有添加图书", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                startUploadFiles(user.getUserId(), uploadFileList);
                return;
        }
        if (getActivity() instanceof SendPostFragmentOnClickListener)
        {
            /**
             * 需要与post的postype保持一致
             */
            ((SendPostFragmentOnClickListener) getActivity())
                    .sendPostFragmentOnClick(
                            v, postTypeSpinner.getSelectedItemPosition() + 1);
        }
    }
    private InputStream getInputStream(Object obj, String character)
    {
        InputStream inputStream = null;
        try
        {
            if (obj instanceof String || obj instanceof Number)
                inputStream = new ByteArrayInputStream(
                        obj.toString().getBytes(character));
            else if (obj instanceof File)
                inputStream = new FileInputStream((File) obj);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return inputStream;
    }
    /**
     * 上传文件
     * @param userId
     * @param uploadFileList
     */
    protected void startUploadFiles(int userId, ArrayList<UploadFile> uploadFileList)
    {
        RequestParams requestParams = new RequestParams();
        requestParams.put(User.USER_ID,
                getInputStream(userId + "", Constant.DEFAULT_CODE));
        requestParams.put("bookcount", uploadFileList.size());
        requestParams.put(Post.POST_TYPE, currentPostType);
        requestParams.put(Post.POST_TITLE, postTitle.getText().toString().trim());
        requestParams
                .put(Post.POST_CONTENT, postContent.getText().toString().trim());
        ArrayList<HashMap<String, Object>> mapList = new ArrayList<>();
        //不能使用Map传输资源文件，否则服务端无法接收,改成post[index][item]的形式
        //然后直接放在RequestParams中
        int i = 0;
        for (UploadFile uploadFile : uploadFileList)
        {
            HashMap<String, Object> map = new HashMap<>();
            map.put(UploadFile.FILE_NAME,
                    uploadFile.getFileName());
            map.put(UploadFile.FILE_AUTHOR,
                    uploadFile.getFileAuthor());
            File file = uploadFile.getFile();
            try
            {
                if (file != null)
                {
//                map.put(UploadFile.FILE,
//                        file);
                    requestParams
                            .put(Post.TAG + "[" + i + "]" + "[" + UploadFile.FILE +
                                            "]",
                                    file);
                }
                File imageFile = uploadFile.getImageFile();
                if (imageFile != null)
                {
//                map.put(UploadFile.IMAGE_FILE,
//                        getInputStream(file, Constant.DEFAULT_CODE));
                    requestParams
                            .put(Post.TAG + "[" + i + "]" + "[" +
                                            UploadFile.IMAGE_FILE + "]",
                                    imageFile);
                }
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            mapList.add(map);
            i++;
        }
        requestParams.put(Post.TAG, mapList);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.post(Constant.CONSTANT_IP + "upload", requestParams,
                new AsyncHttpResponseHandler()
                {
                    @Override public void onStart()
                    {
                        super.onStart();
                        Toast.makeText(getActivity(), "开始上传", Toast.LENGTH_SHORT)
                                .show();
                    }
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          byte[] responseBody)
                    {
                        try
                        {
                            Toast.makeText(getActivity(),
                                    new String(responseBody, Constant.DEFAULT_CODE),
                                    Toast.LENGTH_LONG).show();
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          byte[] responseBody,
                                          Throwable error)
                    {
                        try
                        {
                            if (responseBody != null && responseBody.length > 0)
                                Toast.makeText(getActivity(),
                                        new String(responseBody,
                                                Constant.DEFAULT_CODE),
                                        Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getActivity(), "服务器内部错误",
                                        Toast.LENGTH_LONG).show();
                        }
                        catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
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
                postTypeSpinner.setAdapter(new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, arrayString));
                if (position != 0)
                {
                    postTypeSpinner.setSelection(position - 1);
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
            addFileBt.setEnabled(false);
            startUploadFileBt.setEnabled(false);
            currentPostType = position + 1;
            uploadFileList.clear();
            uploadFileAdapter.notifyDataSetChanged();
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
