package com.leroy.texiao.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.utils.ViewUtil;
import com.leroy.texiao.view.render.MImgRender;


/**
 *
 * 静态图特效
 *
 */
public class ImgActivity extends AppCompatActivity {

    private String filterName;
    private GLSurfaceView mGLSurface;
    private MImgRender render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setFullScreen(this);
        setContentView(R.layout.activity_img);
        checkPermiss();
        initView();
    }

    private void checkPermiss() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            // 验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    // 申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void initView() {
        filterName = getIntent().getStringExtra(Constant.EXTRA);
        mGLSurface = findViewById(R.id.mGLSurface);
        initSurface();
    }

    private void initSurface() {
        mGLSurface.setEGLContextClientVersion(2);
        render = new MImgRender(this, filterName);
        mGLSurface.setRenderer(render);
        mGLSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGLSurface != null) {
            render.setSourceId(R.drawable.static_photo);
            mGLSurface.requestRender();
        }
    }



}