package com.example.adriftbookclient.oobhomeworkclient;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.file.attach.FileChooserFragment;
import com.file.attach.Option;
public class MainActivity extends AppCompatActivity
        implements FileChooserFragment.FileChooserFragmentListItemClickListener
{

    FileChooserFragment fileChooserFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (fileChooserFragment == null)
        {
            fileChooserFragment = new FileChooserFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.file_chooser_fl_container, fileChooserFragment);
            transaction.commit();
        }
    }
    @Override public void onListItemClickListener(Option option)
    {
        Toast.makeText(this, option.getName(), Toast.LENGTH_LONG).show();
    }
}
