package com.klicen.navigationbar;
import android.app.ProgressDialog;
/**
 * Created by Administrator on 2016/4/27.
 */
public class BackStackFragmentWithProgressDialog extends BackStackFragment
{

    private ProgressDialog progressDialog;
    /**
     * show a progress dialog with the given message.
     */
    public void showProgressDialog(String msg)
    {
        if (null == progressDialog)
        {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        if (!progressDialog.isShowing())
        {
            progressDialog.show();
            progressDialog.setMessage(msg);
        }
    }
    /**
     * close a progress if there is one.
     */
    public void dismissProgressDialog()
    {
        if (null != progressDialog && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }
    }
}
