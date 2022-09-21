package com.leroy.texiao.filter.image;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 分屏特效
 */
public class SplitFilter extends MBaseFilter {

    private float number = 2.0f;    // 分屏数量，2.0f, 3.0f, 4.0f, 6.0f, 9.0f
    private int uNumLocation;

    public SplitFilter(Context context, float number) {
        super(context, R.raw.vertex_base, R.raw.frag_split);
        this.number = number;
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uNumLocation = GLES20.glGetUniformLocation(mProgram, "splitNo");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        GLES20.glUniform1f(uNumLocation, number);
    }

    public void setSplitNum(float number) {
        this.number = number;
    }

}
