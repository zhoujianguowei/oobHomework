package utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import adriftbook.entity.AdriftBook;
import adriftbook.entity.Comment;
import adriftbook.entity.EBook;
import adriftbook.entity.EntityEnum;
import adriftbook.entity.Post;
import adriftbook.entity.PostContent;
import adriftbook.entity.User;
/**
 * Created by Administrator on 2016/4/29.
 */
public class JsonEntityParser
{

    private static JsonEntityParser parser;
    public static synchronized JsonEntityParser getSingleInstance()
    {
        if (parser == null)
            parser = new JsonEntityParser();
        return parser;
    }
    public Object parseJsonEntity(EntityEnum entityEnum, JSONObject jsonObject)
    {
        return parseJsonEntity(entityEnum, jsonObject.toString());
    }
    public Object parseJsonEntity(EntityEnum entityEnum, String jsonStr)
    {
        Object obj = null;
        try
        {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Calendar calendar = Calendar.getInstance();
            switch (entityEnum)
            {
                case AdriftBook:
                    AdriftBook adriftBook = null;
                    if (jsonObject.getInt("type") == AdriftBook.EBOOK)
                    {
                        adriftBook = new EBook();
                        ((EBook) adriftBook)
                                .setEbookUrl(
                                        jsonObject.getString(AdriftBook.EBOOK_URL));
                    } else
                        adriftBook = new AdriftBook();
                    adriftBook.setAuthor(jsonObject.getString(AdriftBook.AUTHOR));
                    adriftBook.setBookId(jsonObject.getInt(AdriftBook.BOOK_ID));
                    adriftBook.setBookImageUrl(
                            jsonObject.optString(AdriftBook.BOOK_IMAGE_URL));
                    adriftBook.setBookName(
                            jsonObject.optString(AdriftBook.BOOK_NAME));
                    adriftBook.setRating(
                            (float) jsonObject.getDouble(AdriftBook.RATING));
                    adriftBook.setReviewPeopleCount(
                            jsonObject.getInt(AdriftBook.REVIEW_COUNT));
                    adriftBook.setType(jsonObject.getInt(AdriftBook.TYPE));
                    obj = adriftBook;
                    break;
                case User:
                    User user = new User();
                    user.setPassword(jsonObject.getString(User.PASSWORD));
                    user.setUserId(jsonObject.getInt(User.USER_ID));
                    user.setUserName(jsonObject.getString(User.USER_NAME));
                    user.setUserLevel(jsonObject.getInt(User.USER_LEVEL));
                    calendar.setTimeInMillis(jsonObject.getLong(User.REGISTER_DATE));
                    user.setRegisterDate(calendar);
                    obj = user;
                    break;
                case Post:
                    Post post = new Post();
                    post.setPostId(jsonObject.getInt(Post.POST_ID));
                    PostContent postContent = new PostContent(
                            jsonObject.getString(Post.POST_CONTENT));
                    post.setPostContent(postContent);
                    calendar.setTimeInMillis(jsonObject.getLong(Post.POST_DATE));
                    post.setPostDate(calendar);
                    post.setPostTitle(jsonObject.getString(Post.POST_TITLE));
                    post.setPostType(jsonObject.getInt(Post.POST_TYPE));
                    post.setReadCount(jsonObject.getInt(Post.READ_COUNT));
                    post.setPostUser((User) parseJsonEntity(EntityEnum.User,
                            jsonObject.getJSONObject("user")));
                    obj = post;
                    break;
                case Comment:
                    Comment comment = new Comment();
                    comment.setCommentContent(
                            jsonObject.optString(Comment.COMMENT_CONTENT));
                    calendar.setTimeInMillis(
                            jsonObject.getLong(Comment.REVIEW_DATE));
                    comment.setReviewDate(calendar);
                    comment.setCommentUser((User) parseJsonEntity(EntityEnum.User,
                            jsonObject.getJSONObject("user")));
                    obj = comment;
                    break;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return obj;
    }
}
