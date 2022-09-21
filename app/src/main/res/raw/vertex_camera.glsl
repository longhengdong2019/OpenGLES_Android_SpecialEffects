#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec2 aTextureCoord;
uniform mat4 uMatrix;
out vec2 vTexCoord;
void main(){
    vTexCoord = aTextureCoord;
    gl_PointSize = 10.0;
    gl_Position = uMatrix * vPosition;
}


//attribute vec4 vPosition;
//attribute vec4 vCoord;
//// 摄像头的矩阵，要从摄像头去获取矩阵
//uniform mat4 vMatrix;
//varying vec2 aCoord;
//
//void main(){
//    gl_Position = vPosition;
//    aCoord = (vMatrix * vCoord).xy;
//}

