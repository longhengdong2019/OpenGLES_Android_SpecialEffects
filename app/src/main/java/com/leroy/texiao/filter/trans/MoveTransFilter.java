package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;

/*
 * 移动特效
 */
public class MoveTransFilter extends MBsTransFilter {

    private int uDirectionalLocation;
    private String moveType;

    public MoveTransFilter(Context context, String moveType){
        super(context, R.raw.anim_move_vertex, R.raw.anim_move_frag);
        this.moveType = moveType;
    }

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uDirectionalLocation = GLES20.glGetUniformLocation(mProgram, "uDirectional");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        switch (moveType) {
            case Constant.trans_move_up:
                // Up
                GLES20.glUniform2f(uDirectionalLocation, 0, 1);
                break;
            case Constant.trans_move_down:
                // Down
                GLES20.glUniform2f(uDirectionalLocation, 0, -1);
                break;
            case Constant.trans_move_left:
                // Left
                GLES20.glUniform2f(uDirectionalLocation, -1, 0);
                break;
            case Constant.trans_move_right:
                // Right
                GLES20.glUniform2f(uDirectionalLocation, 1, 0);
                break;
            case Constant.trans_move_lup:
                // Left, Up
                GLES20.glUniform2f(uDirectionalLocation, -1, 1);
                break;
            case Constant.trans_move_rup:
                // Right, Up
                GLES20.glUniform2f(uDirectionalLocation, 1, 1);
                break;
            case Constant.trans_move_ldown:
                // Left, Down
                GLES20.glUniform2f(uDirectionalLocation, -1, -1);
                break;
            case Constant.trans_move_rdown:
                // Right, Down
                GLES20.glUniform2f(uDirectionalLocation, 1, -1);
                break;

        }
    }


}

