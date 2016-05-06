package com.example.adriftbookclient.oobhomeworkclient;
import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;

import com.klicen.navigationbar.BackStackFragment;
/**
 * Created by Administrator on 2016/4/27.
 */
public class BackStackFragmentWithProgressDialog extends BackStackFragment
{

    private ProgressDialog progressDialog;
    @Override public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (getTag() == null)
            throw new IllegalArgumentException("必须拥有tag");
    }
    /**
     * show a progress dialog with the given message.
     */
    @Override public void onHomeClick()
    {
//        super.onHomeClick();
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0)
        {
           /* if (getActivity() instanceof SupActivityHandleFragment)
            {
                SupActivityHandleFragment activityHandleFragment = (SupActivityHandleFragment) getActivity();
                activityHandleFragment.popFragmentTag();
            }*/
            fragmentManager.popBackStackImmediate();
        } else
        {
            getActivity().finish();
        }
    }
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
        progressDialog.setMessage(msg);
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
