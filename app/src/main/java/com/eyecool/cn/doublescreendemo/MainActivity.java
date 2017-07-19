package com.eyecool.cn.doublescreendemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.eyecool.cn.doublescreendemo.utils.DisplayUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Leslie
 * @date 2017/6/21
 * @description Android 实现双屏异显 参考地址：http://blog.csdn.net/wlwl0071986/article/details/48542923
 * 主要类：Presentation、DisplayManager、Display
 *
 * 号外号外:实现了在第二个 display 显示相机视频，然后第一个 display 利用相机的回调，把 yuv 数据处理成 bitmap，然后达到视频效果！！！
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.fl_video_container)
    FrameLayout flVideoContainer;
    private Camera camera;
    private static final int YUV_2_BITMAP_SUCCESS = 2017;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case YUV_2_BITMAP_SUCCESS:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };

    //相机的预览回调
    private Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public Bitmap dstbmp;
        public int totalObtainFrameTimes = 0;
        public long totalObtainFrameUsedTime = 0;
        public long currentTimeMillis = 0;

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            yuv2Bitmap(data, camera);
        }

        private void yuv2Bitmap(byte[] data, Camera camera) {
            Camera.Size size = camera.getParameters().getPreviewSize();
            long callBackUsedTimes;

            if (currentTimeMillis != 0) {
                callBackUsedTimes = System.currentTimeMillis() - currentTimeMillis;
                totalObtainFrameUsedTime += callBackUsedTimes;
                totalObtainFrameTimes++;
                long averageUsedTime = (totalObtainFrameUsedTime / totalObtainFrameTimes);
                Log.d(TAG, "onPreviewFrame: -----------------------------最新回调之间间隔：" + callBackUsedTimes + "ms");
                Log.d(TAG, "onPreviewFrame: -----------------------------回调之间平均间隔：" + averageUsedTime + "ms");
            }
            currentTimeMillis = System.currentTimeMillis();
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

                    //转化完摄像头的图像数据之后，要1.画人脸框;2.截取人脸图片，存到集合里面去
                    //由于识别人脸比较耗时，所以用监听把要识别的图片发送到外面去

                    /*if (SsDuckFaceRecognition.drawFaceFrame(mParentView, mSurfaceView, dstbmp)) {

                        float[] faceLoc = new float[3];

                        faceLoc[0] = SsDuckFaceRecognition.faceRect[0];
                        faceLoc[1] = SsDuckFaceRecognition.faceRect[1];
                        faceLoc[2] = SsDuckFaceRecognition.faceRect[2];

                        //1.从截图中截取含有人脸的图片，如果摄像头设置成镜像的话，就需要对图片做镜像处理。。。。。。。。。。。。。。，然后再转换成base64
                        Bitmap cropBitmap = BitmapUtil.cropBitmap(dstbmp, faceLoc, Constant.CAPTURE_FACE_ZOOM_TIMES);

                        //1.5对图片镜像处理,如果是用 USB 摄像头，就不需要对图片做镜像处理了
                        Bitmap mirrorPic = PictureUtil.mirrorPic(cropBitmap);
                        if (captureListener != null) {

                            captureListener.captureFace(mirrorPic, faceLoc);
                        }
                        App.getInstance().setCurrentCaptureFaceImg(mirrorPic);
                        if (iv != null) {

                            iv.setImageBitmap(cropBitmap);
                        }

                    }*/

                    //TODO：此处可以对位图进行处理，如显示，保存等

                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }
    };


    //这个回调给外屏的控件 surfaceview2 使用
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
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
                camera.setPreviewCallback(previewCallback);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        DisplayUtil.showOutScreenCheckFace(this);


    }

}
