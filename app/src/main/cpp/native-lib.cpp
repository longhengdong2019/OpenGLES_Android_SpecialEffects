#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include <android/native_window_jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include "FaceTrack.h"


#define TAG "JNI_TAG"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

// 使用命名空间
using namespace cv;
using namespace std;

DetectionBasedTracker *tracker = 0;
ANativeWindow *window = 0;


extern "C"
JNIEXPORT jlong JNICALL
Java_com_leroy_texiao_OpencvJni_init(JNIEnv *env, jobject instance, jstring path_, jstring seeta_) {
    const char *path = env->GetStringUTFChars(path_,0);
    const char *seeta = env->GetStringUTFChars(seeta_,0);

    // 初始化检测器 和 跟踪器
    FaceTrack *faceTrack = new FaceTrack(path, seeta);

    env->ReleaseStringUTFChars(path_, path);
    env->ReleaseStringUTFChars(seeta_, seeta);

    return reinterpret_cast<jlong>(faceTrack);
}

int i = 0;

extern "C"
JNIEXPORT void JNICALL
Java_com_leroy_texiao_OpencvJni_postData(JNIEnv *env, jobject instance, jbyteArray data_,
                                         jint w, jint h, jint cameraId) {
    // data[NV21] NV21 -> Bitmap - Mat
    jbyte *data = env->GetByteArrayElements(data_, NULL);
    // src = bitmap
    Mat src(h + h / 2, w, CV_8UC1, data);
    // 转化为RGBA，第一个src: 输入，第二个src: 复制给谁
    cvtColor(src, src,COLOR_YUV2RGBA_NV21);
//    bool rst1 = cv::imwrite("/sdcard/opencv/src.jpg", src);

    if (cameraId == 1) {
        // 前置摄像头
        rotate(src,src,ROTATE_90_COUNTERCLOCKWISE);
//        bool rst2 = cv::imwrite("/sdcard/opencv/src2.jpg",src);
        // 1 水平翻转 镜像 0 垂直翻转
        flip(src, src, 1);
//        bool rst3 = cv::imwrite("/sdcard/opencv/src3.jpg",src);
    } else {
        rotate(src, src, ROTATE_90_CLOCKWISE);
    }

    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);
//    bool rst4 = cv::imwrite("/sdcard/opencv/src4.jpg",gray);
    // 对比度 黑白 轮廓 二值化
    equalizeHist(gray, gray);
//    bool rst5 = cv::imwrite("/sdcard/opencv/src5.jpg",gray);

    // 检测人脸
    std::vector<Rect> faces;
    tracker->process(gray);
    tracker->getObjects(faces);

    LOGE("检测到人脸");

    for (Rect face : faces) {
        rectangle(src, face, Scalar(255,0,255));

        LOGE("检测到人脸: %d", 111);

        // 训练数据源
//        Mat m;
//        src(face).copyTo(m);
//        resize(m,m,Size(24,24));
//        cvtColor(m,m,COLOR_BGR2GRAY);
//        char p[100];
//        sprintf(p,"/sdcard/opencv/%d.jpg", i++);
//        LOGE("检测到人脸: %s", p);
//        int suc = cv::imwrite(p, m);
//        LOGE("------------------ suc: %d", suc);
    }

    // src Mat类型
    if (window){
        ANativeWindow_setBuffersGeometry(window,src.cols,src.rows,WINDOW_FORMAT_RGBA_8888);
        ANativeWindow_Buffer buffer;
        do {
            // lock失败，直接break出去
            if (ANativeWindow_lock(window, &buffer,0)) {
                ANativeWindow_release(window);
                window = 0;
                break;
            }
            // src.data : rgba的数据
            // 把src.data 一行一行的拷贝到 buffer.bits 里去
            // 填充rgb数据给dst_data（一行一行拷贝）
            uint8_t *dst_data = static_cast<uint8_t *>(buffer.bits);
            // stride : 一行多少个数据 （RGBA） * 4
            int dst_line_size = buffer.stride * 4;

            // 一行一行拷贝
            for (int i = 0; i < buffer.height; ++i) {
                // void *memcpy(void *dest, const void *src, size_t n);
                // 从源src所指的内存地址的起始位置开始拷贝n个字节到目标dest所指的内存地址的起始位置中
                memcpy(dst_data + i * dst_line_size,
                       src.data + i * src.cols * 4, dst_line_size);
            }

            // 提交刷新
            ANativeWindow_unlockAndPost(window);
        } while (0);
    }

    src.release();
    gray.release();
    env->ReleaseByteArrayElements(data_, data,0);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_leroy_texiao_OpencvJni_setSurface(JNIEnv *env, jobject instance, jobject surface) {
    if (window){
        ANativeWindow_release(window);
        window = 0;
    }
    window = ANativeWindow_fromSurface(env, surface);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_leroy_texiao_OpencvJni_native_1start(JNIEnv *env, jobject instance, jlong self) {
    if (self == 0) {
        return;
    }
    FaceTrack *me = reinterpret_cast<FaceTrack *>(self);
    me->startTracking();
}

// 人眼位置
extern "C"
JNIEXPORT jobject JNICALL
Java_com_leroy_texiao_OpencvJni_native_1detector(JNIEnv *env, jobject instance, jlong self,
                                                 jbyteArray obj_, jint cameraId, jint w,
                                                 jint h) {
    if (self == 0) {
        return NULL;
    }
    jbyte *data = env->GetByteArrayElements(obj_,NULL);
    // 强转
    FaceTrack *me = (FaceTrack *)self;

    // src = bitmap
    Mat src(h + h / 2, w, CV_8UC1, data);
    // 转化为RGBA，第一个src: 输入，第二个src: 复制给谁
    cvtColor(src, src,COLOR_YUV2RGBA_NV21);

    if (cameraId == 1) {
        // 前置摄像头
        rotate(src,src,ROTATE_90_COUNTERCLOCKWISE);
        // 1 水平翻转 镜像 0 垂直翻转
        flip(src, src, 1);
    } else {
        rotate(src, src, ROTATE_90_CLOCKWISE);
    }

    Mat gray;
    cvtColor(src, gray, COLOR_RGBA2GRAY);
    // 对比度 黑白 轮廓 二值化
    equalizeHist(gray, gray);

    // 检测 src - RGBA
    vector<Rect2f> rects = me->detector(gray);

    env->ReleaseByteArrayElements(obj_, data,0);

    // Face对象
    w = src.cols;
    h = src.rows;
    int ret = rects.size();
    if (ret) {
        // 反射
        // 先通过类路径获取class
        jclass clazz = env->FindClass("com/leroy/texiao/camera/face/Face");
        // 再获取方法。
        // <init> 构造方法
        // (IIII[F)V 函数参数键名 第 1 - 4 个都是int类型所以为 IIII，后面是一个float数组. F代表float，由于是数组所以用[F. 最后一个V代表void类型
        jmethodID costruct = env->GetMethodID(clazz, "<init>","(IIII[F)V");

        int size = ret * 2;
        // 创建java的float数组
        jfloatArray floatArray = env->NewFloatArray(size);
        for (int i = 0, j = 0; i < size; j++) {
            float f[2] = {rects[j].x, rects[j].y};
            env->SetFloatArrayRegion(floatArray, i,2, f);
            i += 2;
        }
        Rect2f faceRect = rects[0];
        int width = faceRect.width;
        int heigt = faceRect.height;
        jobject face = env->NewObject(clazz, costruct, width, heigt, w, h, floatArray);

        return face;
    }

    return NULL;
}