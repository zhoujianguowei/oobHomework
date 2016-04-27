package com.klicen.navigationbar;


import android.support.v4.app.Fragment;


/**
 * This Fragment can handle the back key pressed event.To allow this The Activity which
 * attach the Fragment should propagate the event to the one,for example,the Activity can
 * override the onKeyDown function,like:
 * <p/>
 * <pre>
 * <code>
 * public boolean onKeyDown(int keyCode, KeyEvent event) {
 *     if (keyCode == KeyEvent.KEYCODE_BACK) {
 *          if (((BackPressHandleFragment) fragmentManager
 *              .findFragmentById(R.id.activity_login_fl_container))
 *              .onBackPressed()) {
 *              return true;
 *          }
 *     }
 *     return super.onKeyDown(keyCode, event);
 * }
 * </code>
 * </pre>
 */
public abstract class BackPressHandleFragment extends Fragment {

    protected BackPressHandleFragment() {
    }

    /**
     * @return true if the Fragment consume the event.
     */
    public boolean onBackPressed() {
        return false;
    }
}
