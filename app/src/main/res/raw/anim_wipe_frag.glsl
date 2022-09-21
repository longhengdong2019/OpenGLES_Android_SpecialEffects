#version 300 es
precision mediump float;
uniform sampler2D uSampler;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uProgress;
const float OFFSET = 0.2;

uniform int wipe;
const int trans_wipe_up = 1;
const int trans_wipe_down = 2;
const int trans_wipe_left = 3;
const int trans_wipe_right = 4;
const int trans_wipe_lup = 5;
const int trans_wipe_rdown = 6;
const int trans_wipe_middle = 7;
const int trans_wipe_circle = 8;

void main(){
     vec4 sourceColor = texture(uSampler, vTexCoord);
     vec4 sourceColor2 = texture(uSampler2, vTexCoord);
     float progress = 0.0f;
     switch (wipe) {
          case trans_wipe_up:
               // 向上抹除
               progress = 1.0 - uProgress;
               vFragColor = mix(sourceColor, sourceColor2, smoothstep(progress - OFFSET, progress, vTexCoord.y));
               break;
          case trans_wipe_down:
               // 向下抹除
               progress = uProgress;
               vFragColor = mix(sourceColor2, sourceColor, smoothstep(progress, progress + OFFSET, vTexCoord.y));
               break;
          case trans_wipe_left:
               // 向左抹除
               progress = uProgress;
               vFragColor = mix(sourceColor2, sourceColor, smoothstep(progress, progress + OFFSET, vTexCoord.x));
               break;
          case trans_wipe_right:
               // 向右抹除
               progress = 1.0 - uProgress;
               vFragColor = mix(sourceColor, sourceColor2, smoothstep(progress - OFFSET, progress, vTexCoord.x));
               break;
          case trans_wipe_lup:
               // 右上抹除
               progress = 1.0 - uProgress;
               vFragColor = mix(sourceColor, sourceColor2, smoothstep(0.0, progress, vTexCoord.x + vTexCoord.y - progress * 2.0));
               break;
          case trans_wipe_rdown:
               // 左下抹除
               progress = uProgress;
               vFragColor = mix(sourceColor2, sourceColor, smoothstep(0.0, progress, vTexCoord.x + vTexCoord.y - progress * 2.0));
               break;
          case trans_wipe_middle:
               // 抹除中线
               float smoothStep1 = smoothstep(0.5 - (uProgress + OFFSET), 0.5 - uProgress, vTexCoord.y);
               float smoothStep2 = smoothstep(0.5 + uProgress, 0.5 + (uProgress + OFFSET), vTexCoord.y);
               vFragColor = mix(sourceColor, sourceColor2, smoothStep1 - smoothStep2);
               break;
          case trans_wipe_circle:
               // 抹除圆心
               const vec2 CENTER = vec2(0.5);
               float radius = length(vTexCoord - CENTER);
               float maxR = length(CENTER);
               float curR = maxR * uProgress;
               vFragColor = mix(sourceColor2, sourceColor, smoothstep(curR, curR + OFFSET, radius));
               break;
     }

}





