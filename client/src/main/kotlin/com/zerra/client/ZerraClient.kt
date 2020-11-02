package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.GameWindow
import com.zerra.client.state.ClientState
import com.zerra.client.state.ClientStateManager
import com.zerra.client.state.ClientGameState
import com.zerra.common.api.registry.RegistryManager
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
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
                zerraInstance = instance
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.CLIENT)
    }

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
        val state = ClientStateManager.activeState
        if(state is ClientState) {
            state.render()
        }
    }

    override fun update() {
        ClientStateManager.activeState.update()
    }

    override fun createGame() {
        logger.info("Creating new game")
        ClientStateManager.setState(ClientGameState())
    }

    override fun getStateManager(): StateManager = ClientStateManager
    override fun getRegistryManager(): RegistryManager = ClientRegistryManager
}