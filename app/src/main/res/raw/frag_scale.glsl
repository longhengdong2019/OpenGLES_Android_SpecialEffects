#version 300 es
precision mediump float;

// 缩放
uniform sampler2D uSampler;
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uScale;

void main(){
    float scaleX = (2.0 * vTexCoord.x + uScale - 1.0) / (2.0 * uScale);
    float scaleY = (2.0 * vTexCoord.y + uScale - 1.0) / (2.0 * uScale);

    vFragColor = texture(uSampler, vec2(scaleX, scaleY));
}