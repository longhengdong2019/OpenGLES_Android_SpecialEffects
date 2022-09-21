#version 300 es
layout (location = 0) in vec4 vPosition;
layout (location = 1) in vec2 vCoord;
uniform mat4 uMatrix;
out vec2 aCoord;

void main(){
    gl_Position = vPosition;
    aCoord = vCoord;
}