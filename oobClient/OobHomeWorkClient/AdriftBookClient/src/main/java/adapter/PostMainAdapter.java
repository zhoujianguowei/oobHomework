package adapter;
import android.content.Context;
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
            convertView.setTag(postViewHolder);
        } else
            postViewHolder = (PostMainItemViewHolder) convertView.getTag();
        Post currentPost = postList.get(position);
     /*   if (currentPost.getPostLabel().equals(Post.NEW_POST))
            postViewHolder.postLabel.setText(Html.fromHtml(
                    "<b><font color=green>" + Post.NEW_POST + "</font></b>"));
        else if (currentPost.getPostLabel().equals(Post.HOT_POST))
            postViewHolder.postLabel.setText(Html.fromHtml(
                    "<b><font color=green>" + Post.HOT_POST + "</font></b>"));*/
        postViewHolder.postTitle.setText(currentPost.getPostTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern("MM-dd");
        String postDate = dateFormat.format(currentPost.getPostDate());
        postViewHolder.postDate.setText(postDate);
        AbsListView.LayoutParams postItemParams = (AbsListView.LayoutParams) convertView
                .getLayoutParams();
        postItemParams.height = ScreenSize.getScreenHeight() / 10;
        return convertView;
    }
    private static class PostMainItemViewHolder
    {

        TextView postLabel;
        TextView postTitle;
        TextView postDate;
    }
}
