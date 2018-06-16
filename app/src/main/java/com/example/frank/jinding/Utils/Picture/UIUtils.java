package com.henau.pictureselect.utils;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by xxx on 2016/6/30.
 */
public class UIUtils {

    public static void lightOn(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    public static void lightOff(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        activity.getWindow().setAttributes(lp);
    }
}
