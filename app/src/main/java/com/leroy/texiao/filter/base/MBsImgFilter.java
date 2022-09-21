package com.leroy.texiao.filter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;

import com.leroy.texiao.camera.utils.CameraGLUtil;
import com.leroy.texiao.utils.GLUtil;


public class MBsImgFilter extends MBaseFilter{

    private static final String TAG = "MBsImgFilter";

    private int imgSourceId;
    private String fileName;

    public MBsImgFilter(Context context) {
        super(context);
    }

    /**
     * 是否启用混色
     * 在位移变换等特效时，要关闭混色
     */
    public boolean onEnableBlend() {
        return false;
    }

    /**
     * 激活
     */
    @Override
    protected void onActiveTexture(int textureId) {
        super.onActiveTexture(textureId);
        GLES20.glUniform1i(uSamplerLocation, 0);
    }

    /**
     * 初始化纹理图片
     */
    @Override
    protected boolean initTextureId() {
        // 不处理会出现内存问题
        if (textureId != 0) {
            return true;
        }

        if (imgSourceId > 0) {
            textureId = CameraGLUtil.loadImgTexture(context, imgSourceId);
            return true;
        } else if (!TextUtils.isEmpty(fileName)) {
            Bitmap bitmap = GLUtil.getBitmapFromAssets(context, fileName);
            if (bitmap == null) {
                return false;
            }
            imgW = bitmap.getWidth();
            imgH = bitmap.getHeight();

            textureId = GLUtil.getBitmapTexture(bitmap);
            return true;
        } else {
            Log.e(TAG, "请设置图片资源");
            return false;
        }
    }

    public void setImgSourceId(int imgSourceId) {
        this.imgSourceId = imgSourceId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
