package com.zerra.client.render

import org.lwjgl.opengl.GL33.*

fun main() {
    GameWindow.init()
    GameWindow.create(1280, 720, "Test Window")



    while (!GameWindow.closeRequested) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        glBegin(GL_QUADS)
        glVertex3f(-0.5f, -0.5f, 0f)
        glVertex3f(-0.5f, 0.5f, 0f)
        glVertex3f(0.5f, 0.5f, 0f)
        glVertex3f(0.5f, -0.5f, 0f)
        glEnd()

        GameWindow.pollEvents()
    }
    GameWindow.free()
}