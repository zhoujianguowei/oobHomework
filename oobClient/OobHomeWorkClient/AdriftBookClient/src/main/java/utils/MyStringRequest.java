package utils;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Map;
/**
 * Created by Administrator on 2016/4/27.
 */
public class MyStringRequest extends StringRequest
{

    private Map<String, String> requestBody;
    private volatile boolean hasSetRequestQueue;
    public MyStringRequest(int methodType, String url,
                           Map<String, String> requestBody,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener)
    {
        super(methodType, url, listener, errorListener);
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
        if (hasSetRequestQueue || getMethod() != Method.POST)
            throw new IllegalStateException(
                    "the request has been added into queue or it's not a  post request");
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
    /**
     * 自定义上传的byte[]数组流，如果使用Request的getBody方法，
     * 传统的是使用URLEncoder进行EncodeParams的。在Servlet服务端
     * 会对post内容进行修改，所以需要覆写。同时过滤掉=和&
     * @return
     * @throws AuthFailureError
     */
    @Override public byte[] getBody() throws AuthFailureError
    {
        Map<String, String> params = getParams();
        if (params != null && params.size() > 0)
        {
            return encodeParameters(params, getParamsEncoding());
        }
        return null;
    }
    private String filterKeywords(String param)
    {
        HashSet<String> keywordSet = new HashSet<>();
        keywordSet.add("=");
        keywordSet.add("&");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < param.length(); i++)
        {
            String s = param.substring(i, i + 1);
            if (keywordSet.contains(s))
                continue;
            builder.append(s);
        }
        return builder.toString();
    }
    private byte[] encodeParameters(Map<String, String> params,
                                    String paramsEncoding)
    {
        StringBuilder encodedParams = new StringBuilder();
        try
        {
            //不要使用URLEncoder否则服务端就会收到乱码
            for (Map.Entry<String, String> entry : params.entrySet())
            {
                encodedParams.append(filterKeywords(entry.getKey()));
                encodedParams.append('=');
                encodedParams.append(filterKeywords(entry.getValue()));
                encodedParams.append('&');
            }
            String resParams = encodedParams.toString();
            if (resParams.endsWith("&"))
                resParams = resParams.substring(0, resParams.length() - 1);
            return resParams.getBytes(paramsEncoding);
        }
        catch (UnsupportedEncodingException uee)
        {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding,
                    uee);
        }
    }
}
