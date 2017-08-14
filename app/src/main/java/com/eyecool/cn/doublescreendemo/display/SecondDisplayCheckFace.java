package com.eyecool.cn.doublescreendemo.display;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;

import com.eyecool.cn.doublescreendemo.R;

import java.io.ByteArrayOutputStream;
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
    private static final int YUV_2_BITMAP_SUCCESS = 2017;
    public Bitmap dstbmp;



    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            yuv2Bitmap(data,camera);
//            mHandler.sendEmptyMessage(Constants.CAMERA_FRAME_CODE);
        }
    };

    public void yuv2Bitmap(byte[] data, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();

        try {
            YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);

                Bitmap bmp = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                stream.close();

                Canvas canvas = new Canvas();
                // 设置canvas画布背景为白色
                canvas.drawColor(Color.BLACK);
                // 在画布上绘制缩放之前的位图，以做对比
                //屏幕上的位置坐标是0,0
                canvas.drawBitmap(bmp, 0, 0, null);
                // 定义矩阵对象
                Matrix matrix = new Matrix();
                // 缩放原图
                matrix.postScale(1f, 1f);
                // 向左旋转45度，参数为正则向右旋转,如果是横屏的话，就不需要转了。

                //bmp.getWidth(), 500分别表示重绘后的位图宽高
                dstbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                // 在画布上绘制旋转后的位图
                //放在坐标为0,200的位置
                canvas.drawBitmap(dstbmp, 0, 200, null);
                //把一帧图片发送给主线程
                Message message = mHandler.obtainMessage();
                message.obj = dstbmp;
                message.what = YUV_2_BITMAP_SUCCESS;
                mHandler.sendMessage(message);


            }
        } catch (Exception ex) {
            Log.e("Sys", "Error:" + ex.getMessage());
        }
    }

    private SurfaceHolder.Callback mCallback;
    private final Handler mHandler;

    public SecondDisplayCheckFace(Context outerContext, Display display, final int cameraId, Handler handler) {
        super(outerContext, display);
        mCameraId = cameraId;
        mHandler = handler;
        mCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera = Camera.open(cameraId);
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
