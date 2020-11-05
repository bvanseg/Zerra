package com.zerra.client

import bvanseg.kotlincommons.comparable.clamp
import com.zerra.client.render.GameWindow
import org.lwjgl.opengl.GL33C.*

fun main() {
    val client = ZerraClient.getInstance()
    client.init()
    client.createGame()

    // Uncomment these to start an internal server.
//    val state = client.getStateManager().activeState as ClientGameState
//    state.createInternalServer()

    val nsPerTick = 1000000000 / 60
    var lastTime = System.nanoTime()
    var delta = 0.0

    var loopStart: Long
    var loopTime = 0L

    val title = GameWindow.title

    while (!GameWindow.closeRequested) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // Execute tasks
        client.flushTasks()

        loopStart = System.nanoTime()

        val now = System.nanoTime()
        delta += (now - lastTime).toDouble() / nsPerTick.toDouble()
        lastTime = now

        if (delta > 10)
            delta = 10.0

        while (delta >= 1) { // FIXME temporary update loop
            client.update()
            delta--
            if (loopTime > 0)
                GameWindow.setTitle("$title | ${1000000000 / loopTime} FPS | ${loopTime}ns")
        }

        client.render(clamp(delta, 0.0, 1.0).toFloat())
        GameWindow.update()

        loopTime = System.nanoTime() - loopStart
    }

    client.cleanup()
}