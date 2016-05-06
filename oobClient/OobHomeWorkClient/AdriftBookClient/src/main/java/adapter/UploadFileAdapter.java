package adapter;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adriftbookclient.oobhomeworkclient.R;
import com.klicen.constant.BitmapUtil;

import java.util.ArrayList;

import adriftbook.entity.UploadFile;
/**
 * Created by Administrator on 2016/5/5.
 */
public class UploadFileAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<UploadFile> uploadFileList;
    private int postType;
    public void setPostType(int postType)
    {
        this.postType = postType;
    }
    public UploadFileAdapter(Context context, ArrayList<UploadFile> uploadFileList,
                             int postType)
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
        convertView = LayoutInflater.from(context)
                .inflate(R.layout.upload_file_item, parent, false);
        ImageView iv = (ImageView) convertView
                .findViewById(R.id.upload_file_item_iv);
        TextView author = (TextView) convertView
                .findViewById(R.id.upload_file_item_file_author);
        TextView fileName = (TextView) convertView
                .findViewById(R.id.upload_file_item_filename);
        TextView fileImageLocation = (TextView) convertView
                .findViewById(R.id.upload_file_item_file_image_location);
        TextView fileLocation = (TextView) convertView
                .findViewById(R.id.upload_file_item_file_location);
        UploadFile currentFile = (UploadFile) getItem(position);
        author.setText(Html.fromHtml(
                "作者：<font color=red>" + currentFile.getFileAuthor() + "</font>"));
        fileName.setText(Html.fromHtml(
                "书名：<font color=red>" + currentFile.getFileName() + "</font>"));
        if (currentFile.getImageFile() != null)
        {
            iv.setImageBitmap(BitmapUtil
                    .getThumbBitmap(currentFile.getImageFile().getAbsolutePath(),
                            BookBaseAdapter.PER_BOOK_ITEM_IMAGE_WIDTH,
                            BookBaseAdapter.PER_BOOK_ITEM_IMAGE_HEIGHT));
            fileImageLocation.setText(Html.fromHtml(
                    "图片位置:<font color=red>" + currentFile.getImageFile().getName() +
                            "</font>"));
        }
        if (currentFile.getFile() != null)
            fileLocation.setText(Html.fromHtml(
                    "资源位置:<font color=red>" + currentFile.getFile().getName() +
                            "</font>"));
        else
            fileLocation.setVisibility(View.GONE);
        AbsListView.LayoutParams uploadFileItemParams = (AbsListView.LayoutParams) convertView
                .getLayoutParams();
        convertView
                .measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        uploadFileItemParams.height = convertView.getMeasuredHeight();
        return convertView;
    }
}
