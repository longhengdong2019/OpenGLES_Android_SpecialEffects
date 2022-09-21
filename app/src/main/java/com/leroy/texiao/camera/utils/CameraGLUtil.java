package com.leroy.texiao.camera.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


/**
 * OpenGL工具类
 */
public class CameraGLUtil {

    private static final String TAG = "OpenGLUtil";

    /**
     * 获取矩形的纹理坐标
     */
    public static FloatBuffer getCoordinateBuffer(float[] data) {
        return createFloatBuffer(data);
    }

    /**
     * 构建FloatBuffer
     */
    public static FloatBuffer createFloatBuffer(float[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        FloatBuffer buffer = ByteBuffer.allocateDirect(data.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(data, 0, data.length).position(0);
        return buffer;
    }

    public static void copyAssets(Context context, String path) {
        File model = new File(path);
        File file = new File(Environment.getExternalStorageDirectory(), model.getName());
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = context.getAssets().open(path);

            int len;
            byte[] b = new byte[2048];

            while ((len = is.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            is.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readRawTextFile(Context context, int rawId){
        InputStream is = context.getResources().openRawResource(rawId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 编译
     *
     * @param type       顶点着色器:GLES30.GL_VERTEX_SHADER
     *                   片段着色器:GLES30.GL_FRAGMENT_SHADER
     * @param shaderCode
     * @return
     */
    public static int compileShader(int type, String shaderCode) {
        //创建一个着色器
        final int shaderId = GLES30.glCreateShader(type);
        if (shaderId != 0) {
            //加载到着色器
            GLES30.glShaderSource(shaderId, shaderCode);
            //编译着色器
            GLES30.glCompileShader(shaderId);
            //检测状态
            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(shaderId, GLES30.GL_COMPILE_STATUS, compileStatus, 0);
            if (compileStatus[0] == 0) {
                String logInfo = GLES30.glGetShaderInfoLog(shaderId);
                System.err.println(logInfo);
                //创建失败
                GLES30.glDeleteShader(shaderId);
                return 0;
            }
            return shaderId;
        } else {
            //创建失败
            return 0;
        }
    }

    public static int loadProgram(String vertexSharder, String framentSharder) {
        // 顶点着色器
        int vShader = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        // 加载着色器代码
        GLES20.glShaderSource(vShader, vertexSharder);
        // 编译
        GLES20.glCompileShader(vShader);

        Log.e(TAG, "vertexSharder:\n" + vertexSharder);
        Log.e(TAG, "framentSharder:\n" + framentSharder);

        // 查看配置，是否成功
        int[] status = new int[1];
        GLES20.glGetShaderiv(vShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            // 失败
            throw new IllegalStateException("load vertex shader:" + GLES20.glGetShaderInfoLog(vShader));
        }

        // 片元着色器
        int fShader = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        // 加载着色器代码
        GLES20.glShaderSource(fShader, framentSharder);
        // 编译
        GLES20.glCompileShader(fShader);

        // 查看配置，是否成功
        GLES20.glGetShaderiv(fShader, GLES20.GL_COMPILE_STATUS, status, 0);
        if (status[0] != GLES20.GL_TRUE) {
            // 失败
            throw new IllegalStateException("load fragment shader:" + GLES20.glGetShaderInfoLog(fShader));
        }

        // 创建着色器程序
        int program = GLES20.glCreateProgram();
        // 绑定顶点和片元
        GLES20.glAttachShader(program, vShader);
        GLES20.glAttachShader(program, fShader);
        // 链接着色器程序
        GLES20.glLinkProgram(program);
        // 获得状态
        GLES20.glGetProgramiv(program,GLES20.GL_LINK_STATUS, status,0);
        if (status[0] != GLES20.GL_TRUE) {
            // 失败
            throw new IllegalStateException("link program:" + GLES20.glGetShaderInfoLog(program));
        }

        GLES20.glDeleteShader(vShader);
        GLES20.glDeleteShader(fShader);

        return program;
    }

    // 生成一个纹理 -> FBO
    public static void glGenTextures(int[] texture) {
        GLES20.glGenTextures(texture.length, texture,0);
        for (int i = 0; i < texture.length; i++) {
            // 后面的代码配置纹理，就是配置bind的这个纹理 绑定
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[i]);

            // 设置纹理缩放
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);

            // 设置纹理环绕方式
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);

            // 解绑 相当于通知GPU
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,0);
        }
    }

    /**
     * 创建FBO
     */
    public static int[][] getFbo(int width, int height) {
        int[][] fboData = new int[2][1];

        // fboTextureId -- mFrameBufferTextures[0]
        // fboId -- mFrameBuffer
        int fboId, fboTextureId;

        int[] fboIds = new int[1];
        GLES20.glGenFramebuffers(1, fboIds, 0);
        fboId = fboIds[0];

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId);

        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        fboTextureId = textureIds[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTextureId);
        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, fboTextureId, 0);

        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
                0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);

        if (GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.e(TAG, "glFramebufferTexture2D error");
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        fboData[0] = fboIds;
        fboData[1] = textureIds;

        return fboData;
    }

    /**
     * 加载Bitmap纹理
     */
    public static int getBitmapTexture(Bitmap bitmap) {
        int[] textureIds = new int[1];
        GLES20.glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            return 0;
        }
        if (bitmap == null || bitmap.isRecycled()) {
            GLES20.glDeleteTextures(1, textureIds, 0);
            return 0;
        }
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureIds[0]);

