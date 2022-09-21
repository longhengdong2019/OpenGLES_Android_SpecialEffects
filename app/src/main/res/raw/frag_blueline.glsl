#version 300 es
precision mediump float;

// 抖音蓝线挑战(横向)
uniform sampler2D vTexture;
uniform sampler2D vTexture2;
in vec2 vTexCoord;
uniform float uOffset;
uniform int type;
out vec4 vFragColor;
void main(){
    // 1 蓝线竖 2 蓝线横 3 传送竖 4 传送横
    if (type == 1) {
        if (vTexCoord.y < uOffset) {
            vFragColor = texture(vTexture2, vTexCoord);
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    } else if (type == 2) {
        if (vTexCoord.x < uOffset) {
            vFragColor = texture(vTexture2, vTexCoord);
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    } else if (type == 3) {
        float uOffset = 0.01f;
        if (vTexCoord.y < 0.5f) {
            vFragColor = texture(vTexture, vTexCoord);
        } else {
            vFragColor = texture(vTexture2, vTexCoord - vec2(0.0f, uOffset));
        }
    } else if (type == 4) {
        float uOffset = 0.01f;
        if (vTexCoord.x < 0.5f) {
            vFragColor = texture(vTexture, vTexCoord);
        } else {
            vFragColor = texture(vTexture2, vTexCoord - vec2(uOffset, 0.0f));
        }
    }
}


// 抖音传送带特效 (横向)
//precision mediump float;
//uniform sampler2D vTexture;
//uniform sampler2D vTexture2;
//varying vec2 vTexCoord;
//
//void main(){
//    float uOffset = 0.01f;
//    if (vTexCoord.y < 0.5f) {
//        vFragColor = texture(vTexture, vTexCoord);
//    } else {
//        vFragColor = texture(vTexture2, vTexCoord - vec2(0.0f, uOffset));
//    }
//}


// 抖音传送带特效 (竖向)
//precision mediump float;
//uniform sampler2D vTexture;
//uniform sampler2D vTexture2;
//varying vec2 vTexCoord;
//
//void main(){
//    float uOffset = 0.01f;
//    if (vTexCoord.x < 0.5f) {
//        vFragColor = texture(vTexture, vTexCoord);
//    } else {
//        vFragColor = texture(vTexture2, vTexCoord - vec2(uOffset, 0.0f));
//    }
//}


// 抖音蓝线挑战(竖向)
//precision mediump float;
//uniform sampler2D vTexture;
//uniform sampler2D vTexture2;
//varying vec2 vTexCoord;
//uniform float uOffset;
//void main(){
//    if (vTexCoord.y < uOffset) {
//        vFragColor = texture(vTexture2, vTexCoord);
//    } else {
//        vFragColor = texture(vTexture, vTexCoord);
//    }
//}


