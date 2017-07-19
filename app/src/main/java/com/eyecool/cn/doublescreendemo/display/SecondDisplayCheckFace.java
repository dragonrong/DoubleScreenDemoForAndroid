package com.eyecool.cn.doublescreendemo.display;

import android.app.Presentation;
import android.content.Context;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.SurfaceHolder;

import com.eyecool.cn.doublescreendemo.Constants;
import com.eyecool.cn.doublescreendemo.R;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created date: 2017/7/7
 * Author:  Leslie
 */

public class SecondDisplayCheckFace extends Presentation implements GLSurfaceView.Renderer {

    private Camera mCamera;
    private int mCameraId = -1;
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mHandler.sendEmptyMessage(Constants.CAMERA_FRAME_CODE);
        }
    };

    private SurfaceHolder.Callback mCallback;
    private final Handler mHandler;

    public SecondDisplayCheckFace(Context outerContext, Display display, int cameraId, Handler handler) {
        super(outerContext, display);
        mCameraId = cameraId;
        mHandler = handler;
        mCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera = Camera.open();
                    //设置预览监听
                    mCamera.setPreviewDisplay(holder);
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters.setPreviewSize(640, 480);
                    parameters.set("orientation", "landscape");
                    mCamera.setDisplayOrientation(0);
                    parameters.setRotation(0);

                    mCamera.setParameters(parameters);
                    mCamera.setPreviewCallback(previewCallback);
                    //启动摄像头预览

                } catch (IOException e) {
                    e.printStackTrace();
                    mCamera.release();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                mCamera.startPreview();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                holder.removeCallback(this);

                if (mCamera != null) {
                    mCamera.setPreviewCallback(null);
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_face2);

        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glSurfaceceView);
        glSurfaceView.setRenderer(this);
        glSurfaceView.getHolder().addCallback(mCallback);
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
}
