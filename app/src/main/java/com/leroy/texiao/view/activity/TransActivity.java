package com.leroy.texiao.view.activity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leroy.texiao.Constant;
import com.leroy.texiao.R;
import com.leroy.texiao.utils.ViewUtil;
import com.leroy.texiao.view.render.MTransRender;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 动态特效
 *
 */
public class TransActivity extends AppCompatActivity implements View.OnClickListener {

    private String filterName;
    private GLSurfaceView mSurface;
    private TextView btnStart;
    private MTransRender render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.setFullScreen(this);
        setContentView(R.layout.activity_transion);
        filterName = getIntent().getStringExtra(Constant.EXTRA);
        mSurface = findViewById(R.id.mSurface);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        initGLView();
    }

    private void initGLView() {
        mSurface.setEGLContextClientVersion(2);
        render = new MTransRender(this, filterName);
        mSurface.setRenderer(render);
        mSurface.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        render.setAssetsFileName("image/trans_from.jpeg", "image/trans_to.jpeg");
    }

    @Override
    public void onClick(View v) {
        start();
    }

    private void start(){
        mSurface.queueEvent(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        });
        mSurface.requestRender();
    }

    private Timer timer;

    public void startTimer() {
        long duration = (long) (0.7 * 1000);
//        long duration = (long) (3 * 1000);

        stop();
        mSurface.queueEvent(new Runnable() {
            @Override
            public void run() {
                // TransitionManager.getInstance(getContext()).setTransition(bean);
            }
        });
        mSurface.requestRender();
        long startTime = System.currentTimeMillis();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long time = System.currentTimeMillis() - startTime;

                if (time >= duration) {
                    render.setProgress(1);
                    mSurface.requestRender();
                    stop();
                    return;
                }

                time = time % duration;

                float progress = (float) time / duration;
                render.setProgress(progress);
                mSurface.requestRender();
            }
        }, 0, 1000 / 60);
    }

    public void stop() {
        if (timer == null) {
            return;
        }
        timer.cancel();
        timer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        render.onRelease();
    }


}
