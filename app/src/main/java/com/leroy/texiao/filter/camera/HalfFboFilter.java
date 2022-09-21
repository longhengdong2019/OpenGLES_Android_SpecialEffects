package com.leroy.texiao.filter.camera;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;


/*
 * 一半Fbo效果处理(蓝线)
 */
public class HalfFboFilter extends MBaseFilter {

    private int vTexture2Location;
    private int uOffsetLocation;
    private int uTypeLocation;
    private int type;

    private float progress;

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public HalfFboFilter(Context context, int type) {
        super(context, R.raw.vertex_base, R.raw.frag_blueline);
        this.type = type;
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

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        vTexture2Location = GLES20.glGetUniformLocation(mProgram, "vTexture2");
        uOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uOffset");
        uTypeLocation = GLES20.glGetUniformLocation(mProgram, "type");
    }

    @Override
    public boolean onEnableBlend() {
        return false;
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        // 要与蓝线保持一致
        float offset = progress;

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureId);
        GLES20.glUniform1i(vTexture2Location, 1);
        GLES20.glUniform1f(uOffsetLocation, offset);
        GLES20.glUniform1i(uTypeLocation, type);
    }



}
