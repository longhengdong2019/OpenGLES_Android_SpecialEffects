package com.leroy.texiao.view.render;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.util.Log;

import com.leroy.texiao.OpencvJni;
import com.leroy.texiao.filter.camera.BigEyeFilter;
import com.leroy.texiao.filter.camera.CameraFilter;
import com.leroy.texiao.filter.image.GaussFilter;
import com.leroy.texiao.filter.image.ScaleFilter;
import com.leroy.texiao.filter.camera.BlueLineFilter;
import com.leroy.texiao.filter.camera.StickFilter;
import com.leroy.texiao.camera.utils.CameraHelper;
import com.leroy.texiao.camera.utils.CameraGLUtil;
import com.leroy.texiao.filter.base.MBaseFilter;
import com.leroy.texiao.filter.camera.BeautyFilter;
import com.leroy.texiao.filter.image.PipFilter;

import java.io.File;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


// 在 GLSurfaceView.Render 中创建一个纹理，再使用该纹理创建一个 SurfaceTexture。
// 将该SurfaceTexture 作为相机预览输出

// 使用Camera时直接将该SurfaceTexture传给相机。
// 使用Camera2时使用该SurfaceTexture创建一个 Surface 传给相机。

// 使用 GLSurfaceView.Render 将该纹理渲染到 GLSurfaceView 窗口上。
// 使用 GLSurfaceTexture 的 setOnFrameAvailableListener 方法给 SurfaceTexture 添加一个数据帧数据可用的监听器，
// 在监听器中调用 GLSurfaceView 的 requestRender 方法渲染该帧数据，
// 这样相机每次输出一帧数据就可以渲染一次，就可以在GLSurfaceView窗口中看到相机的预览数据了。


