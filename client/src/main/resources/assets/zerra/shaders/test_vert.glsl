#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec3 color;
layout(location = 2) in vec2 textureCoords;

out vec3 pass_Color;
out vec2 pass_TextureCoords;

uniform mat4 projection;
uniform mat4 transformation;

void main() {
    gl_Position = projection * transformation * vec4(position, 0.0, 1.0);
    pass_Color = color;
    pass_TextureCoords = textureCoords;
}