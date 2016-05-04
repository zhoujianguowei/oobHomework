package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.klicen.navigationbar.BackStackFragmentWithProgressDialog;
/**
 * Created by Administrator on 2016/5/5.
 */
public class AddFileFragment extends BackStackFragmentWithProgressDialog implements
        View.OnClickListener, TextWatcher
{

    EditText author;
    EditText fileName;
    TextView fileLocation;
    Button selectFileBt;
    Button deliverBt;
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState)
    {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_file, container, false);
        author = (EditText) view.findViewById(R.id.fragment_add_file_author);
        fileName = (EditText) view.findViewById(R.id.fragment_add_file_file_name);
        fileLocation = (TextView) view.findViewById(R.id.fragment_add_file_location);
        selectFileBt = (Button) view
                .findViewById(R.id.fragment_add_file_select_file_bt);
        deliverBt = (Button) view
                .findViewById(R.id.fragment_add_file_deliver_file_bt);
        selectFileBt.setEnabled(false);
        deliverBt.setEnabled(false);
        author.addTextChangedListener(this);
        fileName.addTextChangedListener(this);
        selectFileBt.setOnClickListener(this);
        deliverBt.setOnClickListener(this);
        return view;
    }
    @Override public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.fragment_add_file_select_file_bt:
                break;
            case R.id.fragment_add_file_deliver_file_bt:
                break;
        }
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
        if (author.getText().toString().trim().length() < 2 ||
                fileName.getText().toString().trim().length() < 6)
        {
            selectFileBt.setEnabled(false);
            deliverBt.setEnabled(false);
        } else
        {
            selectFileBt.setEnabled(true);
            deliverBt.setEnabled(true);
        }
    }
}
