package com.leroy.texiao.filter.base;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.leroy.texiao.R;
import com.leroy.texiao.camera.utils.CameraGLUtil;
import com.leroy.texiao.utils.GLUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * 过滤器基类
 *
 *
 *                         MBaseFilter
 *                              |
 *                 ----------------------------
 *                |                           |
 *          MBsImgFilter               MBsTransFilter
 *                |                           |
 *                                     MoveTransFilter/PageUpFilter/WipeTransFilter
 *
 *
 */
public class MBaseFilter {

    private static final String TAG = "MBaseFilter";
    protected Context context;

    protected int vertexShaderId, fragmentShaderId;
    protected FloatBuffer vertexBuffer, coordinateBuffer;
    protected ShortBuffer mVertexIndexBuffer;     //
    protected int uMatrixLocation;

    protected float[] mMatrix = new float[16];
    protected int mProgram;
    protected int textureId;
    protected int width;
    protected int height;

    // -------------------------- 其他 --------------------------
    protected int uSamplerLocation;
    protected int aPosLocation;
    protected int aCoordinateLocation;
    protected int vertexSize = 2;
    protected int coordinateSize = 2;
    protected int vertexStride = vertexSize * 4;
    protected int coordinateStride = coordinateSize * 4;
    protected int vertexCount = 4;
    // 纹理坐标
    protected float[] TEX_VERTEX;

    private boolean isCreate = false;
    private boolean isChange = false;
    private boolean isFbo = true;

    protected int vprogress = 40;

    public void setSeekProgress(int vprogress) {
        this.vprogress = vprogress;
    }

    public int getSeekProgress() {
        return vprogress;
    }

    // 顶点坐标
    protected float[] POS_VERTEX = new float[]{
                0f, 0f, 0f,
                -1f, -1f, 0f,
                1f, -1f, 0f,
                1f, 1f, 0f,
                -1f, 1f, 0f,
    };

    protected float[] fbotexVertex = new float[]{
                0.5f, 0.5f,
                1f, 0f,
                0f, 0f,
                0f, 1.0f,
                1f, 1.0f
        };

    protected float[] texVertex = new float[]{
                0.5f, 0.5f,
                0f, 1.0f,
                1f, 1.0f,
                1f, 0f,
                0f, 0f,
        };

    protected static final short[] VERTEX_INDEX = {
            0, 1, 2,
            0, 2, 3,
            0, 3, 4,
            0, 4, 1
    };

    // --------------------------------------- 初始化 ---------------------------------------

    public MBaseFilter(Context context){
        this.context = context;
        this.vertexShaderId = R.raw.vertex_base;
        this.fragmentShaderId = R.raw.frag_base;
    }

    public MBaseFilter(Context context, boolean isFbo){
        this(context);
        this.isFbo = isFbo;
    }

    public MBaseFilter(Context context, int vertexShaderId, int fragmentShaderId){
        this.context = context;
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
    }

    public void onCreate() {
        if (isCreate) {
            return;
        }
        if (isFbo) {
            TEX_VERTEX = fbotexVertex;
        } else {
            TEX_VERTEX = texVertex;
        }

        GLES20.glClearColor(0, 0, 0, 1);
        initBlend();
        initVertexBuffer();
        onCompileShader();
        initLocation();
        isCreate = true;
    }

