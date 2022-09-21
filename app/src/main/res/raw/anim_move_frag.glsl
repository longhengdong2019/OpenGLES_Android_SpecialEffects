#version 300 es
precision mediump float;
uniform sampler2D uTextureUnit;
uniform sampler2D uSampler2;       //
in vec2 vTexCoord;
out vec4 vFragColor;

uniform float uProgress;
uniform vec2 uDirectional;

void main(){
     vec2 coordinate = vTexCoord + uProgress * sign(uDirectional);
     vec2 fractCoordinate = fract(coordinate);

     vec4 sourceColor = texture(uTextureUnit, fractCoordinate);
     vec4 sourceColor2 = texture(uSampler2, fractCoordinate);

     vFragColor = mix(
          sourceColor2,
          sourceColor,
          step(0.0, coordinate.y) * step(coordinate.y, 1.0) * step(0.0, coordinate.x) * step(coordinate.x, 1.0)
     );
}
