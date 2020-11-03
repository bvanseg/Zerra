package com.zerra.client.shader

import org.lwjgl.opengl.GL33C.*

/**
 * @author Ocelot5836
 * @since 0.0.1
 */
enum class ShaderType(val extension: CharSequence, val shaderType: Int) {
    VERTEX("vert", GL_VERTEX_SHADER),
    GEOMETRY("geom", GL_GEOMETRY_SHADER),
    FRAGMENT("frag", GL_FRAGMENT_SHADER)
}