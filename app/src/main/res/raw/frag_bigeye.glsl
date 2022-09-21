#version 300 es
precision mediump float;

// 大眼
uniform sampler2D uSampler;
in vec2 vTexCoord;

uniform vec2 left_eye;
uniform vec2 right_eye;

out vec4 vFragColor;

// 得出需要采集的改变后的点 距离眼睛中心点的距离
float fs(float r, float rmax){
    // 放大系数
    float a = 0.4;
    return (1.0 - (r / rmax - 1.0) * (r / rmax - 1.0) * a);
}

vec2 newCoord(vec2 coord, vec2 eye, float rmax){
    vec2 c = coord;
    float r = distance(coord, eye);
    if (r < rmax) {
        // 改变顶点位置,需要采集的点与眼睛中心点的距离
        float fsr = fs(r, rmax);
        // 高中数学 (x - eye) / (coord - eye) = fsr / r;
        c = fsr * (coord - eye) + eye;
    }
    return c;
}

void main(){
    // 两个眼睛中间的临界值
    float rmax = distance(left_eye, right_eye) / 2.0;

    vec2 cd = newCoord(vTexCoord, left_eye, rmax);
    cd = newCoord(cd, right_eye, rmax);

    vFragColor = texture(uSampler, cd);

}
