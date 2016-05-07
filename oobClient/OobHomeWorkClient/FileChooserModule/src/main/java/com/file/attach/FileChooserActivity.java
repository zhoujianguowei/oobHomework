package com.file.attach;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class FileChooserActivity extends ListActivity
{

    private File currentDir;
    private FileArrayAdapter adapter;
    private static final File EXTERNAL_SDCARD_STORAGE = Environment
            .getExternalStorageDirectory();
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        currentDir = EXTERNAL_SDCARD_STORAGE;
        fill(currentDir);
    }
    private void fill(File f)
    {
        File[] dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option> fls = new ArrayList<Option>();
        try
        {
            for (File ff : dirs)
            {
                if (ff.isDirectory())
                    dir.add(new Option(ff.getName(), "Folder",
                            ff.getAbsolutePath()));
                else
                {
                    fls.add(new Option(ff.getName(), "File Size: " + ff.length(),
                            ff.getAbsolutePath()));
                }
            }
        }
        catch (Exception e)
        {
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0, new Option("..", "Parent Directory", f.getParent()));
        adapter = new FileArrayAdapter(FileChooserActivity.this, R.layout.file_view,
                dir);
        this.setListAdapter(adapter);
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        //if folder we go to the folder
        if (o.getData().equalsIgnoreCase("folder") ||
                o.getData().equalsIgnoreCase("parent directory"))
        {
            currentDir = new File(o.getPath());
            fill(currentDir);
        } else
        {
            onFileClick(o);
            Intent intent = getIntent();
            intent.putExtra("path", o.getPath());
            setResult(Activity.RESULT_OK, intent);
            FileChooserActivity.this.finish();
        }
    }
    /*
     * Got the file, let's do something with it
     *
     */
    private void onFileClick(Option o)
    {
        formStream(o);
       /* Toast.makeText(this, "File Clicked: " + o.getName(), Toast.LENGTH_SHORT)
                .show();*/
    }
    private void formStream(Option o)
    {
        try
        {
            // read this file into InputStream
            InputStream inputStream = new FileInputStream(o.getPath());
            // write the inputStream to a FileOutputStream
            //OutputStream out = new FileOutputStream(new File("foodfood1"));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1)
            {
                //out.write(bytes, 0, read);
            }
            inputStream.close();
            //out.flush();
            //out.close();
            System.out.println("New file created!");
        }
        catch (IOException e)
        {
            Toast.makeText(this, "Something went wrong with streams: " + o.getName(),
                    Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }
}

