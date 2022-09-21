#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uOffset;
uniform float uDarken;
void main(){
     vec4 sourceColor = texture(uSampler, vTexCoord);
     vec4 sourceColor2 = texture(uSampler2, vTexCoord);

     float x = vTexCoord.x;

     if (uOffset > 0.5) {
          if (x < 0.5) {
               vFragColor = sourceColor;
          } else if (x > uOffset) {
               vFragColor = sourceColor2;
          } else {
               float newX = 0.5 * (x - 0.5) / (uOffset - 0.5) + 0.5;

               vec3 color = texture(uSampler, vec2(newX, vTexCoord.y)).rgb;
               color -= uDarken;

               vFragColor = vec4(color, 1.0);
          }
     } else {
          if (x < uOffset) {
               vFragColor = sourceColor;
          } else if (x > 0.5) {
               vFragColor = sourceColor2;
          } else {
               float newX = 0.5 * (x - uOffset) / (0.5 - uOffset);

               vec3 color = texture(uSampler2, vec2(newX, vTexCoord.y)).rgb;
               color -= uDarken;

               vFragColor = vec4(color, 1.0);
          }
     }

}