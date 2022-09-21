#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;

out vec4 vFragColor;
uniform float uAlpha;

void main(){
     vec4 sourceColor = texture(uSampler, vTexCoord);
     vec4 sourceColor2 = texture(uSampler2, vTexCoord);

     vFragColor = mix(sourceColor, sourceColor2, uAlpha);
}






