package com.leroy.texiao.filter.trans;

import android.content.Context;
import android.opengl.GLES20;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBsTransFilter;

/*
 * 翻页特效
 */
public class PageUpFilter extends MBsTransFilter {

    private String type;

    public PageUpFilter(Context context,String pageupType){
        super(context, R.raw.anim_page_vertex, R.raw.anim_page_frag);
        this.type = pageupType;
    }

    private int uOffsetLocation;
    private int uDarkenLocation;

    @Override
    public void onInitLocation() {
        super.onInitLocation();
        uOffsetLocation = GLES20.glGetUniformLocation(mProgram, "uOffset");
        uDarkenLocation = GLES20.glGetUniformLocation(mProgram, "uDarken");
    }

    @Override
    public void onSetOtherData() {
        super.onSetOtherData();
        float progress = onGetProgress();

        float offset;
        if (Constant.trans_pageup_l.equals(type)) {
            offset = progress;
        } else {
            offset = 1 - progress;
        }

        float darken = progress != 1.0 ? 0.1f : 0;

        GLES20.glUniform1f(uOffsetLocation, offset);
        GLES20.glUniform1f(uDarkenLocation, darken);
    }


}
