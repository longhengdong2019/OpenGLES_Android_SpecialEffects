#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;
out vec4 vFragColor;
uniform float uOffset;

uniform int cut;

const int cut_ud_1 = 1;
const int cut_ud_2 = 2;
const int cut_lr_1 = 3;
const int cut_lr_2 = 4;

void main(){
     float x = vTexCoord.x;
     float y = vTexCoord.y;

     switch (cut) {
          case cut_ud_1:
               // 上下切割(一)
               if (y < 0.5) {
                    x += uOffset;
                    if (x <= 1.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x - 1.0, y));
                    }
               } else {
                    x -= uOffset;
                    if (x >= 0.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x + 1.0, y));
                    }
               }
               break;
          case cut_ud_2:
               // 上下切割(二)
               if (y < 0.5) {
                    x -= uOffset;
                    if (x >= 0.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x + 1.0, y));
                    }
               } else {
                    x += uOffset;
                    if (x <= 1.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x - 1.0, y));
                    }
               }
               break;
          case cut_lr_1:
               // 左右切割(一)
               if (x < 0.5) {
                    y += uOffset;
                    if (y <= 1.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x, y - 1.0));
                    }
               } else {
                    y -= uOffset;
                    if (y >= 0.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x, y + 1.0));
                    }
               }
               break;
          case cut_lr_2:
               // 左右切割(二)
               if (x < 0.5) {
                    y -= uOffset;
                    if (y >= 0.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x, y + 1.0));
                    }
               } else {
                    y += uOffset;
                    if (y <= 1.0) {
                         vFragColor = texture(uSampler, vec2(x, y));
                    } else {
                         vFragColor = texture(uSampler2, vec2(x, y - 1.0));
                    }
               }
               break;
     }

}


