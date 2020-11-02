package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.GameWindow
import com.zerra.client.state.GameState
import com.zerra.client.state.StateManager
import com.zerra.common.RegistryManager
import com.zerra.common.Zerra
import com.zerra.common.network.Side

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
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

    override fun init() {
        logger.info("Starting up ZerraClient")
        ClientRegistryManager.ENTITY_REGISTRY.register(ClientEntityPlayer::class, "client.entity.player")

        initClient()
    }

    fun initClient() {
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

    override fun getRegistryManager(): RegistryManager = ClientRegistryManager

    fun createGame() {
        logger.info("Creating new game")
        stateManager.setState(GameState())
    }
}