#version 300 es
precision mediump float;

// 缩放
uniform sampler2D uSampler;
in vec2 vTexCoord;
// 模糊半径
uniform int uBlurRadius;
// 模糊步长
uniform float uBlurOffset;
// 总权重
uniform float uSumWeight;
// PI
const float PI = 3.1415926;
// 中心点
const vec2 CENTER = vec2(0.5, 0.5);

out vec4 vFragColor;

// 边界值处理
vec2 clampCoordinate(vec2 coordinate) {
    return vec2(clamp(coordinate.x, 0.0, 1.0), clamp(coordinate.y, 0.0, 1.0));
}

// 计算权重
float getWeight(int i) {
    float sigma = float(uBlurRadius) / 3.0;
    return (1.0 / sqrt(2.0 * PI * sigma * sigma)) * exp(-float(i * i) / (2.0 * sigma * sigma)) / uSumWeight;
}

void main(){
    // 原图
    vec4 sourceColor = texture(uSampler, vTexCoord);

    if (uBlurRadius <= 1) {
        vFragColor = sourceColor;
        return;
    }

    // 模糊方向
    vec2 direction = (CENTER - vTexCoord) * uBlurOffset;

    // 最终图像
    vec3 finalColor = sourceColor.rgb * getWeight(0);

    for (int i = 1; i < uBlurRadius; i++) {
        finalColor += texture(uSampler, vTexCoord + direction * float(i)).rgb * getWeight(i);
    }

    vFragColor = vec4(finalColor, sourceColor.a);
}
