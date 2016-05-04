package adriftbook.utils;
import java.util.Comparator;

import adriftbook.entity.Post;
/**
 * 将Post按照日期从大到校排序
 */
public class PostComparator implements Comparator<Post>
{

    @Override public int compare(Post o1, Post o2)
    {
        return (int) (o2.getPostDate().getTimeInMillis() -
                o1.getPostDate().getTimeInMillis());
    }
}