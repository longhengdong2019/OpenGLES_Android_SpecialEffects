package com.leroy.texiao.filter.camera;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 蓝线挑战(蓝线绘制)
 */
public class BlueLineFilter extends MBaseFilter {

    private HalfFboFilter halfFboFilter;

    private int uTypeLocation;
    private int uOffsetLocation;
    private int type;

    public BlueLineFilter(Context context, int type) {
        super(context, R.raw.vertex_base, R.raw.frag_line);
        this.type = type;

        halfFboFilter = new HalfFboFilter(context, type);
        timeStart(10000);
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
        halfFboFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        halfFboFilter.onChange(width, height);                 //
    }

    @Override
    public boolean onEnableBlend() {
        return false;
    }

    @Override
    public int onDrawFrame(int textureId) {
        halfFboFilter.setProgress(getProgress());
        halfFboFilter.onDrawFrame(textureId);
        return super.onDrawFrame(halfFboFilter.getFboTextureId());
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uOffset");
        uTypeLocation = GLES20.glGetUniformLocation(mProgram, "type");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float offset = getProgress();

        GLES20.glUniform1f(uOffsetLocation, offset);
        GLES20.glUniform1i(uTypeLocation, type);
    }

    // --------------------------------------- 定时器 ---------------------------------------
    private static long duration;
    private static long startTime = -1;

    public static void timeStart(long du) {
        startTime = System.currentTimeMillis();
        duration = du;
    }

    public static float getProgress() {
        if (startTime == -1) {
            return 0;
        }
        long time = System.currentTimeMillis() - startTime;

        time %= duration;

        return (float) time / duration;
    }


}
