#version 330 core

in vec3 pass_Color;
in vec2 pass_TextureCoords;

out vec4 out_Color;

uniform float counter;
uniform sampler2D textureSampler;

void main() {
    out_Color = vec4(pass_Color, 1.0) * texture(textureSampler, pass_TextureCoords) + vec4(sin(counter / 17) / 2, sin(counter / 43) / 2, sin(counter / 33) / 2, 0);
}