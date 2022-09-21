package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;


/**
 * 翻转特效
 */
public class FlipFilter extends MBsTransFilter {

    private int uCompressLocation;
    private int uUseSamplerLocation;
    private int uOrientationLocation;
    private int orientation = 1;    // 1 水平翻转； 2 垂直翻转

    public FlipFilter(Context context, int orientation){
        super(context, R.raw.anim_flip_vertex, R.raw.anim_flip_frag);
        this.orientation = orientation;
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uCompressLocation = GLES20.glGetUniformLocation(mProgram, "uCompress");
        uUseSamplerLocation = GLES20.glGetUniformLocation(mProgram, "uUseSampler");
        uOrientationLocation = GLES20.glGetUniformLocation(mProgram, "orientation");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float progress = onGetProgress();

        float compress;
        int useSampler;
        if (progress < 0.5) {
            compress = progress * 2;
            useSampler = 0;
        } else {
            compress = (1 - progress) * 2;
            useSampler = 1;
        }

        GLES20.glUniform1f(uCompressLocation, compress);
        GLES20.glUniform1i(uUseSamplerLocation, useSampler);
        GLES20.glUniform1i(uOrientationLocation, orientation);
    }


}
