package com.leroy.texiao.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leroy.texiao.R;
import com.leroy.texiao.Constant;
import com.leroy.texiao.view.ListAdapter;


/**
 * 特效列表
 *
 */
public class FilterListActivity extends AppCompatActivity {

    private TextView tvTitle;
    private RecyclerView recyList;
    private ListAdapter adapter;
    private String filterType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);
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
        filterType = getIntent().getStringExtra(Constant.EXTRA);
        tvTitle = findViewById(R.id.tvTitle);
        recyList = findViewById(R.id.recyList);

        if (Constant.EXTRA_IMAGE.equals(filterType)) {
            tvTitle.setText("OpenGL静态特效");
        } else {
            tvTitle.setText("OpenGL动态特效");
        }

        adapter = new ListAdapter(Constant.getFilters(filterType));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyList.setLayoutManager(manager);
        recyList.setAdapter(adapter);
        adapter.setItemClick(new ListAdapter.OnItemClick() {
            @Override
            public void itemClick(String filterName) {
                Intent intent = null;
                if (Constant.EXTRA_IMAGE.equals(filterType)) {
                    intent = new Intent(FilterListActivity.this, ImgActivity.class);
                } else if (Constant.EXTRA_TRANS.equals(filterType)) {
                    intent = new Intent(FilterListActivity.this, TransActivity.class);
                }

                intent.putExtra(Constant.EXTRA, filterName);
                startActivity(intent);
            }
        });
    }


}