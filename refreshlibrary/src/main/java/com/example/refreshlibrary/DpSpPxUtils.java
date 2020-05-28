package com.example.refreshlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DpSpPxUtils {
    /**
     * convert dp to its equivalent px
     */
    public static int dp2px(int dp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * convert sp to its equivalent px
     */
    public static int sp2px(int sp, Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取屏幕的宽度（app显示区域）
     * */
    public static int getContentWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（app显示区域）
     * */
    public static int getContentHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    public static int getContentHeight1(Context context){
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        if(Build.VERSION.SDK_INT < 17) {
            display.getSize(point);
        } else {
            display.getRealSize(point);
        }

        int width = point.x;
        Log.e("getContentHeight1:::",width+"");
        int height = point.y;
        Log.e("getContentHeight1:::", height+"");
        return height;
    }

    public static int getContentHeight2(Activity context){
        DisplayMetrics metrics = new DisplayMetrics();
        Display display = context.getWindowManager().getDefaultDisplay();
        if(Build.VERSION.SDK_INT < 17) {
            display.getMetrics(metrics);
        } else {
            display.getRealMetrics(metrics);
        }

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Log.e("getContentHeight2:::", width+"");
        Log.e("getContentHeight2:::", height+"");
        return height;
    }
}
