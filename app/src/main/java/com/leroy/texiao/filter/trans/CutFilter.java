package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;

/*
 * 切分特效
 */
public class CutFilter extends MBsTransFilter {

    private int uOffsetLocation;

    private int uCutLocation;
    private String cutType;
    private int cut = 1;    // 1 水平翻转； 2 垂直翻转

    public CutFilter(Context context, String cutType){
        super(context, R.raw.anim_cut_vertex, R.raw.anim_cut_frag);
        this.cutType = cutType;
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uOffset");
        uCutLocation = GLES20.glGetUniformLocation(mProgram, "cut");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = onGetProgress();

        GLES20.glUniform1f(uOffsetLocation, progress);

        switch (cutType) {
            case Constant.cut_ud_1:
                cut = 1;
                break;
            case Constant.cut_ud_2:
                cut = 2;
                break;
            case Constant.cut_lr_1:
                cut = 3;
                break;
            case Constant.cut_lr_2:
                cut = 4;
                break;
        }
        GLES20.glUniform1i(uCutLocation, cut);
    }


}
