package com.eyecool.cn.doublescreendemo;

import android.view.Surface;

/**
 * Created date: 2017/7/6
 * Author:  Leslie
 */

public class ShowYUV {

    static {
        System.loadLibrary("showYUV");
    }

    public native boolean nativeSetVideoSurface(Surface surface);
    public native void nativeShowYUV(byte[] yuvArray,int width,int height);

}
