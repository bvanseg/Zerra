package com.zerra.client.state

import com.zerra.client.render.GameWindow
import com.zerra.client.shader.Shader
import com.zerra.client.texture.TextureManager
import com.zerra.client.vertex.VertexArray
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.util.TransformationHelper
import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.GL_FLOAT
import org.lwjgl.opengl.GL33C.glClearColor

class TestRenderState(private val textureManager: TextureManager, resourceManager: ResourceManager) : ClientState {

    private val testShader = Shader(ResourceLocation(resourceManager, "zerra", "test"))
    private val testTextureLocation = resourceManager.createResourceLocation("textures/b5fca2fe-313d-4d53-a16a-6c856c7da7e3.jpg")
    private var vao: VertexArray? = null
    private var test = 0

    override fun render(partialTicks: Float) {
        testShader.use {
            testShader.loadMatrix4f("projection", Matrix4f().perspective(45f, GameWindow.framebufferWidth.toFloat() / GameWindow.framebufferHeight.toFloat(), 0.3f, 10000.0f))
            testShader.loadMatrix4f("transformation", TransformationHelper.get().translate(0f, 0f, -1f).rotate((test + partialTicks).toDouble() / 5, 0f, 1f, 0f).value())
            testShader.loadFloat("counter", test + partialTicks)

            textureManager.bind(testTextureLocation)
            vao?.render()
        }
    }

    override fun init() {
        testShader.load()

//        glBindVertexArray(vao)
//
//        glBindBuffer(GL_ARRAY_BUFFER, vbo1)
//        glBufferData(GL_ARRAY_BUFFER, floatArrayOf(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f), GL_DYNAMIC_DRAW)
//        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0L)
//        glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 8L * Float.SIZE_BYTES)
//        glBindBuffer(GL_ARRAY_BUFFER, 0)
//
//        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo2)
//        glBufferData(GL_ELEMENT_ARRAY_BUFFER, shortArrayOf(2, 1, 0, 3, 2, 0), GL_STATIC_DRAW)
//
//        glBindVertexArray(0)

        VertexBuilder.reset().segment(GL_FLOAT, 2)
        VertexBuilder.put(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, vertexData = true)
        VertexBuilder.segment(GL_FLOAT, 3)
        VertexBuilder.put(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        VertexBuilder.segment(GL_FLOAT, 2)
        VertexBuilder.put(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f)
        VertexBuilder.indices(2, 1, 0, 3, 2, 0)
        vao = VertexBuilder.compile()

//        glEnable(GL_CULL_FACE)
//        glCullFace(GL_BACK)
        glClearColor(1f, 1f, 1f, 1f)
    }

    override fun update() {
        test++
    }

    override fun dispose() {
        testShader.free()
        vao?.free()
    }
}