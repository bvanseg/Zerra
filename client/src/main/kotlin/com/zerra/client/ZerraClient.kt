package com.zerra.client

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.render.GameWindow
import com.zerra.client.state.ClientState
import com.zerra.client.state.ClientStateManager
import com.zerra.client.state.LoadingState
import com.zerra.client.state.TestRenderState
import com.zerra.client.texture.TextureManager
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.Zerra
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import com.zerra.common.util.Reloadable
import org.lwjgl.opengl.GL11C.glClearColor
import org.lwjgl.opengl.GL33C
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ZerraClient private constructor() : Zerra(), Reloadable {

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
        GameWindow.setVsync(true)

        glClearColor(1f, 1f, 1f, 1f)

        // Initial load, no async
        TextureManager.load(Runnable::run, Runnable::run).join()
    }

    override fun cleanup() {
        ClientStateManager.activeState.dispose()
        TextureManager.free()
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
        ClientStateManager.setState(LoadingState { ClientStateManager.setState(TestRenderState()) }) // TODO temporary
//        ClientStateManager.setState(TestRenderState(textureManager, getResourceManager()))
    }

    override fun getStateManager(): StateManager = ClientStateManager
    override fun getRegistryManager(): ClientRegistryManager = ClientRegistryManager

    // TODO add ability for mods to add reloading resources
    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(TextureManager.reload(backgroundExecutor, mainExecutor))
    }
}