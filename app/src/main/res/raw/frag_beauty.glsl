#version 300 es
precision lowp float;

// 画中画
uniform sampler2D vTexture;
in vec2 aCoord;

uniform int width;
uniform int height;

out vec4 vFragColor;
const highp vec3 W = vec3(0.30, 0.59, 0.11);
vec2 blurCoordinates[20];

float hardLight(float color){
    if (color <= 0.5)
    color = color * color * 2.0;
    else
    color = 1.0 - ((1.0 - color) * (1.0 - color) * 2.0);
    return color;
}

void main(){
    // 3 - 1 值越小，效果越明显
    float params = 0.33;
    vec2 singleStepOffset = vec2(1.0 / float(width), 1.0 / float(height));
    vec3 centralColor = texture(vTexture, aCoord).rgb;

    blurCoordinates[0] = aCoord.xy + singleStepOffset * vec2(0.0, -10.0);
    blurCoordinates[1] = aCoord.xy + singleStepOffset * vec2(0.0, 10.0);
    blurCoordinates[2] = aCoord.xy + singleStepOffset * vec2(-10.0, 0.0);
    blurCoordinates[3] = aCoord.xy + singleStepOffset * vec2(10.0, 0.0);
    blurCoordinates[4] = aCoord.xy + singleStepOffset * vec2(5.0, -8.0);
    blurCoordinates[5] = aCoord.xy + singleStepOffset * vec2(5.0, 8.0);
    blurCoordinates[6] = aCoord.xy + singleStepOffset * vec2(-5.0, 8.0);
    blurCoordinates[7] = aCoord.xy + singleStepOffset * vec2(-5.0, -8.0);
    blurCoordinates[8] = aCoord.xy + singleStepOffset * vec2(8.0, -5.0);
    blurCoordinates[9] = aCoord.xy + singleStepOffset * vec2(8.0, 5.0);
    blurCoordinates[10] = aCoord.xy + singleStepOffset * vec2(-8.0, 5.0);
    blurCoordinates[11] = aCoord.xy + singleStepOffset * vec2(-8.0, -5.0);
    blurCoordinates[12] = aCoord.xy + singleStepOffset * vec2(0.0, -6.0);
    blurCoordinates[13] = aCoord.xy + singleStepOffset * vec2(0.0, 6.0);
    blurCoordinates[14] = aCoord.xy + singleStepOffset * vec2(6.0, 0.0);
    blurCoordinates[15] = aCoord.xy + singleStepOffset * vec2(-6.0, 0.0);
    blurCoordinates[16] = aCoord.xy + singleStepOffset * vec2(-4.0, -4.0);
    blurCoordinates[17] = aCoord.xy + singleStepOffset * vec2(-4.0, 4.0);
    blurCoordinates[18] = aCoord.xy + singleStepOffset * vec2(4.0, -4.0);
    blurCoordinates[19] = aCoord.xy + singleStepOffset * vec2(4.0, 4.0);

    float sampleColor = centralColor.g * 20.0;

    sampleColor += texture(vTexture, blurCoordinates[0]).g;
    sampleColor += texture(vTexture, blurCoordinates[1]).g;
    sampleColor += texture(vTexture, blurCoordinates[2]).g;
    sampleColor += texture(vTexture, blurCoordinates[3]).g;
    sampleColor += texture(vTexture, blurCoordinates[4]).g;
    sampleColor += texture(vTexture, blurCoordinates[5]).g;
    sampleColor += texture(vTexture, blurCoordinates[6]).g;
    sampleColor += texture(vTexture, blurCoordinates[7]).g;
    sampleColor += texture(vTexture, blurCoordinates[8]).g;
    sampleColor += texture(vTexture, blurCoordinates[9]).g;
    sampleColor += texture(vTexture, blurCoordinates[10]).g;
    sampleColor += texture(vTexture, blurCoordinates[11]).g;
    sampleColor += texture(vTexture, blurCoordinates[12]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[13]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[14]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[15]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[16]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[17]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[18]).g * 2.0;
    sampleColor += texture(vTexture, blurCoordinates[19]).g * 2.0;
    sampleColor = sampleColor / 48.0;

    // 高反差保留
    float heighPass = centralColor.g - sampleColor + 0.5;

    // 强光处理 让噪声更加突出
    for (int i = 0; i < 5; i++) {
        heighPass = hardLight(heighPass);
    }

    // 305911 获得灰度值
    float luminance = dot(centralColor, W);

    // 平均透明度
    float alpha = pow(luminance, params);

    vec3 smoothColor = centralColor + (centralColor - vec3(heighPass)) * alpha * 0.1;

    vFragColor = vec4(mix(smoothColor.rgb, max(smoothColor, centralColor), alpha), 1.0);


}


