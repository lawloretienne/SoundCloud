package com.sample.soundcloud.utilities;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by etiennelawlor on 4/14/15.
 */
public class SoundcloudUtility {

    // reigon Utility Methods
    public static int dp2px(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);

        return (int) (px / displaymetrics.density + 0.5f);
    }
    // endregion
}