    /**
     * 初始化顶点坐标
     */
    protected void initVertexBuffer() {
        // 分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(POS_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(POS_VERTEX);
        vertexBuffer.position(0);

        coordinateBuffer = ByteBuffer.allocateDirect(TEX_VERTEX.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(TEX_VERTEX);
        coordinateBuffer.position(0);

        mVertexIndexBuffer = ByteBuffer.allocateDirect(VERTEX_INDEX.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(VERTEX_INDEX);
        mVertexIndexBuffer.position(0);
    }

    /**
     * 初始化着色器传值
     */
    protected void initLocation() {
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, "uMatrix");
    }

    /**
     * 编译着色器
     */
    private void onCompileShader() {
        initProgram(context,vertexShaderId,fragmentShaderId);
    }

    /**
     * 初始化着色器程序
     */
    private void initProgram(Context context, int vertexShaderId, int fragmentShaderId){
        String vertexSharder = CameraGLUtil.readRawTextFile(context, vertexShaderId);
        String framentSharder = CameraGLUtil.readRawTextFile(context, fragmentShaderId);

        // Log.e(TAG, "顶点着色器：\n" + vertexSharder);
        // Log.e(TAG, "片元着色器：\n" + framentSharder);

        mProgram = CameraGLUtil.loadProgram(vertexSharder, framentSharder);
    }

    /**
     * 初始化混色
     */
    private void initBlend() {
        if (!onEnableBlend()) {
            return;
        }
        GLES20.glEnable(GLES20.GL_BLEND);
        // GPU 一次性渲染 Bitmap 贴全部
        // GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * 是否启用混色
     * 在位移变换等特效时，要关闭混色
     */
    public boolean onEnableBlend() {
        return false;
    }

    // --------------------------------------- onChange ---------------------------------------

    public void onChange(int width, int height){
        if (isChange) {
            return;
        }
        onChangePre();
        setSize(width, height);
        onViewport();
        if (isFbo) {
            initFbo();
        }
        onChangeAfter();
        isChange = true;
    }

    /**
     * 设置尺寸之前
     */
    public void onChangePre() { }

    /**
     * 设置尺寸
     */
    protected void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    // FBO int 类型
    int[] fboIds;
    int[] fboTextureIds;

    protected int fboId;
    public int fboTextureId;

    /**
     * 初始化Fbo
     */
    public void initFbo() {
        int[][] fboData = CameraGLUtil.getFbo(width, height);

        fboIds = fboData[0];
        fboTextureIds = fboData[1];

        fboId = fboIds[0];
        fboTextureId = fboTextureIds[0];
    }

    /**
     * 设置窗口尺寸
     */
    public void onViewport() {
        // 设置显示窗口
        GLES20.glViewport(0,0, width, height);

        if (imgW > 0 && imgH > 0) {
            Log.e(TAG,"onViewport");
            final float aspectRatio = width > height ?
                    (float) width / (float) height :
                    (float) height / (float) width;
            if (width > height) {
                // 横屏
                Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
            } else {
                // 竖屏
                Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
            }
            mMatrix = GLUtil.getMatrix(width, height, imgW, imgH);
        } else {
            mMatrix = GLUtil.getMatrix(width, height, width, height);
        }
    }

    /**
     * 设置尺寸之后
     */
    public void onChangeAfter() { }

    // --------------------------------------- onChange ---------------------------------------

    public boolean onReadyToDraw() {
        return imgW != 0 && imgH != 0;
    }

    public int onDrawFrame(int textureId){
        this.textureId = textureId;
        return onDrawFrame();
    }

    public int onDrawFrame(){
        if (!onDrawPre()){
            fboTextureId = textureId;
            return fboTextureId;
        }

        if (!initTextureId()) {
            return textureId;
        }

        onViewport();
        GLES20.glClearColor(0, 0, 0, 1);

        initBlend();
        onClear();
        onUseProgram();
        onInitLocation();
        if (isFbo) {
            onBindFbo();
        }
        onBindVbo();
        onActiveTexture(textureId);
        onPointData();
        onSetOtherData();
        onDraw();
        onDrawAfter();
        onDisableVertex();
        onUnBind();

        if (isFbo) {
            return fboTextureId;
        } else {
            return textureId;
        }
    }

    protected int imgW;
    protected int imgH;

    public void setFbo(boolean fbo) {
        isFbo = fbo;
    }

    /**
     * 初始化纹理图片
     */
    protected boolean initTextureId() {
        return true;
    }

    /**
     * 释放顶点
     */
    protected void onDisableVertex() {
        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
    }

    /**
     * 绑定VBO
     */
    protected void onBindVbo() { }

    /**
     * 解绑
     */
    private void onUnBind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    /**
     * 绘制
     */
    protected void onDraw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, VERTEX_INDEX.length, GLES20.GL_UNSIGNED_SHORT, mVertexIndexBuffer);
    }

    /**
     * 其他设置项
     */
    protected void onSetOtherData() { }

    /**
     * 激活
     */
    protected void onActiveTexture(int textureId) {
        this.textureId = textureId;
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    /**
     * 绑定Fbo
     */
    private void onBindFbo() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, fboTextureId, 0);
        GLES20.glViewport(0, 0, width, height);
    }

    /**
     * 设置顶点坐标
     */
    public void onPointData() {
        GLES20.glEnableVertexAttribArray(0);
        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        GLES20.glEnableVertexAttribArray(1);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, coordinateBuffer);
    }

    /**
     * 向GPU传送数据
     */
    protected void onInitLocation() {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    /**
     * 使用着色器
     */
    private void onUseProgram() {
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 清空屏幕
     */
    private void onClear() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * 绘制之前
     */
    public boolean onDrawPre() {
        return true;
    }

    /**
     * 绘制之后
     */
    public void onDrawAfter() { }


    public int getFboTextureId() {
        return fboTextureId;
    }


    public void onRelease() {
        // onDeleteProgram(program);
        // onDeleteShader(vertexShader);
        // onDeleteShader(fragShader);
        // onDeleteTexture(textureId);
        /// onDeleteTexture(fboTextureId);
        // onDeleteFbo(fboId);

        GLES20.glDeleteProgram(mProgram);
        GLES20.glDeleteTextures(1, new int[]{textureId}, 0);
        GLES20.glDeleteTextures(1, new int[]{fboTextureId}, 0);
        GLES20.glDeleteFramebuffers(1, new int[]{fboId}, 0);
    }


}
