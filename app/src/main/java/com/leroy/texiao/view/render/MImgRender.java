package com.leroy.texiao.view.render;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.leroy.texiao.Constant;
import com.leroy.texiao.filter.base.MBaseFilter;
import com.leroy.texiao.filter.base.MBsImgFilter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class MImgRender implements GLSurfaceView.Renderer {

    private Context context;
    private MBsImgFilter imgfilter;
    private MBaseFilter filter;

    private final MBaseFilter outFilter;
    private int sourceId;
    private String fileName;


    public MImgRender(Context context, String filterName){
        this.context = context;
        imgfilter = new MBsImgFilter(context);
        filter = Constant.getImageFilters(context, filterName);
        outFilter = new MBaseFilter(context,false);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        imgfilter.onCreate();
        imgfilter.setFileName(fileName);
        imgfilter.setImgSourceId(sourceId);
        filter.onCreate();
        outFilter.onCreate();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        imgfilter.onChange(width, height);
        filter.onChange(width, height);
        outFilter.onChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        imgfilter.onDrawFrame();
        filter.onDrawFrame(imgfilter.getFboTextureId());
        outFilter.onDrawFrame(filter.getFboTextureId());
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setAssetsFileName(String fileName) {
        this.fileName = fileName;
    }


}

