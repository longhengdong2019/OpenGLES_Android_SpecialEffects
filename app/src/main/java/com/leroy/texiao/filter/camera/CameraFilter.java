package com.leroy.texiao.filter.camera;

import android.content.Context;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 相机过滤器
 */
public class CameraFilter extends MBaseFilter {

    public CameraFilter(Context context) {
        super(context, R.raw.vertex_camera, R.raw.frag_camera);
    }

    protected float[] fbotexVertex = new float[]{
            0.5f, 0.5f,
            1f, 1.0f,
            1f, 0f,
            0f, 0f,
            0f, 1.0f,
    };

    @Override
    protected void initVertexBuffer() {
        TEX_VERTEX = fbotexVertex;
        super.initVertexBuffer();
    }

    public void setMatrix(float[] matrix){
        this.mMatrix = matrix;
    }



}
