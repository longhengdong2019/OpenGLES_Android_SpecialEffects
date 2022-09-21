package com.leroy.texiao.filter.trans;

import android.content.Context;

import com.leroy.texiao.filter.base.MBsTransFilter;
import com.leroy.texiao.filter.image.GaussFilter;

/*
 * 模糊变化特效
 */
public class BlurTransFilter extends MBsTransFilter {

    private final MixTransFilter mixTransition;
    private GaussFilter gaussFilter;

    public BlurTransFilter(Context context) {
        super(context);
        mixTransition = new MixTransFilter(context);
        gaussFilter = new GaussFilter(context);
    }

    @Override
    public void onCreate() {
        mixTransition.onCreate();
        gaussFilter.onCreate();
    }

    @Override
    public void onChange(int width, int height) {
        mixTransition.onChange(width, height);
        gaussFilter.onChange(width, height);
    }

    @Override
    public void onDrawTrans(int textureId, int textureId2) {
        mixTransition.onDrawTrans(textureId, textureId2);

        float progress = getProgress();
        int scaleRatio = 4;
        int blurRadius;
        int blurOffset;
        if (progress < 0.5) {
            blurRadius = (int) (progress * 2 * 30);
            blurOffset = (int) (progress * 2 * 5);
        } else {
            blurRadius = (int) ((1 - progress) * 2 * 30);
            blurOffset = (int) ((1 - progress) * 2 * 5);
        }

        gaussFilter.setBlurRadius(blurRadius);
        gaussFilter.setBlurOffset(blurOffset, blurOffset);
        gaussFilter.onDrawFrame(mixTransition.getFboTextureId());
    }

    @Override
    public int getFboTextureId() {
        return gaussFilter.getFboTextureId();
    }

    @Override
    public void setProgress(float progress) {
        super.setProgress(progress);
        mixTransition.setProgress(progress);
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mixTransition.onRelease();
        gaussFilter.onRelease();
    }


}
