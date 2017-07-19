# DoubleScreenDemoForAndroid
这是一个 单个应用在有两个屏幕的 Android 设备上显示不同 UI 的事例

1.MainActivity 参考地址：http://blog.csdn.net/wlwl0071986/article/details/48542923

        *   经过尝试 实现了在第二个 display 显示相机视频，然后第一个 display 利用相机的回调，把 yuv 数据处理成 bitmap，然后达到视频效果！！！

    2.PresentationWithMediaRouterActivity  在副屏幕上显示 surfaceView 绘制的魔方动画，这个是 AndroidStudio 里面，Google 官方提供的例子。
