package adapter;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adriftbookclient.oobhomeworkclient.R;

import java.text.SimpleDateFormat;
import java.util.List;

import adriftbook.entity.Post;
import utils.ScreenSize;
/**
 * Created by Administrator on 2016/4/29.
 */
public class PostMainAdapter extends BaseAdapter
{

    Context context;
    List<Post> postList;
    public static final int PER_POST_ITEM_HEIGTH = ScreenSize.getScreenHeight() / 8;
    public PostMainAdapter(Context context, List<Post> postList)
    {
        this.context = context;
        this.postList = postList;
    }
    @Override public int getCount()
    {
        return postList.size();
    }
    @Override public Object getItem(int position)
    {
        return postList.get(position);
    }
    @Override public long getItemId(int position)
    {
        return position;
    }
    @Override public View getView(int position, View convertView, ViewGroup parent)
    {
        PostMainItemViewHolder postViewHolder;
        if (convertView == null)
        {
            postViewHolder = new PostMainItemViewHolder();
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.post_main_item, null);
            postViewHolder.postLabel = (TextView) convertView
                    .findViewById(R.id.post_main_item_label_tv);
            postViewHolder.postTitle = (TextView) convertView
                    .findViewById(R.id.post_main_item_post_title_tv);
            postViewHolder.postDate = (TextView) convertView
                    .findViewById(R.id.post_main_item_post_date_tv);
            postViewHolder.postAuthor = (TextView) convertView
                    .findViewById(R.id.post_main_item_post_author_tv);
            convertView.setTag(postViewHolder);
        } else
            postViewHolder = (PostMainItemViewHolder) convertView.getTag();
        Post currentPost = postList.get(position);
        if (currentPost.getLabelStatus().equals(Post.HOST_POST_LABEL_STATUS))
            postViewHolder.postLabel.setText(Html.fromHtml(
                    "<big><b><font color=red>" + Post.HOST_POST_LABEL_STATUS +
                            "</font></b></big>"));
        else if (currentPost.getLabelStatus().equals(Post.NEW_POST_LABEL_STATUS))
            postViewHolder.postLabel.setText(Html.fromHtml(
                    "<big><b><font color=green>" + Post.NEW_POST_LABEL_STATUS +
                            "</font></b></big>"));
        else
            postViewHolder.postLabel.setText("");
        postViewHolder.postTitle.setText(currentPost.getPostTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("MM-dd");
        String postDate = dateFormat.format(currentPost.getPostDate().getTime());
        postViewHolder.postDate.setText(postDate);
        postViewHolder.postAuthor.setText(currentPost.getPostUser().getUserName());
        AbsListView.LayoutParams postItemParams = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        postItemParams.height = PER_POST_ITEM_HEIGTH;
        convertView.setLayoutParams(postItemParams);
        return convertView;
    }
    private static class PostMainItemViewHolder
    {

        TextView postLabel;
        TextView postTitle;
        TextView postDate;
        TextView postAuthor;
    }
}
