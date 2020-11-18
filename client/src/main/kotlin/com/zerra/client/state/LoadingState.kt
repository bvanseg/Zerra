package com.zerra.client.state

import bvanseg.kotlincommons.any.getLogger
import com.zerra.client.ZerraClient
import com.zerra.client.shader.ShaderManager
import com.zerra.client.texture.TextureManager
import com.zerra.client.vertex.VertexArray
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.util.ResourceReloader
import com.zerra.common.util.resource.MasterResourceManager
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.GL_FLOAT
import java.util.concurrent.CompletableFuture

class LoadingState(private val reloader: ResourceReloader, private val completeCallback: () -> Unit) : ClientState {

    private val testShader = ShaderManager.getShader(MasterResourceManager.createResourceLocation("quad"))
    private val testTextureLocation = MasterResourceManager.createResourceLocation("textures/loading.png")
    private val vao: VertexArray

    private var loadingTask: CompletableFuture<*> = CompletableFuture.completedFuture(null)

    init {
        VertexBuilder.reset().segment(GL_FLOAT, 2)
        VertexBuilder.put(0, 0, 0, 1, 1, 1, 1, 0, vertexData = true)
        VertexBuilder.segment(GL_FLOAT, 2)
        VertexBuilder.put(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f)
        VertexBuilder.indices(2, 1, 0, 3, 2, 0)
        vao = VertexBuilder.compile()
    }

    override fun render(partialTicks: Float) {
        testShader.use {
            TextureManager.bind(testTextureLocation)
            vao.render()
        }
    }

    override fun init() {
        testShader.use {
            testShader.loadMatrix4f("projection", Matrix4f().ortho(0f, 1f, 0f, 1f, 0.3f, 1000.0f))
            testShader.loadMatrix4f("transformation", Matrix4f())
        }

        // FIXME make this work lol
        loadingTask = reloader.reload(Runnable::run, Runnable::run)
    }

    override fun update() {
        if (loadingTask.isDone && ZerraClient.getInstance().getRemainingTasks() == 0) {
            logger.info("Finished async loading")
            completeCallback.invoke()
        }
    }

    override fun dispose() {
        vao.free()
    }

    companion object {
        private val logger = getLogger()
    }
}