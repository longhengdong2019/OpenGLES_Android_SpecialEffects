package com.leroy.texiao.filter.camera;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.camera.face.Face;
import com.leroy.texiao.filter.base.MBaseFilter;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/*
 * 大眼特效
 */
public class BigEyeFilter extends MBaseFilter {

    private int left_eye;
    private int right_eye;
    private FloatBuffer left;
    private FloatBuffer right;
    private Face mFace;

    public BigEyeFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_bigeye);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        left_eye = GLES20.glGetUniformLocation(mProgram, "left_eye");
        right_eye = GLES20.glGetUniformLocation(mProgram, "right_eye");
        left = ByteBuffer.allocateDirect(2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        right = ByteBuffer.allocateDirect(2 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setFace(Face face){
        mFace = face;
    }

    protected float[] fbotexVertex = new float[]{
            0.5f, 0.5f,
            0f, 0f,
            1f, 0f,
            1f, 1.0f,
            0f, 1.0f,
    };

    @Override
    protected void initVertexBuffer() {
        TEX_VERTEX = fbotexVertex;
        super.initVertexBuffer();
    }

    /**
     * 绘制之前
     */
    @Override
    public boolean onDrawPre() {
        if (mFace == null) {
            return false;
        }
        return true;
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float[] landmarks = mFace.faceRects;
        // 左眼
        float x = landmarks[2] / mFace.imgWidth;
        float y = landmarks[3] / mFace.imgHeight;
        left.clear();
        left.put(x);
        left.put(y);
        left.position(0);
        GLES20.glUniform2fv(left_eye,1, left);

        // 右眼
        x = landmarks[4] / mFace.imgWidth;
        y = landmarks[5] / mFace.imgHeight;
        right.clear();
        right.put(x);
        right.put(y);
        right.position(0);
        GLES20.glUniform2fv(right_eye,1, right);
    }


}
