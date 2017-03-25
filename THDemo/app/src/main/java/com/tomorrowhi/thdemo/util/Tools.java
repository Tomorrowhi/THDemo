package com.tomorrowhi.thdemo.util;

import android.graphics.Bitmap;
import android.view.View;


/**
 * Created by zhaotaotao on 2017/3/25.
 */

public class Tools {

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
