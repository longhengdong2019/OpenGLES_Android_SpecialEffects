package com.leroy.texiao;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.leroy.texiao.camera.face.Face;
import com.leroy.texiao.camera.utils.CameraHelper;


public class OpencvJni {

    private Face mFace;
    private static final int CHAECK_FACE = 101;
    private long self;

    static {
        System.loadLibrary("native-lib");
    }

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    public OpencvJni(String path, String seeta, CameraHelper cameraHelper) {
        self = init(path, seeta);
        mHandlerThread = new HandlerThread("track");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // 子线程
                mFace = native_detector(self, (byte[])msg.obj,
                        cameraHelper.getCameraId(),CameraHelper.WIDTH, CameraHelper.HEIGHT);
            }
        };
    }

    public void startTrack(){
        native_start(self);
    }

    // 检测人脸 耗时操作，子线程来进行检测
    public void detector(byte[] data) {
        // 先把之前的消息移除掉 防止检测旧的数据，只检测新的
        mHandler.removeMessages(CHAECK_FACE);
        // 加入新的任务
        Message message = mHandler.obtainMessage(CHAECK_FACE);
        message.obj = data;
        mHandler.sendMessage(message);
    }

    public Face getFace(){
        return mFace;
    }

    /**
     * 初始化
     *
     * @param path
     *
     */
    public native long init(String path, String seeta);

    public native void postData(byte[] data, int width, int height, int cameraId);

    public native void setSurface(Surface surface);

    private native void native_start(long self);

    private native Face native_detector(long self, byte[] obj, int cameraId, int width, int height);

}
