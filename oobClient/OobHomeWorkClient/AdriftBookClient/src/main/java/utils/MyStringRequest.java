package utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;
/**
 * Created by Administrator on 2016/4/27.
 */
public class MyStringRequest extends StringRequest
{

    private Map<String, String> requestBody;
    private volatile boolean hasSetRequestQueue;
    public MyStringRequest(String url, Map<String, String> requestBody,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener)
    {
        this(url, listener, errorListener);
        this.requestBody = requestBody;
    }
    public MyStringRequest(String url,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener)
    {
        super(url, listener, errorListener);
    }
    public void setRequestBody(Map<String, String> requestBody)
    {
        if (hasSetRequestQueue)
            throw new IllegalStateException("the request has been added into queue");
        this.requestBody = requestBody;
    }
    public Request<?> setRequestQueue(RequestQueue requestQueue)
    {
//        mRequestQueue = requestQueue;
        super.setRequestQueue(requestQueue);
        hasSetRequestQueue = true;
        return this;
    }
    @Override protected Map<String, String> getParams() throws AuthFailureError
    {
//        return super.getParams();
//        if (getMethod() == Method.POST)
//        if (requestBody != null)
        return requestBody;
    }
}
