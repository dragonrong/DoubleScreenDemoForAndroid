package com.eyecool.cn.doublescreendemo;


/**
 * Created date: 2017/6/14
 * Author:  Leslie
 */

public interface Constants {

    //监听是否读取到二代身份证的线程锁对象

    //设置界面数据存储文件名（不包括扩展名）
    String SETTING_DATA_SP_FILE = "setting_data";

    // SP 文件，key 名
    String SP_FILE_IP_ADDRESS_KEY_NAME = "server_ip_address";

    //IP 地址正则表达式
    String REGEX_IP_ADDRESS = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\" +
            ".((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\" +
            ".((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\" +
            ".((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b";

    //一下四个 一 CODE 为结尾的是给 handler 使用的

    //请重新放置身份证
    int READ_IDCARD_SUCCESS_CODE = 1000;
    int PLEASE_REPLACE_IDCARD_CODE = 1001;

    //读身份证失败
    int READ_ICDARD_ERROR_CODE = 1002;

    //身份证和现场的人是一个人
    int IDCARD_SCENEFACE_OK_CODE = 1003 ;

    //身份证和现场的人不是一个人
    int IDCARD_SCENEFACE_ERROR_CODE = 1004;

    //读取身份证的 3 种情况
    int READ_IDCARD_STATE_OK = 1;
    int READ_IDCARD_STATE_REPLACE = 2;
    int READ_IDCARD_STATE_ERROR = 3;

    int CAMERA_BACK_ID = 0;
    int CAMERA_FRONT_ID = 1;

    //截取人脸图片缓存个数
    int CACHE_CAPTURE_FACE_COUNT = 3;

    float CAPTURE_FACE_ZOOM_TIMES = 3F;

    //人脸比对分默认值
    int FACE_MATCH_SCORE_DEFAULT_VALUE = 70;

    //最大比对次数
    int FACE_MATCH_MAX_TIMES = 50;

    //以下是比对结果的 7 种结果

    int RESULT_STATE_LOGIN_OK = 1;
    int RESULT_STATE_UNBOOK = 2;
    int RESULT_STATE_UNLOGIN = 3;
    int RESULT_STATE_BLACK_LIST = 4;
    int RESULT_STATE_TEACHER_IO = 5;
    int RESULT_STATE_STU_IO = 6;
    int RESULT_STATE_REFUSE = 7;

    //未登记，即数据库中查不到此人
    int RESULT_STATE7 = 7;

    //访客类型
    int USER_TYPE_STU = 11;
    int USER_TYPE_PARENT = 22;
    int USER_TYPE_TEACHER = 33;
    int USER_TYPE_BLACKLIST = 44;
    int USER_TYPE_UNKNOWN = 55;
    int USER_TYPE_REFUSE = 66;

    //数据库名称
    String DATA_BASE_NAME = "VComVisitorSysInfo.db";



    //摄像头一帧
    int CAMERA_FRAME_CODE = 1001;

    //人脸框数据标记，从服务端拿到的
    int FACE_RECT_DATA = 1002;
    int FACE_SCORE_DATA = 1003;
    int FACE_CHECK_ERROR = 1004;
    int FACE_CHECK_SUCCESS = 1005;

    //改变 UI 的信号，服务端发送过来的
    int UI_MAIN_CODE = 1;
    int UI_CHECK_FACE_CODE = 2;
    int UI_CHECK_RESULT_CODE = 3;
}
