package adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import adriftbook.entity.UploadFile;
/**
 * Created by Administrator on 2016/5/5.
 */
public class UploadFileAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<UploadFile> uploadFileList;
    public UploadFileAdapter(Context context, ArrayList<UploadFile> uploadFileList)
    {
        this.context = context;
        this.uploadFileList = uploadFileList;
    }
    @Override public int getCount()
    {
        return uploadFileList.size();
    }
    @Override public Object getItem(int position)
    {
        return uploadFileList.get(position);
    }
    @Override public long getItemId(int position)
    {
        return position;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        return null;
    }
}
