package com.leroy.texiao.filter.image;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;


/*
 * 运动模糊特效
 *
 */
public class MoveBlurFilter extends MBaseFilter {

    private int uBlurRadiusLocation;
    private int uSumWeightLocation;
    private int uBlurOffsetLocation;

    private int blurRadius = 30;
    private float blurOffset = 5.0f;
    private float sumWeight = 1.0f;

    public MoveBlurFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_moveblur);
    }

    /**
     * 计算总权重
     */
    private void calculateSumWeight() {
        if (blurRadius < 1) {
            setSumWeight(0);
            return;
        }

        float sumWeight = 0;
        float sigma = blurRadius / 3f;
        for (int i = 0; i < blurRadius; i++) {
            float weight = (float) ((1 / Math.sqrt(2 * Math.PI * sigma * sigma)) * Math.exp(-(i * i) / (2 * sigma * sigma)));
            sumWeight += weight;
        }

        setSumWeight(sumWeight);
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uBlurRadiusLocation = GLES20.glGetUniformLocation(mProgram, "uBlurRadius");
        uBlurOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uBlurOffset");
        uSumWeightLocation = GLES20.glGetUniformLocation(mProgram, "uSumWeight");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        // 计算总权重
        calculateSumWeight();

        GLES20.glUniform1i(uBlurRadiusLocation, blurRadius);
        GLES20.glUniform1f(uBlurOffsetLocation, blurOffset / width);
        GLES20.glUniform1f(uSumWeightLocation, sumWeight);
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    public void setBlurOffset(float blurOffset) {
        this.blurOffset = blurOffset;
    }

    public void setSumWeight(float sumWeight) {
        this.sumWeight = sumWeight;
    }

}
