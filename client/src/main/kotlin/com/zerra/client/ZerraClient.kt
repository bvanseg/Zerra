package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.render.GameWindow
import com.zerra.client.state.ClientGameState
import com.zerra.client.shader.ShaderManager
import com.zerra.client.state.ClientState
import com.zerra.client.state.ClientStateManager
import com.zerra.client.state.LoadingState
import com.zerra.client.state.TestRenderState
import com.zerra.client.texture.TextureManager
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.server.ZerraServer
import org.lwjgl.opengl.GL11C.glClearColor
import org.lwjgl.opengl.GL33C
import java.util.concurrent.CompletableFuture

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ZerraClient private constructor() : Zerra() {

    companion object {
        private var instance: ZerraClient? = null

        val logger = getLogger()

        fun printOpenGLError() {
            val error = GL33C.glGetError()
            if (error != GL33C.GL_NO_ERROR)
                logger.warn("OpenGL Error: 0x${Integer.toHexString(error)}")
        }

        /**
         * Gets a client-sided instance of Zerra.
         */
        fun getInstance(): ZerraClient {
            if (instance == null) {
                logger.info("Creating ZerraClient instance")
                instance = ZerraClient()

                if(zerraClientInstance == null) {
                    zerraClientInstance = instance
                }
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.CLIENT)
        reloader.add(TextureManager)
        reloader.add(ShaderManager)
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
        GameWindow.setVsync(true)

        glClearColor(1f, 1f, 1f, 1f)

        // Initial load, no async
        CompletableFuture.allOf(
            TextureManager.load(Runnable::run, Runnable::run),
            ShaderManager.load(Runnable::run, Runnable::run)
        ).join()
        logger.info("Finished initial loading")
    }

    override fun cleanup() {
        ClientStateManager.activeState.dispose()
        TextureManager.free()
        ShaderManager.free()
        VertexBuilder.free()
    }

    fun render(partialTicks: Float) {
        printOpenGLError()

        val state = ClientStateManager.activeState
        if (state is ClientState) {
            state.render(partialTicks)
        }
    }

    override fun update() {
        ClientStateManager.activeState.update()
    }

    override fun createGame() {
        logger.info("Creating new game")
        ClientStateManager.setState(LoadingState(reloader) { ClientStateManager.setState(TestRenderState()) }) // TODO temporary
//        ClientStateManager.setState(TestRenderState(textureManager, getResourceManager()))
    }

    override fun getStateManager(): StateManager = ClientStateManager
    override fun getRegistryManager(): ClientRegistryManager = ClientRegistryManager

}