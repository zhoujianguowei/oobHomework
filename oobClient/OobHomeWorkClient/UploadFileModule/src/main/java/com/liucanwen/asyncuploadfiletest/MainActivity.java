package com.liucanwen.asyncuploadfiletest;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.klicen.constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
public class MainActivity extends Activity
{

    private TextView uploadInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadInfo = (TextView) findViewById(R.id.upload_info);
        uploadFile();
    }
    public void uploadFile()
    {
        //服务器端地址
        String url = Constant.CONSTANT_IP + "upload";
        //手机端要上传的文件，首先要保存你手机上存在该文件
        String filePath = Environment.getExternalStorageDirectory()
                + "/Android/上传图片.jpg";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams param = new RequestParams();
        try
        {
            param.put("file", new File(filePath));
            param.put("content", "liucanwen");
            httpClient.post(url, param, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    super.onStart();
                    uploadInfo.setText("正在上传...");
                }
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes)
                {
                    Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG)
                            .show();
                }
                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes,
                                      Throwable throwable)
                {
                    try
                    {
                        Toast.makeText(MainActivity.this,
                                "上传失败",
                                Toast.LENGTH_LONG)
                                .show();
                        if (bytes != null)
                            Log.e("failure",
                                    new String(bytes, Constant.DEFAULT_CODE));
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "上传文件不存在！", Toast.LENGTH_LONG).show();
        }
    }
}
