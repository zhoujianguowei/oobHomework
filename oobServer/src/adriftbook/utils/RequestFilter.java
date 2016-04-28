package adriftbook.utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Created by Administrator on 2016/4/28.
 * if request params is illegal ,then the server will throw bdd params for response
 */
public class RequestFilter
{

    /**
     * judge whether request params legality as well as setting response headers
     * @param req
     * @param resp
     * @param legalRequestParamsSet
     * @return
     */
    public synchronized static boolean isRequestParamsLegal(HttpServletRequest req,
                                                            HttpServletResponse resp,
                                                            Set<String> legalRequestParamsSet)
    {
        int matchCount = 0;
        int paramSize = legalRequestParamsSet.size();
        resp.setHeader(Constant.HTTP_CONTENT_TYPE,
                "text/html;charset=" + Constant.DEFAULT_CODE);
        Enumeration<String> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements())
        {
            String param = enumeration.nextElement();
            if (legalRequestParamsSet.contains(param))
            {
                matchCount++;
                legalRequestParamsSet.remove(param);
            }
        }
        if (matchCount != paramSize)
        {
            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put(Constant.STATUS_KEY, Constant.FAIL_VALUE);
                jsonObject.put(Constant.INFO_KEY, "bad request params");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    resp.getOutputStream().write(jsonObject.toString()
                            .getBytes(Constant.DEFAULT_CODE));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return false;
        }
        return true;
    }
}
