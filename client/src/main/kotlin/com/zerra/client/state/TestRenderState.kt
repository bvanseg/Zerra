package com.zerra.client.state

import com.zerra.client.render.GameWindow
import com.zerra.client.shader.ShaderManager
import com.zerra.client.texture.TextureManager
import com.zerra.client.vertex.VertexArray
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.util.TransformationHelper
import com.zerra.common.util.resource.MasterResourceManager
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*

class TestRenderState : ClientState {

    private val resourceManager = MasterResourceManager.getResourceManager("zerra")!!
    private val testShader = ShaderManager.getShader(resourceManager.createResourceLocation("test"))
    private val testTextureLocation = resourceManager.createResourceLocation("textures/aliencat.jpg")
    private var vao: VertexArray? = null
    private var test = 0

    override fun render(partialTicks: Float) {
        for (i in 0 until 2) {
            if (i == 1)
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE)
            testShader.use {
                testShader.loadMatrix4f("projection", Matrix4f().perspective(45f, GameWindow.framebufferWidth.toFloat() / GameWindow.framebufferHeight.toFloat(), 0.3f, 10000.0f))
                testShader.loadMatrix4f("transformation", TransformationHelper.get().translate(-0.75f + i * 1.5f, 0f, -2f).rotate((test + partialTicks).toDouble() / 5, 0f, 1f, 0f).value())
                testShader.loadFloat("counter", test + partialTicks)

                TextureManager.bind(testTextureLocation)
                vao?.render()
            }
            if (i == 1)
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL)
        }
    }

    override fun init() {
        VertexBuilder.reset().segment(GL_FLOAT, 2)
        VertexBuilder.put(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, vertexData = true)
        VertexBuilder.segment(GL_FLOAT, 3)
        VertexBuilder.put(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        VertexBuilder.segment(GL_FLOAT, 2)
        VertexBuilder.put(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f)
        VertexBuilder.indices(2, 1, 0, 3, 2, 0)
        vao = VertexBuilder.compile()
    }

    override fun update() {
        test++
    }

    override fun dispose() {
        vao?.free()
    }
}