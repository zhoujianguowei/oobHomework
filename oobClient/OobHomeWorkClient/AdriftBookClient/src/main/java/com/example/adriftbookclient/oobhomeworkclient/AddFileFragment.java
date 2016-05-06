package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import adriftbook.entity.Post;
import adriftbook.entity.UploadFile;
/**
 * Created by Administrator on 2016/5/5.
 */
public class AddFileFragment extends BackStackFragmentWithProgressDialog implements
        View.OnClickListener, TextWatcher
{

    EditText fileAuthor;
    EditText fileName;
    TextView fileImageLocation;
    File selectImage;
    TextView fileLocation;
    File selectFile;
    Button selectFileImageBt;
    Button selectFileBt;
    Button deliverBt;
    public static final String TAG = "addfilefragment";
    private int postType;
    public static final int IMAGE_TYPE = 1;
    public static final int FILE_TYPE = 2;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_file, container, false);
        postType = getArguments().getInt(Post.POST_TYPE);
        fileAuthor = (EditText) view.findViewById(R.id.fragment_add_file_author_et);
        fileName = (EditText) view.findViewById(R.id.fragment_add_file_file_name_et);
        fileImageLocation = (TextView) view
                .findViewById(R.id.fragment_add_file_file_image_location);
        fileLocation = (TextView) view.findViewById(R.id.fragment_add_file_location);
        selectFileImageBt = (Button) view
                .findViewById(R.id.fragment_add_file_select_image_bt);
        selectFileBt = (Button) view
                .findViewById(R.id.fragment_add_file_select_file_bt);
        deliverBt = (Button) view
                .findViewById(R.id.fragment_add_file_deliver_file_bt);
        selectFileImageBt.setEnabled(false);
        if (postType == Post.REQUEST_BOOK_AREA || postType == Post.SEND_BOOK_AREA)
        {
            fileLocation.setVisibility(View.GONE);
            selectFileBt.setVisibility(View.GONE);
        }
        selectFileBt.setEnabled(false);
        deliverBt.setEnabled(false);
        fileAuthor.addTextChangedListener(this);
        fileName.addTextChangedListener(this);
        selectFileImageBt.setOnClickListener(this);
        selectFileBt.setOnClickListener(this);
        deliverBt.setOnClickListener(this);
        return view;
    }
    @Override protected View onTitleCreate()
    {
        TextView tv = new TextView(getActivity());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources()
                .getDimensionPixelSize(R.dimen.xx_large));
        switch (postType)
        {
            case Post.REQUEST_BOOK_AREA:
                tv.setText("求漂区");
                break;
            case Post.SEND_BOOK_AREA:
                tv.setText("放漂区");
                break;
            case Post.EBOOK_AREA:
                tv.setText("电子书籍");
                break;
        }
        tv.setTextColor(getResources().getColor(R.color.shallow_blue));
        return tv;
    }
    public void refreshSelectFile(File file, int selectFileType)
    {
        switch (selectFileType)
        {
            case AddFileFragment.FILE_TYPE:
                selectFile = file;
                fileLocation.setVisibility(View.VISIBLE);
                fileLocation.setText(Html.fromHtml(
                        "文件位置：<font color=red>" + file.getName() +
                                "</font>"));
                break;
            case AddFileFragment.IMAGE_TYPE:
                selectImage = file;
                fileImageLocation.setVisibility(View.VISIBLE);
                fileImageLocation.setText(Html.fromHtml(
                        "图片位置：<font color=red>" + file.getName() +
                                "</font>"));
                break;
        }
    }
    @Override public void onClick(View v)
    {
        UploadFile uploadFile = new UploadFile();
        switch (v.getId())
        {
            case R.id.fragment_add_file_select_image_bt:
            case R.id.fragment_add_file_select_file_bt:
                break;
            case R.id.fragment_add_file_deliver_file_bt:
                if (postType == Post.EBOOK_AREA &&
                        TextUtils.isEmpty(fileLocation.getText()))
                {
                    Toast.makeText(getActivity(), "你还没有选择文件资源", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                uploadFile.setFile(selectFile);
                uploadFile.setFileAuthor(fileAuthor.getText().toString());
                uploadFile.setFileName(fileName.getText().toString());
                uploadFile.setImageFile(selectImage);
                break;
        }
        if (getActivity() instanceof AddFileFragmentOnClickListener)
            ((AddFileFragmentOnClickListener) getActivity())
                    .addFileFragmentOnClick(v, uploadFile);
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
        if (fileAuthor.getText().toString().trim().length() < 2 ||
                fileName.getText().toString().trim().length() < 2)
        {
            selectFileBt.setEnabled(false);
            deliverBt.setEnabled(false);
        } else
        {
            selectFileImageBt.setEnabled(true);
            selectFileBt.setEnabled(true);
            deliverBt.setEnabled(true);
        }
    }
    interface AddFileFragmentOnClickListener
    {

        void addFileFragmentOnClick(View v, UploadFile uploadFile);
    }
}
