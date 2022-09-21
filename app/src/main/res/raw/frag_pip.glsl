#version 300 es
precision mediump float;

// 画中画
uniform sampler2D uSampler;
uniform sampler2D vTexture2;
in vec2 vTexCoord;
out vec4 vFragColor;

void main(){
    if (vTexCoord.y > 0.30 && vTexCoord.y < 0.70) {
        vFragColor = texture(vTexture2, vTexCoord);
    } else {
        vFragColor = texture(uSampler, vTexCoord);
    }
}

