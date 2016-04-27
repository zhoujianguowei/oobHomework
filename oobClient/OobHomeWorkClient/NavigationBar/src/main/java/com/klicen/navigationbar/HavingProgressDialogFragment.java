package com.klicen.navigationbar;

import android.app.ProgressDialog;

/**
 * Created on 15-4-16.
 */
public abstract class HavingProgressDialogFragment extends AccessNavigationBarFragment {
    private ProgressDialog progressDialog;

    /**
     * show a progress dialog with the given message.
     */
    public void showProgressDialog(String msg) {
        if (null == progressDialog) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
            progressDialog.setMessage(msg);
        }
    }

    /**
     * close a progress if there is one.
     */
    public void dismissProgressDialog() {
        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
