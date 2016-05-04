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

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;

import java.io.File;
import java.util.ArrayList;

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
    Button addFile;
    ListView fileLv;
    Button startUploadFile;
    User user;
    public static final String TAG = "sendpostfragment";
    private static final int POST_CONTENT_HEIGHT = ScreenSize.getScreenHeight() / 4;
    private String[] arrayString = new String[]{"求漂区", "放漂区", "电子书籍"};
    boolean isSpinnerFirstSetAdapter = true;
    ArrayList<File> fileList = new ArrayList<>();
    ArrayList<String> fileNameList = new ArrayList<>();
    ArrayAdapter<String> fileLvAdapter;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_send_post, container, false);
        user = (User) getArguments().getSerializable(User.TAG);
        postType = (Spinner) view
                .findViewById(R.id.fragment_send_post_post_type_spinner);
        postTitle = (EditText) view
                .findViewById(R.id.fragment_send_post_post_title_et);
        postContent = (EditText) view
                .findViewById(R.id.fragment_send_post_post_content_et);
        fileLv = (ListView) view
                .findViewById(R.id.fragment_send_post_upload_book_lv);
        fileLvAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, fileNameList);
        fileLv.setAdapter(fileLvAdapter);
        addFile = (Button) view.findViewById(R.id.fragment_send_post_upload_file_bt);
        startUploadFile = (Button) view
                .findViewById(R.id.fragment_send_post_start_upload_bt);
        //没有选择发帖类型，不可编辑
        postTitle.setFocusable(false);
        postTitle.setFocusableInTouchMode(false);
        postContent.setFocusable(false);
        postContent.setFocusableInTouchMode(false);
        addFile.setEnabled(false);
        startUploadFile.setEnabled(false);
        postTitle.addTextChangedListener(this);
        postContent.addTextChangedListener(this);
        postType.setOnItemSelectedListener(this);
        String[] spinnerFirstArrays = getResources()
                .getStringArray(R.array.post_type_arrays);
        postType.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, spinnerFirstArrays));
        addFile.setOnClickListener(this);
        startUploadFile.setOnClickListener(this);
        RelativeLayout.LayoutParams postContentParams = (RelativeLayout.LayoutParams) postContent
                .getLayoutParams();
        postContentParams.height = POST_CONTENT_HEIGHT;
        return view;
    }
    @Override public void onClick(View v)
    {
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
                    postType.setSelection(position - 1);
                isSpinnerFirstSetAdapter = false;
            }
        }
        if (!isSpinnerFirstSetAdapter)
        {
            handleSelectedPost(position);
        }
    }
    /**
     *
     * @param selectedFile 添加的文件
     * @param type 如果为0那么删除所有文件，为1添加文件
     */
    void refreshBookLv(File selectedFile, int type)
    {
        if (type == 0)
        {
            fileList.clear();
            fileNameList.clear();
        } else
        {
            fileList.add(selectedFile);
            fileNameList.add(selectedFile.getName());
        }
        fileLvAdapter.notifyDataSetChanged();
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

                break;
            case "放漂区":
                break;
            case "电子书籍":
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
    }
}
