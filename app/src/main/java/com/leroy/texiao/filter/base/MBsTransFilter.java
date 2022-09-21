package com.leroy.texiao.filter.base;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.R;

/**
 * 动态特效基类
 */
public class MBsTransFilter extends MBaseFilter {

    private int uSampler2Location;
    private int uProgressLocation;

    private int textureId2;

    private float progress;

    public MBsTransFilter(Context context) {
        super(context, R.raw.vertex_base_trans, R.raw.frag_base_trans);
    }

    public MBsTransFilter(Context context, int vertexShaderId, int fragmentShaderId){
        super(context, vertexShaderId, fragmentShaderId);
    }

    public boolean onEnableBlend() {
        return false;
    }

    // ------------------------------------------------------------------------------------

    @Override
    protected void onInitLocation() {
        super.onInitLocation();
        uSampler2Location = GLES20.glGetUniformLocation(mProgram, "uSampler2");
        uProgressLocation = GLES20.glGetUniformLocation(mProgram, "uProgress");
    }

    @Override
    protected void onActiveTexture(int textureId) {
        super.onActiveTexture(textureId);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId2);
        GLES20.glUniform1i(uSampler2Location, 1);
    }

    @Override
    protected void onSetOtherData() {
        super.onSetOtherData();
        GLES20.glUniform1f(uProgressLocation, onGetProgress());
    }

    public float onGetProgress() {
        // 根据效果选择是否阻尼
        // return getProgress();
        return getAccelerateProgress();
    }

    public void onDrawTrans(int textureId, int textureId2) {
        this.textureId2 = textureId2;
        this.textureId = textureId;
        super.onDrawFrame();
    }

    public float getProgress() {
        return progress;
    }

    public float getAccelerateProgress() {
        return (float) Math.pow(progress, 2 * 1.5);
    }

    public float getDecelerateProgress() {
        return (float) (1 - Math.pow(1 - progress, 2 * 1.5));
    }

    public float getAccelerateDecelerateProgress() {
        return (float) (Math.cos((progress + 1) * Math.PI) / 2 + 0.5);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

//    @Override
//    public void onRelease() {
//        onDeleteProgram(getProgram());
//        onDeleteShader(getVertexShader());
//        onDeleteShader(getFragShader());
//        onDeleteTexture(getFboTextureId());
//        onDeleteFbo(getFboId());
//        onDeleteVbo(getVboId());
//    }
//
//    public void releaseTextureId() {
//        onDeleteTexture(getTextureId());
//        onDeleteTexture(textureId2);
//    }


}
