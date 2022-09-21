#version 300 es
#extension GL_OES_EGL_image_external_essl3 : require
//#extension GL_OES_EGL_image_external : require

precision mediump float;
// 安卓特有的采样器
uniform samplerExternalOES vTexture;

in vec2 vTexCoord;
out vec4 vFragColor;

void main(){
    vFragColor = texture(vTexture, vTexCoord);
}



//#extension GL_OES_EGL_image_external : require
//precision mediump float;
//varying vec2 aCoord;
//
//// 安卓特有的采样器
//uniform samplerExternalOES vTexture;
//
//void main(){
//    gl_FragColor = texture2D(vTexture, aCoord);
//}