package com.example.adriftbookclient.oobhomeworkclient;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.klicen.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adriftbook.entity.EntityEnum;
import adriftbook.entity.Post;
import utils.JsonEntityParser;
import utils.MyStringRequest;
/**
 * Created by Administrator on 2016/5/1.
 */
public class LoadEntityUtils
{

    private Context context;
    private Handler handler;
    private Bundle bundle;
    public LoadEntityUtils(Context context, Handler handler)
    {
        this.context = context;
        this.handler = handler;
        bundle = new Bundle();
    }
    public void loadPosts(int requestBookType, int sendBookType, int ebookType,
                          int page, String tag)
    {
        String loadPostsUrl = Constant.CONSTANT_IP +
                "get_post?requestbooktype=" + requestBookType + "&sendbooktype=" +
                sendBookType + "&ebooktype=" + ebookType + "&page=" + page;
        if (tag != null && tag.trim().length() > 0)
            loadPostsUrl += "&tag=" + tag;
        RequestQueue queue = Volley.newRequestQueue(context);
        MyStringRequest loadPostsRequest = new MyStringRequest(loadPostsUrl,
                new Response.Listener<String>()
                {
                    @Override public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject resJObj = new JSONObject(response);
                            if (resJObj.getString(Constant.STATUS_KEY)
                                    .equals(Constant.FAIL_VALUE))
                            {
                                bundle.putString(Constant.STATUS_KEY,
                                        Constant.FAIL_VALUE);
                                bundle.putString(Constant.INFO_KEY, "服务器内部错误");
                            } else
                            {
                                bundle.putString(Constant.STATUS_KEY,
                                        Constant.SUCCESS_VALUE);
                                bundle.putInt(Post.POSTS_COUNT_KEY,
                                        resJObj.getInt(Post.POSTS_COUNT_KEY));
                                JSONArray postsJArray = resJObj
                                        .getJSONArray(Constant.INFO_KEY);
                                ArrayList<Post> posts = new ArrayList<>();
                                for (int i = 0; i < postsJArray.length(); i++)
                                {
                                    JSONObject postJObj = postsJArray
                                            .getJSONObject(i);
                                    posts.add((Post) JsonEntityParser
                                            .getSingleInstance()
                                            .parseJsonEntity(EntityEnum.Post,
                                                    postJObj));
                                }
                                bundle.putInt(Post.POSTS_COUNT_KEY,
                                        resJObj.getInt(Post.POSTS_COUNT_KEY));
                                bundle.putSerializable(Post.POSTS_KEY, posts);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            Message msg = Message.obtain();
                            msg.obj = bundle;
                            handler.sendMessage(msg);
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override public void onErrorResponse(VolleyError error)
            {
                bundle.putString(Constant.STATUS_KEY, Constant.FAIL_VALUE);
                bundle.putString(Constant.INFO_KEY, error.getMessage());
                Message msg = Message.obtain();
                msg.obj = bundle;
                handler.sendMessage(msg);
            }
        });
        queue.add(loadPostsRequest);
    }
}
