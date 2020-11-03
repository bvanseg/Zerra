package com.zerra.server

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.time.api.every
import bvanseg.kotlincommons.time.api.milliseconds
import com.zerra.common.api.registry.RegistryManager
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import com.zerra.server.entity.ServerEntityPlayer
import com.zerra.server.state.ServerGameState
import com.zerra.server.state.ServerStateManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ZerraServer private constructor(): Zerra() {

    companion object {
        private var instance: ZerraServer? = null

        private val logger = getLogger()

        /**
         * Gets a server-sided instance of Zerra.
         */
        fun getInstance(): ZerraServer {
            if(instance == null) {
                instance = ZerraServer()

                if(zerraInstance == null) {
                    zerraInstance = instance
                }
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.SERVER)
    }

    override fun init() {
        logger.info("Starting up ZerraServer")
        ServerRegistryManager.ENTITY_REGISTRY.register(ServerEntityPlayer::class)

        initServer()

        every((1000 / TICKS_PER_SECOND).milliseconds) {
            this.update()
        }.execute()
    }

    private fun initServer() {
        // TODO
    }

    override fun createGame() {
        logger.info("Creating new game")
        ServerStateManager.setState(ServerGameState())
    }

    override fun update() {
        getStateManager().activeState.update()
    }

    override fun getStateManager(): StateManager = ServerStateManager
    override fun getRegistryManager(): RegistryManager = ServerRegistryManager

    override fun cleanup() {
        // TODO
    }
}