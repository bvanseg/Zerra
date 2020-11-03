package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.render.GameWindow
import com.zerra.client.shader.Shader
import com.zerra.client.state.ClientGameState
import com.zerra.client.state.ClientState
import com.zerra.client.state.ClientStateManager
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.system.NativeResource

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ZerraClient private constructor() : Zerra(), NativeResource {

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

    val testShader: Shader = Shader(ResourceLocation(getResourceManager(), "zerra", "test"))

    override fun init() {
        super.init()

        logger.info("Starting up ZerraClient")
        initClient()
    }

    fun initClient() {
        // Create window
        GameWindow.init()
        GameWindow.create(1920, 1080, "Zerra")

        testShader.load()
    }

    override fun free() {
        testShader.free()
    }

    fun render() {
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
        ClientStateManager.setState(ClientGameState())
    }

    override fun getStateManager(): StateManager = ClientStateManager
    override fun getRegistryManager(): ClientRegistryManager = ClientRegistryManager
}