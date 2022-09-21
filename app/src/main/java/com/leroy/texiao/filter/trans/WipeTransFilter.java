package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;

/*
 * 抹除特效
 */
public class WipeTransFilter extends MBsTransFilter {

    private String wipeType;
    private int wipe = 1;    // 1 水平翻转； 2 垂直翻转
    private int uWipeLocation;

    public WipeTransFilter(Context context, String wipeType) {
        super(context, R.raw.anim_wipe_vertex, R.raw.anim_wipe_frag);
        this.wipeType = wipeType;
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uWipeLocation = GLES20.glGetUniformLocation(mProgram, "wipe");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();

        switch (wipeType) {
            case Constant.trans_wipe_up:
                wipe = 1;
                break;
            case Constant.trans_wipe_down:
                wipe = 2;
                break;
            case Constant.trans_wipe_left:
                wipe = 3;
                break;
            case Constant.trans_wipe_right:
                wipe = 4;
                break;
            case Constant.trans_wipe_rup:
                wipe = 5;
                break;
            case Constant.trans_wipe_ldown:
                wipe = 6;
                break;
            case Constant.trans_wipe_middle:
                wipe = 7;
                break;
            case Constant.trans_wipe_circle:
                wipe = 8;
                break;
        }

        GLES20.glUniform1i(uWipeLocation, wipe);
    }


}

