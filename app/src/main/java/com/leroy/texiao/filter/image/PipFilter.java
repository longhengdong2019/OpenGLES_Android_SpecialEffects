package com.leroy.texiao.filter.image;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;


/*
 * 画中画特效
 */
public class PipFilter extends MBaseFilter {

    private static final String TAG = "PipFilter";
    private GaussFilter gaussFilter;
    private ScaleFilter scaleFilter;
    private int vTexture2Location;
    private int scaleId;

    public PipFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_pip);

        gaussFilter = new GaussFilter(context);
        scaleFilter = new ScaleFilter(context);
        scaleFilter.setScaleRatio(1.0f);
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
        vTexture2Location = GLES20.glGetUniformLocation(mProgram, "vTexture2");
        gaussFilter.onCreate();
        scaleFilter.onCreate();
    }

    @Override
    public boolean onEnableBlend() {
        return false;
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width, height);
        gaussFilter.onChange(width, height);                    //
        scaleFilter.onChange(width, height);                    //
    }

    @Override
    public int onDrawFrame(int textureId) {
        scaleFilter.setSeekProgress(getSeekProgress());
        scaleFilter.onDrawFrame(textureId);
        scaleId = scaleFilter.getFboTextureId();
        gaussFilter.onDrawFrame(scaleId);
        return super.onDrawFrame(gaussFilter.getFboTextureId());
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, scaleId);
        GLES20.glUniform1i(vTexture2Location, 1);
    }
    

}
