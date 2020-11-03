#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 color;

out vec2 pass_TextureCoords;
out vec3 pass_Color;

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    pass_TextureCoords = position + vec2(0.5);
    pass_Color = color;
}