#version 300 es
precision mediump float;

// 贴图
uniform sampler2D uSampler;
in vec2 vTexCoord;
out vec4 vFragColor;

void main(){
    vFragColor = texture(uSampler, vTexCoord);
}



