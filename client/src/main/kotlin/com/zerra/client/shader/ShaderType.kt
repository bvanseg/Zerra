package com.zerra.client.shader

import org.lwjgl.opengl.GL33C.*

enum class ShaderType(val extension: CharSequence, val shaderType: Int) {
    VERTEX("vert", GL_VERTEX_SHADER),
    GEOMETRY("geom", GL_GEOMETRY_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER)
}