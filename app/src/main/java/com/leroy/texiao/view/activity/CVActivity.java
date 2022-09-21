package com.leroy.texiao.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.leroy.texiao.OpencvJni;
import com.leroy.texiao.R;
import com.leroy.texiao.camera.utils.CameraHelper;
import com.leroy.texiao.utils.ViewUtil;
import com.leroy.texiao.view.render.CameraRender;

/**
 *
 * 视频合成特效
 *
 */
public class CVActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private GLSurfaceView mGLSurface;
    private AppCompatSeekBar mSeek;
    private RadioGroup radioGroup;
    private CheckBox cbBeauty;
    private CheckBox cbBigEye;
    private CheckBox cbStick;
    private CheckBox cbPip;
    private CheckBox cbGauss;
    private CheckBox cbScale;

    private CheckBox cbNone;

    private CameraHelper cameraHelper;
    private OpencvJni opencvJni;
    CameraRender render;

    int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setFullScreen(this);
        setContentView(R.layout.activity_cv);
        checkPermiss();
        initView();
        initData();
    }

    /**
     * 权限申请
     */
    private void checkPermiss() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
    }

    private void initData() {
        cameraHelper = new CameraHelper(cameraId);
    }

    /**
     * 初始化View
     */
    private void initView() {
        mGLSurface = findViewById(R.id.mGLSurface);
        mSeek = findViewById(R.id.mSeek);
        cbBeauty = findViewById(R.id.cbBeauty);
        cbBigEye = findViewById(R.id.cbBigEye);
        cbStick = findViewById(R.id.cbStick);
        cbPip = findViewById(R.id.cbPip);
        cbGauss = findViewById(R.id.cbGauss);
        cbScale = findViewById(R.id.cbScale);

        radioGroup = findViewById(R.id.radioGroup);
        cbNone = findViewById(R.id.cbNone);

        cbBeauty.setOnCheckedChangeListener(this);
        cbBigEye.setOnCheckedChangeListener(this);
        cbStick.setOnCheckedChangeListener(this);
        cbPip.setOnCheckedChangeListener(this);
        cbGauss.setOnCheckedChangeListener(this);
        cbScale.setOnCheckedChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        cbNone.setOnCheckedChangeListener(this);
        mSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                render.setBarProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        initSurface();
    }
    
    private void initSurface() {
        mGLSurface.setEGLContextClientVersion(2);
        render = new CameraRender(mGLSurface);
        mGLSurface.setRenderer(render);
        mGLSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = findViewById(checkedId);
        clearGroupFilter();
        switch(checkedId){
            case R.id.cbBlueLineV:
                render.checkFilter(7,rb.isChecked());
                break;
            case R.id.cbBlueLineH:
                render.checkFilter(8,rb.isChecked());
                break;
            case R.id.cbConveyorV:
                render.checkFilter(9,rb.isChecked());
                break;
            case R.id.cbConveyorH:
                render.checkFilter(10,rb.isChecked());
                break;
        }
        if (rb!=null) {
            cbNone.setChecked(!rb.isChecked());
        }
    }

    private void clearGroupFilter() {
        render.checkFilter(7,false);
        render.checkFilter(8,false);
        render.checkFilter(9,false);
        render.checkFilter(10,false);
    }

    @Override
    public void onCheckedChanged(CompoundButton cbView, boolean isChecked) {
        switch (cbView.getId()){
            case R.id.cbNone:
                if (isChecked) {
                    radioGroup.clearCheck();
                    clearGroupFilter();
                }
                break;
            case R.id.cbBeauty:
                render.checkFilter(1,isChecked);
                break;
            case R.id.cbBigEye:
                render.checkFilter(2,isChecked);
                break;
            case R.id.cbStick:
                render.checkFilter(3,isChecked);
                break;
            case R.id.cbPip:
                mSeek.setProgress(50);
                render.checkFilter(4,isChecked);
                break;
            case R.id.cbGauss:
                render.checkFilter(5,isChecked);
                break;
            case R.id.cbScale:
                mSeek.setProgress(50);
                render.checkFilter(6,isChecked);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurface.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurface.onPause();
    }


}