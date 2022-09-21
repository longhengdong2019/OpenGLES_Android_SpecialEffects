package com.leroy.texiao.filter.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import com.leroy.texiao.R;
import com.leroy.texiao.camera.face.Face;
import com.leroy.texiao.camera.utils.CameraGLUtil;
import com.leroy.texiao.filter.base.MBsImgFilter;


/*
 * 贴纸特效
 */
public class StickFilter extends MBsImgFilter {

    private static final String TAG = "StickFilter";
    private Face mFace;
    private Bitmap mBitmap;
    private int[] textureId2;

    protected float[] fbotexVertex = new float[]{
            0.5f, 0.5f,
            0f, 0f,
            1f, 0f,
            1f, 1.0f,
            0f, 1.0f,
    };

    public StickFilter(Context context) {
        super(context);
        mBitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.xiju_lianpu2);
    }

    @Override
    protected void initVertexBuffer() {
        TEX_VERTEX = fbotexVertex;
        super.initVertexBuffer();
    }

    public void setFace(Face face){
        mFace = face;
    }

    @Override
    public void onChange(int width, int height){
        super.onChange(width, height);
        if (textureId2 == null || textureId2[0] == 0) {
            textureId2 = new int[1];
            // 在GPU开辟一块内存 纹理ID 来加载 Bitmap
            CameraGLUtil.glGenTextures(textureId2);

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId2[0]);
            // 将Bitmap 与 纹理ID 绑定起来
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, mBitmap,0);
            mBitmap.recycle();
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        }
    }

    /**
     * 绘制之前
     */
    @Override
    public boolean onDrawPre() {
        if (mFace == null) {
            // Log.e(TAG, "------------------- ***************** 又来了 ***************** --------------------");
            return false;
        }
        return true;
    }

    /**
     * 绘制之后
     */
    @Override
    public void onDrawAfter() {
//        if (mFace != null) {
            onDrawStick();
//        }
    }

    private void onDrawStick() {
        // 开启混合模式，将图片进行混合
        GLES20.glEnable(GLES20.GL_BLEND);
        // GPU 一次性渲染 Bitmap 贴全部
        GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // mFace.faceRects face 宽度
        float x = mFace.faceRects[0] / mFace.imgWidth * width;
        float y = mFace.faceRects[1] / mFace.imgHeight * height;

//        GLES20.glViewport((int)x, (int)y - mBitmap.getHeight() / 2,
//                (int)((float)mFace.width / mFace.imgWidth * width),
//                mBitmap.getHeight()
//        );

        int faceWidth = (int)((float)mFace.width / mFace.imgWidth * width);
        int faceHeight = (int)((float)mFace.height / mFace.imgHeight * height);
        float xOffsetRatio = 0.06f;
        float yOffsetRatio = 0.16f;

        GLES20.glViewport((int)(x - faceWidth * xOffsetRatio), (int) (y - faceHeight * yOffsetRatio),
                (int) (faceWidth * (1 + 2 * xOffsetRatio)),
                (int) (faceHeight * (1 + 2 * yOffsetRatio))
        );


        // 设置显示窗口
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glUseProgram(mProgram);
        vertexBuffer.position(0);

        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId2[0]);
        GLES20.glUniform1i(uSamplerLocation,0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length, GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER,0);
    }

    /**
     * 是否启用混色
     * 在位移变换等特效时，要关闭混色
     */
    public boolean onEnableBlend() {
        return false;
    }


}
