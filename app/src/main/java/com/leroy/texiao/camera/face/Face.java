package com.leroy.texiao.camera.face;

import android.util.Log;

// native发射生成， jni发射技术
// 不要轻易更改包名 (native-lib.cpp)
public class Face {
    public float[] faceRects;
    // 保持人脸的宽、高
    public int width;
    public int height;
    // 送去检测图片的宽、高
    public int imgWidth;
    public int imgHeight;

    public Face(int width, int height, int imgWidth, int imgHeight, float[] faceRects) {
        this.width = width;
        this.height = height;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.faceRects = faceRects;
        Log.e("leroy", "Face:" + toString());
    }
}
