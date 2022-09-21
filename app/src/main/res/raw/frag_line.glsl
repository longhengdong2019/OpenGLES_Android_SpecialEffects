#version 300 es
precision mediump float;

// 抖音蓝线挑战(横向)
uniform sampler2D vTexture;
in vec2 vTexCoord;
uniform float uOffset;
uniform int type;
const vec4 COLOR = vec4(0.0, 0.0, 1.0, 1.0);
const float SIZE = 0.002;
out vec4 vFragColor;
void main(){
    // 1 蓝线竖 2 蓝线横 3 传送竖 4 传送横
    if (type == 1) {
        if (vTexCoord.y > uOffset - SIZE && vTexCoord.y < uOffset + SIZE) {
            vFragColor = COLOR;
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    } else if (type == 2) {
        if (vTexCoord.x > uOffset - SIZE && vTexCoord.x < uOffset + SIZE) {
            vFragColor = COLOR;
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    } else if (type == 3) {
        if (vTexCoord.y == 0.5f) {
            vFragColor = COLOR;
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    } else if (type == 4) {
        if (vTexCoord.x == 0.5f) {
            vFragColor = COLOR;
        } else {
            vFragColor = texture(vTexture, vTexCoord);
        }
    }
}



// 抖音蓝线挑战(竖向)
//precision mediump float;
//uniform sampler2D vTexture;
//varying vec2 vTexCoord;
//uniform float uOffset;
//const vec4 COLOR = vec4(0.0, 0.0, 1.0, 1.0);
//const float SIZE = 0.002;
//void main(){
//    if (vTexCoord.y > uOffset - SIZE && vTexCoord.y < uOffset + SIZE) {
//        vFragColor = COLOR;
//    } else {
//        vFragColor = texture(vTexture, vTexCoord);
//    }
//}



