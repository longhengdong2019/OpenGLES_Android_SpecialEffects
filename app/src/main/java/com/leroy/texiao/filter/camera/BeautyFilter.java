package com.leroy.texiao.filter.camera;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 美颜特效
 */
public class BeautyFilter extends MBaseFilter {

    private int widthLocation;
    private int heightLocation;

    public BeautyFilter(Context context) {
        super(context, R.raw.vertex_beauty, R.raw.frag_beauty);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        widthLocation = GLES20.glGetUniformLocation(mProgram, "width");
        heightLocation = GLES20.glGetUniformLocation(mProgram, "height");
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
    public boolean onEnableBlend() {
        return true;
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform1i(widthLocation, width);
        GLES20.glUniform1i(heightLocation, height);
    }


}
