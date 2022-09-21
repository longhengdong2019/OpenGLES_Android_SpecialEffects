package com.leroy.texiao.view.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.leroy.texiao.Constant;
import com.leroy.texiao.filter.base.MBaseFilter;
import com.leroy.texiao.filter.base.MBsImgFilter;
import com.leroy.texiao.filter.base.MBsTransFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MTransRender implements GLSurfaceView.Renderer {

    private final Context context;

    private final MBsImgFilter inputFilter1;
    private final MBsImgFilter inputFilter2;
    private final MBsTransFilter filter;
    private final MBaseFilter outFilter;

    private String fileName;
    private String fileName2;

    public MTransRender(Context context, String filterName) {
        this.context = context;
        inputFilter1 = new MBsImgFilter(context);
        inputFilter2 = new MBsImgFilter(context);
        filter = Constant.getTransFilter(context, filterName);
        outFilter = new MBaseFilter(context, false);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        inputFilter1.onCreate();
        inputFilter2.onCreate();
        inputFilter1.setFileName(fileName);
        inputFilter2.setFileName(fileName2);
        filter.onCreate();
        outFilter.onCreate();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        inputFilter1.onChange(width, height);
        inputFilter2.onChange(width, height);
        filter.onChange(width, height);
        outFilter.onChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        inputFilter1.onDrawFrame();
        inputFilter2.onDrawFrame();
        filter.onDrawTrans(inputFilter1.getFboTextureId(), inputFilter2.getFboTextureId());
        outFilter.onDrawFrame(filter.getFboTextureId());
    }

    public void setAssetsFileName(String fileName, String fileName2) {
        this.fileName = fileName;
        this.fileName2 = fileName2;
    }

    public void setProgress(float progress) {
         filter.setProgress(progress);
    }

    public void onRelease() {
        inputFilter1.onRelease();
        inputFilter2.onRelease();
    }


}
