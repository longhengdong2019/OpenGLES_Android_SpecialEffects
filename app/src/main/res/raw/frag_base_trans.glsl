#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;

uniform float uProgress;
out vec4 vFragColor;

void main(){
     vFragColor = texture(uSampler, vTexCoord);
}








