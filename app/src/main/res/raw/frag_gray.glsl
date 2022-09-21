#version 300 es
precision mediump float;

// 灰度图
uniform sampler2D vTexture;
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uScale;

void main(){
    vec4 vColor = texture(vTexture, vTexCoord);
    float gray = vColor.r * 0.299 + vColor.g * 0.587 + vColor.b * 0.114;
    vFragColor = vec4(gray, gray, gray, 1.0);
}