#version 300 es
precision mediump float;

// 仿抖音两屏特效
uniform sampler2D vTexture;
uniform float splitNo;
in vec2 vTexCoord;
out vec4 vFragColor;

void main() {
    if (splitNo == 2.0f) {
        // 纹理坐标
        vec2 uv = vTexCoord.xy;
        float y;
        if (uv.y >= 0.0 && uv.y <= 0.5) {
            y = uv.y + 0.25;
        } else {
            y = uv.y - 0.25;
        }

        vFragColor = texture(vTexture, vec2(uv.x, y));
    } else if (splitNo == 3.0f) {
        highp vec2 uv = vTexCoord;
        if (uv.y < 1.0 / 3.0) {
            uv.y = uv.y + 1.0 / 3.0;
        } else if (uv.y > 2.0 / 3.0) {
            uv.y = uv.y - 1.0 / 3.0;
        }
        vFragColor = texture(vTexture, uv);
    } else if (splitNo == 4.0f) {
        vec2 uv = vTexCoord;
        if (uv.x <= 0.5) {
            uv.x = uv.x * 2.0;
        } else {
            uv.x = (uv.x - 0.5) * 2.0;
        }
        if (uv.y <= 0.5) {
            uv.y = uv.y * 2.0;
        } else {
            uv.y = (uv.y - 0.5) * 2.0;
        }
        vFragColor = texture(vTexture, fract(uv));
    } else if (splitNo == 6.0f) {
        highp vec2 uv = vTexCoord;
        // 左右分三屏
        if (uv.x <= 1.0 / 3.0) {
            uv.x = uv.x + 1.0 / 3.0;
        } else if (uv.x >= 2.0 / 3.0) {
            uv.x = uv.x - 1.0 / 3.0;
        }
        // 上下分两屏，保留 0.25 ~ 0.75部分
        if (uv.y <= 0.5) {
            uv.y = uv.y + 0.25;
        } else {
            uv.y = uv.y - 0.25;
        }
        vFragColor = texture(vTexture, uv);
    } else if (splitNo == 9.0f) {
        highp vec2 uv = vTexCoord;
        if (uv.x < 1.0 / 3.0) {
            uv.x = uv.x * 3.0;
        } else if (uv.x < 2.0 / 3.0) {
            uv.x = (uv.x - 1.0 / 3.0) * 3.0;
        } else {
            uv.x = (uv.x - 2.0 / 3.0) * 3.0;
        }
        if (uv.y <= 1.0 / 3.0) {
            uv.y = uv.y * 3.0;
        } else if (uv.y < 2.0 / 3.0) {
            uv.y = (uv.y - 1.0 / 3.0) * 3.0;
        } else {
            uv.y = (uv.y - 2.0 / 3.0) * 3.0;
        }
        vFragColor = texture(vTexture, uv);
    }
}