        //环绕（超出纹理坐标范围）  （s==x t==y GL_REPEAT 重复）
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_MIRRORED_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_MIRRORED_REPEAT);
        //过滤（纹理像素映射到坐标点）  （缩小、放大：GL_LINEAR线性）
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureIds[0];
    }


    /**
     * 从文件中获取Bitmap，并校正旋转角度
     */
    public static Bitmap getBitmapFromPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        Bitmap sourceBitmap = BitmapFactory.decodeFile(path);
        int degree = getImageOrientation(getExifInterfaceFromPath(path));
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
    }

    public static ExifInterface getExifInterfaceFromPath(String path) {
        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exifInterface;
    }

    public static Bitmap getBitmapFromAssets(Context context, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            Bitmap sourceBitmap = BitmapFactory.decodeStream(is);
            int degree = getImageOrientation(getExifInterfaceFromIs(is));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.postRotate(degree);
            bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static ExifInterface getExifInterfaceFromIs(InputStream is) {
        ExifInterface exifInterface = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                exifInterface = new ExifInterface(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exifInterface;
    }


    /**
     * 获取图片的旋转方向
     */
    public static int getImageOrientation(ExifInterface exifInterface) {
        int degree = 0;
        if (exifInterface == null) {
            return degree;
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
            default:
                degree = 0;
        }
        Log.d(TAG, "getImageOrientation: degree:" + degree);
        return degree;
    }


    public static int loadImgTexture(Context context, int resourceId) {
        int[] textureIds = new int[1];
        //创建一个纹理对象
        GLES30.glGenTextures(1, textureIds, 0);
        if (textureIds[0] == 0) {
            Log.e(TAG, "Could not generate a new OpenGL textureId object.");
            return 0;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        //这里需要加载原图未经缩放的数据
        options.inScaled = false;
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        if (bitmap == null) {
            Log.e(TAG, "Resource ID " + resourceId + " could not be decoded.");
            GLES30.glDeleteTextures(1, textureIds, 0);
            return 0;
        }
        // 绑定纹理到OpenGL
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIds[0]);

        //设置默认的纹理过滤参数
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        // 加载bitmap到纹理中
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

        // 生成MIP贴图
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);

        // 数据如果已经被加载进OpenGL,则可以回收该bitmap
        bitmap.recycle();

        // 取消绑定纹理
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);

        return textureIds[0];
    }


}
