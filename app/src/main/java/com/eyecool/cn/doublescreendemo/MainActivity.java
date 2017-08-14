package com.eyecool.cn.doublescreendemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.eyecool.cn.doublescreendemo.utils.DisplayUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Leslie
 * @date 2017/6/21
 * @description Android 实现双屏异显 参考地址：http://blog.csdn.net/wlwl0071986/article/details/48542923
 * 主要类：Presentation、DisplayManager、Display
 * <p>
 * 号外号外:实现了在第二个 display 显示相机视频，然后第一个 display 利用相机的回调，把 yuv 数据处理成 bitmap，然后达到视频效果！！！
 */
public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.fl_video_container)
    FrameLayout flVideoContainer;
    private static final int YUV_2_BITMAP_SUCCESS = 2017;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case YUV_2_BITMAP_SUCCESS:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DisplayUtil.showOutDisplayMain(this, Constants.UI_CHECK_FACE_CODE, Constants.CAMERA_BACK_ID, mHandler);

    }
}
