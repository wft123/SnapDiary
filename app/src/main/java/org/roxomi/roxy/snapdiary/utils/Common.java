package org.roxomi.roxy.snapdiary.utils;

import android.content.Context;

/**
 * Created by Roxy on 2017-04-18.
 */
public class Common {
    public static float dp2px(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
