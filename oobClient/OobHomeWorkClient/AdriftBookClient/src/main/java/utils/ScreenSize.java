package utils;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
/**
 * Created by Administrator on 2015/4/19.
 */
public class ScreenSize
{

    private static Context context;
    private static DisplayMetrics metrics;
    private static boolean isInitialed = false;
    public static void initial(Context context)
    {
        if (isInitialed)
        {
            return;
        }
        ScreenSize.context = context;
        metrics = new DisplayMetrics();
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context
                        .WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }
    public static int getScreenWidth()
    {
        if (context == null)
        {
            throw new IllegalArgumentException("context hasn't been " +
                    "initialized");
        }
        return metrics.widthPixels;
    }
    public static int getScreenHeight()
    {
        if (context == null)
        {
            throw new IllegalArgumentException("context hasn't been " +
                    "initialized");
        }
        return metrics.heightPixels;
    }
    /**
     * 手机屏幕像素密度，可以用于dp和sp之间的转换
     * @return
     */
    public static float getScreenDensity()
    {
        if (context == null)
        {
            throw new IllegalArgumentException("context hasn't been " +
                    "initialized");
        }
        return metrics.density;
    }
    /**
     * 字体大小缩放密度
     * @return
     */
    public static float getScreenFontScale()
    {
        if (context == null)
        {
            throw new IllegalArgumentException("context hasn't been " +
                    "initialized");
        }
        return metrics.scaledDensity;
    }
}
