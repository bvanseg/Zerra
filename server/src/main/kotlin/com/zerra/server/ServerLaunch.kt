package com.zerra.server

fun main() {
    val server = ZerraServer.getInstance()
    server.init()
    server.createGame()
}