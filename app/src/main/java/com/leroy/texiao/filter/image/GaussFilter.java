package com.leroy.texiao.filter.image;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;


/*
 * 高斯模糊特效
 */
public class GaussFilter extends MBaseFilter {

    private static final String TAG = "GaussFilter";
    GaussVFilter gaussVFilter;

    private int scaleRatio = 1;
    private int blurRadius = 30;
    private float blurOffsetW = 0.0f;
    private float blurOffsetH = 0.0f;
    private float sumWeight = 1;

    private int uBlurRadiusLocation;
    private int uBlurOffsetLocation;
    private int uSumWeightLocation;

    public GaussFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_gauss);

        // 设置缩放因子
        setScaleRatio(1);
        // 设置模糊半径
        setBlurRadius(30);
        // 设置模糊步长
        setBlurOffset(5.0f, 0);

        gaussVFilter = new GaussVFilter(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        uBlurRadiusLocation = GLES20.glGetUniformLocation(mProgram, "uBlurRadius");
        uBlurOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uBlurOffset");
        uSumWeightLocation = GLES20.glGetUniformLocation(mProgram, "uSumWeight");

        gaussVFilter.onCreate();
    }

    @Override
    public boolean onEnableBlend() {
        return false;
    }

    @Override
    public void onChange(int width, int height) {
        super.onChange(width / scaleRatio, height / scaleRatio);

        gaussVFilter.onChange(width, height);
    }

    @Override
    public int onDrawFrame(int textureId) {
        // 注意顺序有别，先绘制自身，有偶发性bug
        gaussVFilter.setBlurRadius(blurRadius);
        gaussVFilter.onDrawFrame(textureId);
        return super.onDrawFrame(gaussVFilter.getFboTextureId());
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        // 计算总权重
        calculateSumWeight();

        GLES20.glUniform1i(uBlurRadiusLocation, blurRadius);
        GLES20.glUniform2f(uBlurOffsetLocation,blurOffsetW / width, blurOffsetH / height);
        GLES20.glUniform1f(uSumWeightLocation, sumWeight);
    }

    /**
     * 计算总权重
     */
    private void calculateSumWeight() {
        if (blurRadius < 1) {
            Log.e(TAG, "calculateSumWeight: blurRadius:" + blurRadius + " w:" + blurOffsetW + " h:" + blurOffsetH);
            setSumWeight(0);
            return;
        }

        float sumWeight = 0;
        float sigma = blurRadius / 3f;
        for (int i = 0; i < blurRadius; i++) {
            float weight = (float) ((1 / Math.sqrt(2 * Math.PI * sigma * sigma)) * Math.exp(-(i * i) / (2 * sigma * sigma)));
            sumWeight += weight;
            if (i != 0) {
                sumWeight += weight;
            }
        }

        setSumWeight(sumWeight);
    }

    public void setSumWeight(float sumWeight) {
        Log.e(TAG, "setSumWeight: " + sumWeight);
        this.sumWeight = sumWeight;
    }

    public void setScaleRatio(int scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public void setBlurRadius(int blurRadius) {
        this.blurRadius = blurRadius;
    }

    public void setBlurOffset(float width, float height) {
        this.blurOffsetW = width;
        this.blurOffsetH = 0.0f;
    }


}
