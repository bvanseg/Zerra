package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.render.GameWindow
import com.zerra.client.state.GameState
import com.zerra.client.state.StateManager
import com.zerra.common.Zerra
import com.zerra.common.network.Side

class ZerraClient private constructor(): Zerra() {

    companion object {
        private var instance: ZerraClient? = null

        val logger = getLogger()

        /**
         * Gets a client-sided instance of Zerra.
         */
        fun getInstance(): ZerraClient {
            if(instance == null) {
                logger.info("Creating ZerraClient instance")
                instance = ZerraClient()
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.CLIENT)
    }

    val stateManager: StateManager = StateManager()

    fun startup() {
        logger.info("Starting up ZerraClient")
        // Create window
        val window = GameWindow(1920, 1080)

        // create context
        // TODO
    }

    fun render() {
        stateManager.activeState.render()
    }

    override fun update() {
        stateManager.activeState.update()
    }

    fun createGame() {
        logger.info("Creating new game")
        stateManager.setState(GameState())
    }
}