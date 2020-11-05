#version 330 core

layout(location = 0) in vec2 position;
layout(location = 1) in vec2 textureCoords;

out vec2 pass_TextureCoords;

uniform mat4 projection;
uniform mat4 transformation;

void main() {
    gl_Position = projection * transformation * vec4(position, -1.0, 1.0);
    pass_TextureCoords = textureCoords;
}