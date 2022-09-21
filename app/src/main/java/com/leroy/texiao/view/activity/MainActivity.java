package com.leroy.texiao.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;


/**
 *
 * 特效列表
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView btnCVTexiao;
    private TextView btnImgTexiao;
    private TextView btnTransion;
    private View clickView;

    private final static int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnCVTexiao = findViewById(R.id.btnCVTexiao);
        btnImgTexiao = findViewById(R.id.btnImgTexiao);
        btnTransion = findViewById(R.id.btnTransion);
        btnCVTexiao.setOnClickListener(this);
        btnImgTexiao.setOnClickListener(this);
        btnTransion.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.clickView = view;
        requestPermission();
    }

    private void gotoAct() {
        if (clickView == null) {
            return;
        }
        Intent intent = null;
        switch (clickView.getId()){
            case R.id.btnCVTexiao:
                intent = new Intent(this, CVActivity.class);
                break;
            case R.id.btnImgTexiao:
                intent = new Intent(this, FilterListActivity.class);
                intent.putExtra(Constant.EXTRA, Constant.EXTRA_IMAGE);
                break;
            case R.id.btnTransion:
                intent = new Intent(this, FilterListActivity.class);
                intent.putExtra(Constant.EXTRA, Constant.EXTRA_TRANS);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                gotoAct();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        }, REQUEST_CODE);
            }
        } else {
            gotoAct();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                gotoAct();
            } else {
                Toast.makeText(this,"存储权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


}