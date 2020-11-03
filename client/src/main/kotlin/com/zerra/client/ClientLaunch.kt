package com.zerra.client

import com.zerra.client.render.GameWindow
import org.lwjgl.opengl.GL33C.*

fun main() {
    val client = ZerraClient.getInstance()
    client.init()
    client.createGame()

    val nsPerTick = 1000000000 / 60
    var lastTime = System.nanoTime()
    var delta = 0.0

    while (!GameWindow.closeRequested) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        val now = System.nanoTime()
        delta += (now - lastTime).toDouble() / nsPerTick.toDouble()
        lastTime = now

        if (delta > 10)
            delta = 10.0

        while (delta >= 1) { // FIXME temporary update loop
            client.update()
            delta--
        }

        client.render()
        GameWindow.update()
    }

    client.cleanup()
}