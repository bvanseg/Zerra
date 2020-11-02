package com.zerra.client.render

import org.lwjgl.opengl.GL33C.*

fun main() {
    GameWindow.init()
    GameWindow.create(1280, 720, "Test Window")

    val vao = glGenVertexArrays()
    val vbo1 = glGenBuffers()
    val vbo2 = glGenBuffers()

    glBindVertexArray(vao)

    glBindBuffer(GL_ARRAY_BUFFER, vbo1)
    glBufferData(GL_ARRAY_BUFFER, floatArrayOf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f), GL_STATIC_DRAW)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo2)
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, shortArrayOf(0, 1, 2, 0, 2, 3), GL_STATIC_DRAW)

    glBindVertexArray(0)

    while (!GameWindow.closeRequested) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // Render
        glBindVertexArray(vao)
        glEnableVertexAttribArray(0)
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0)
        glDisableVertexAttribArray(0)
        glBindVertexArray(0)

        GameWindow.update()
    }
    GameWindow.free()
    glDeleteBuffers(vbo1)
    glDeleteBuffers(vbo2)
    glDeleteVertexArrays(vao)
}