package com.eyecool.cn.doublescreendemo.display;

import android.app.Presentation;
import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.eyecool.cn.doublescreendemo.R;

import java.io.IOException;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created date: 2017/7/7
 * Author:  Leslie
 */

public class SecondDisplay extends Presentation implements GLSurfaceView.Renderer {
    private static final String TAG = "SecondDisplay";
    private Camera camera;
    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open(1);
            try {
                //设置预览监听
                camera.setPreviewDisplay(holder);
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(640, 480);
//                setCameraParams(1080,1920);


                parameters.set("orientation", "landscape");
                camera.setDisplayOrientation(0);
                parameters.setRotation(0);

                List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
                for (Camera.Size pictureSize : pictureSizes) {
                    int height = pictureSize.height;
                    int width = pictureSize.width;
                }
                camera.setParameters(parameters);
//                camera.setPreviewCallback(previewCallback);
                //启动摄像头预览

            } catch (IOException e) {
                e.printStackTrace();
                camera.release();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            holder.removeCallback(this);

            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    };
    private GLSurfaceView surfaceView2;
//        private Camera camera;

    public SecondDisplay(Context outerContext, Display display, SurfaceHolder.Callback callback) {
        super(outerContext, display);
//        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        surfaceView2 = (GLSurfaceView) findViewById(R.id.surfaceView2);
        surfaceView2.setRenderer(this);
        SurfaceHolder holder = surfaceView2.getHolder();
        holder.addCallback(mCallback);
    }

    public SurfaceView getSurfaceView2() {
        return surfaceView2;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, @Nullable Menu menu, int deviceId) {

    }
}
