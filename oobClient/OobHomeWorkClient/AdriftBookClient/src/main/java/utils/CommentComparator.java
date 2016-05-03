package utils;
import java.util.Comparator;

import adriftbook.entity.Comment;
/**
 * Created by Administrator on 2016/5/3.
 */
public class CommentComparator implements Comparator<Comment>
{

    @Override public int compare(Comment lhs, Comment rhs)
    {
        return (int) (rhs.getReviewDate().getTimeInMillis() -
                lhs.getReviewDate().getTimeInMillis());
    }
}
