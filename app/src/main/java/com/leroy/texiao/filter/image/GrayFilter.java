package com.leroy.texiao.filter.image;

import android.content.Context;

import com.leroy.texiao.R;
import com.leroy.texiao.filter.base.MBaseFilter;

/*
 * 灰度图特效
 */
public class GrayFilter extends MBaseFilter {

    public GrayFilter(Context context) {
        super(context, R.raw.vertex_base, R.raw.frag_gray);
    }

}
