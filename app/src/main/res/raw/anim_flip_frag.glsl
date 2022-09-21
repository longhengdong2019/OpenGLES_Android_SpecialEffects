#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uCompress;
uniform int uUseSampler;
uniform int orientation;

const int VERTICAL = 2;
const int HORIZONTAL = 1;

void main(){

     if (orientation == HORIZONTAL) {
          // 水平翻转
          float newX = (2.0 * vTexCoord.x - uCompress) / (2.0 * (1.0 - uCompress));

          if (newX < uCompress / 2.0 || newX > 1.0 - uCompress / 2.0) {
               vFragColor = vec4(0.0, 0.0, 0.0, 1.0);
               return;
          }

          if (uUseSampler == 0) {
               vFragColor = texture(uSampler, vec2(newX, vTexCoord.y));
          } else {
               vFragColor = texture(uSampler2, vec2(newX, vTexCoord.y));
          }
     } else if (orientation == VERTICAL){
          // 垂直翻转
          float newY = (2.0 * vTexCoord.y - uCompress) / (2.0 * (1.0 - uCompress));

          if (newY < uCompress / 2.0 || newY > 1.0 - uCompress / 2.0) {
               vFragColor = vec4(0.0, 0.0, 0.0, 1.0);
               return;
          }

          if (uUseSampler == 0) {
               vFragColor = texture(uSampler, vec2(vTexCoord.x, newY));
          } else {
               vFragColor = texture(uSampler2, vec2(vTexCoord.x, newY));
          }
     }

}

