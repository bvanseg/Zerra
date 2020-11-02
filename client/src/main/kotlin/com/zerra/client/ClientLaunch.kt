package com.zerra.client

import com.zerra.client.render.GameWindow

fun main() {
    val client = ZerraClient.getInstance()
    client.init()
    client.createGame()
    while (!GameWindow.closeRequested) {
        // waste time until close requested. Temporary until loop is properly set up
        GameWindow.update()
    }
}