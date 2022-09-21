package com.leroy.texiao.filter.image;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 缩放特效
 */
public class ScaleFilter extends MBaseFilter {

    private int uScaleLocation;
    // 缩放比率
    private float scaleRatio = 1.0f;

    float minRatio = 0.2f;
    float maxRatio = 2.0f;
    float defaultRatio = 1.0f;


    public ScaleFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_scale);
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
    public void onCreate() {
        super.onCreate();
        int progress = (int) ((defaultRatio - minRatio) * 100 / maxRatio);
        setSeekProgress(progress);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uScaleLocation = GLES20.glGetUniformLocation(mProgram, "uScale");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        int progress = getSeekProgress();

        scaleRatio = minRatio + (float) (progress / 100.0f) * maxRatio;

        GLES20.glUniform1f(uScaleLocation, scaleRatio);
    }

    public void setScaleRatio(float scaleRatio) {
        this.scaleRatio = scaleRatio;
    }


}
