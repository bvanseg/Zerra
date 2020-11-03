package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.render.GameWindow
import com.zerra.client.state.ClientState
import com.zerra.client.state.ClientStateManager
import com.zerra.client.state.TestRenderState
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import org.lwjgl.opengl.GL33C
import org.lwjgl.system.NativeResource

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ZerraClient private constructor() : Zerra() {

    companion object {
        private var instance: ZerraClient? = null

        val logger = getLogger()

        /**
         * Gets a client-sided instance of Zerra.
         */
        fun getInstance(): ZerraClient {
            if (instance == null) {
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
        super.init()

        logger.info("Starting up ZerraClient")
        initClient()
    }

    fun initClient() {
        // Create window
        GameWindow.init()
        GameWindow.create(1280, 720, "Zerra")
    }

    override fun cleanup() {
        ClientStateManager.activeState.dispose()
    }

    fun render() {
        val error = GL33C.glGetError()
        if (error != GL33C.GL_NO_ERROR)
            logger.warn("OpenGL Error: 0x${Integer.toHexString(error)}")

        val state = ClientStateManager.activeState
        if (state is ClientState) {
            state.render()
        }
    }

    override fun update() {
        ClientStateManager.activeState.update()
    }

    override fun createGame() {
        logger.info("Creating new game")
//        ClientStateManager.setState(ClientGameState()) TODO temporary
        ClientStateManager.setState(TestRenderState(getResourceManager()))
    }

    override fun getStateManager(): StateManager = ClientStateManager
    override fun getRegistryManager(): ClientRegistryManager = ClientRegistryManager
}