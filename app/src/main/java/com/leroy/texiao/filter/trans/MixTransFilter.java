package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;

/*
 * 混合特效
 */
public class MixTransFilter extends MBsTransFilter {
    private int uAlphaLocation;

    public MixTransFilter(Context context){
        super(context, R.raw.anim_mix_vertex, R.raw.anim_mix_frag);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uAlphaLocation = GLES20.glGetUniformLocation(mProgram, "uAlpha");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        float alpha = getProgress();
        GLES20.glUniform1f(uAlphaLocation, alpha);
    }


}