public class CameraRender implements GLSurfaceView.Renderer,
        SurfaceTexture.OnFrameAvailableListener, Camera.PreviewCallback {

    private final static String TAG = "CameraRender";
    private CameraHelper mCameraHelper;
    private GLSurfaceView cameraView;
    private SurfaceTexture mSurfaceTexture;
    private int[] mTextures;
    private float[] mtx = new float[16];
    private CameraFilter mCameraFilter;
    private MBaseFilter mScreenFilter;
    private BigEyeFilter mBigEyeFilter;
    private StickFilter mStickFilter;
    private BeautyFilter mBeautyFilter;
    private BlueLineFilter mBlueFilter;
    private GaussFilter mGaussFilter;
    private PipFilter mPipFilter;
    private ScaleFilter mScaleFilter;
    private int progress;

    private int mWidth;
    private int mHeight;

    OpencvJni opencvJni;
    File cascadeFile = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml");
    File seeta_fa_v1 = new File(Environment.getExternalStorageDirectory(), "seeta_fa_v1.1.bin");
    
    public CameraRender(GLSurfaceView cameraView){
        this.cameraView = cameraView;
        initCascade();
    }

    private void initCascade() {
        CameraGLUtil.copyAssets(cameraView.getContext(),"lbpcascade_frontalface.xml");
        CameraGLUtil.copyAssets(cameraView.getContext(),"seeta_fa_v1.1.bin");
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.e(TAG, "onSurfaceCreated");

        // 打开摄像头
        mCameraHelper = new CameraHelper(Camera.CameraInfo.CAMERA_FACING_BACK);
        mTextures = new int[1];
        GLES20.glGenTextures(mTextures.length, mTextures,0);
        mSurfaceTexture = new SurfaceTexture(mTextures[0]);
        mSurfaceTexture.setOnFrameAvailableListener(this);
        // 矩阵 -> 摄像头数据不会变形
        mSurfaceTexture.getTransformMatrix(mtx);

        mCameraFilter = new CameraFilter(cameraView.getContext());
        mScreenFilter = new MBaseFilter(cameraView.getContext());

        mCameraFilter.setMatrix(mtx);
        mCameraHelper.switchCamera();

        mCameraFilter.setFbo(true);
        mCameraFilter.onCreate();

        mScreenFilter.setFbo(false);
        mScreenFilter.onCreate();
    }

    // SurfaceTexture.OnFrameAvailableListener用于通知TextureView内容流有新图像到来
    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.e(TAG, "onFrameAvailable");
        // 需要重绘的时候调用
        cameraView.requestRender();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.e(TAG, "onSurfaceChanged");
        Log.e("美颜特效", "当前线程: " + Thread.currentThread().getName());
        mWidth = width;
        mHeight = height;
        // 开启预览
        mCameraHelper.startPreview(mSurfaceTexture);
        mCameraHelper.setPreviewCallback(this);

        opencvJni = new OpencvJni(cascadeFile.getAbsolutePath(), seeta_fa_v1.getAbsolutePath(), mCameraHelper);
        opencvJni.startTrack();

        mCameraFilter.onChange(width, height);
        mScreenFilter.onChange(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Log.e(TAG, "onDrawFrame");
        // 摄像头获取一帧数据，会回调此方法
        // 摄像头采集到数据后，会回调onFrameAvailable，调用mView.requestRender()时，会回调onDrawFrame方法
        GLES20.glClearColor(0,0,0,0);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // 把摄像头数据拿出来
        mSurfaceTexture.updateTexImage();
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraFilter.setMatrix(mtx);

        // 责任链模式：
        // id = 效果1.onDrawFrame(id); 帽子
        // id = 效果2.onDrawFrame(id); 眼睛
        // id = 效果3.onDrawFrame(id); 大耳朵

        // 相机
        int id = mCameraFilter.onDrawFrame(mTextures[0]);

        // 美颜
        if (mBeautyFilter != null) {
            id = mBeautyFilter.onDrawFrame(id);
        }

        // 大眼
        if (mBigEyeFilter!=null) {
            mBigEyeFilter.setFace(opencvJni.getFace());
            id = mBigEyeFilter.onDrawFrame(id);
        }

        // 贴图
        if (mStickFilter!=null) {
            mStickFilter.setFace(opencvJni.getFace());
            id = mStickFilter.onDrawFrame(id);
        }

        // 蓝线
        if (mBlueFilter != null) {
            id = mBlueFilter.onDrawFrame(id);
        }

        // 画中画
        if (mPipFilter != null) {
            mPipFilter.setSeekProgress(progress);
            id = mPipFilter.onDrawFrame(id);
        }

        // 高斯
        if (mGaussFilter != null) {
            id = mGaussFilter.onDrawFrame(id);
        }

        // 缩放
        if (mScaleFilter != null) {
            mScaleFilter.setSeekProgress(progress);
            id = mScaleFilter.onDrawFrame(id);
        }

        // ScreenFilter 最后调用。将最终的特效运用到 SurfaceView 中
        mScreenFilter.onDrawFrame(id);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.e(TAG, "onPreviewFrame");
        // 数据 data->
        if (opencvJni!=null) {
            opencvJni.detector(data);
        }
    }

    public void checkFilter(int filterType,boolean isChecked) {
        // 操作GPU，有专属的线程
        // 通过queueEvent可以把它塞到主线程。类似于runOnUiThread
        cameraView.queueEvent(new Runnable() {
            @Override
            public void run() {
                if (!isChecked) {
                    switch (filterType){
                        case 1:
                            mBeautyFilter = null;
                            break;
                        case 2:
                            mBigEyeFilter = null;
                            break;
                        case 3:
                            mStickFilter = null;
                            break;
                        case 4:
                            mPipFilter = null;
                            break;
                        case 5:
                            mGaussFilter = null;
                            break;
                        case 6:
                            mScaleFilter = null;
                            break;
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            mBlueFilter = null;
                            break;
                    }
                } else {
                    switch (filterType) {
                        case 1:
                            mBeautyFilter = new BeautyFilter(cameraView.getContext());
                            mBeautyFilter.onCreate();
                            mBeautyFilter.onChange(mWidth, mHeight);
                            break;
                        case 2:
                            mBigEyeFilter = new BigEyeFilter(cameraView.getContext());
                            mBigEyeFilter.onCreate();
                            mBigEyeFilter.onChange(mWidth, mHeight);
                            break;
                        case 3:
                            mStickFilter = new StickFilter(cameraView.getContext());
                            mStickFilter.onCreate();
                            mStickFilter.onChange(mWidth, mHeight);
                            break;
                        case 4:
                            mPipFilter = new PipFilter(cameraView.getContext());
                            mPipFilter.onCreate();
                            mPipFilter.onChange(mWidth, mHeight);
                            break;
                        case 5:
                            mGaussFilter = new GaussFilter(cameraView.getContext());
                            mGaussFilter.onCreate();
                            mGaussFilter.onChange(mWidth, mHeight);
                            break;
                        case 6:
                            mScaleFilter = new ScaleFilter(cameraView.getContext());
                            mScaleFilter.onCreate();
                            mScaleFilter.onChange(mWidth, mHeight);
                            break;

                        case 7:
                            mBlueFilter = new BlueLineFilter(cameraView.getContext(), 1);
                            mBlueFilter.onCreate();
                            mBlueFilter.onChange(mWidth, mHeight);
                            break;
                        case 8:
                            mBlueFilter = new BlueLineFilter(cameraView.getContext(), 2);
                            mBlueFilter.onCreate();
                            mBlueFilter.onChange(mWidth, mHeight);
                            break;
                        case 9:
                            mBlueFilter = new BlueLineFilter(cameraView.getContext(), 3);
                            mBlueFilter.onCreate();
                            mBlueFilter.onChange(mWidth, mHeight);
                            break;
                        case 10:
                            mBlueFilter = new BlueLineFilter(cameraView.getContext(), 4);
                            mBlueFilter.onCreate();
                            mBlueFilter.onChange(mWidth, mHeight);
                            break;
                    }
                }
            }
        });
    }

    public void setBarProgress(int progress) {
        this.progress = progress;
    }


}
