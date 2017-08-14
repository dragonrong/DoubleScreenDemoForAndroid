package com.eyecool.cn.doublescreendemo.utils;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import com.eyecool.cn.doublescreendemo.Constants;
import com.eyecool.cn.doublescreendemo.display.SecondDisplay;
import com.eyecool.cn.doublescreendemo.display.SecondDisplayCheckFace;


/**
 * Created date: 2017/7/7
 * Author:  Leslie
 * 显示设备控制工具类
 */

public class DisplayUtil {

    /**
     * 展示外屏的待机页面，内屏首页、查询页、设置页开启的时候，都是调用这个方法
     *
     * @param context
     */
    public static Presentation showOutDisplayMain(Context context, int uiId, int cameraId, Handler handler) {
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display display1 = mDisplayManager.getDisplay(1);           //获得副屏幕

        Presentation display = null;
        //开启副屏幕
        switch (uiId) {
            case Constants.UI_MAIN_CODE:
//                 display = new SecondDisplay(context, display1,);
                break;
            case Constants.UI_CHECK_FACE_CODE:
                display = new SecondDisplayCheckFace(context,display1,cameraId,handler);
                break;
        }
        display.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        display.show();
        return display;
    }

    public static void showOutScreen(Context context){
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display display1 = mDisplayManager.getDisplay(1);           //获得副屏幕


        Presentation display = new SecondDisplay(context,display1,null);
        display.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        display.show();


    }

    public static void showOutScreenCheckFace(Context context){
        DisplayManager mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display display1 = mDisplayManager.getDisplay(1);           //获得副屏幕


        Presentation display = new SecondDisplayCheckFace(context,display1,0,null);
        display.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        display.show();
    }
}
