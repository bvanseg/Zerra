package com.zerra.client.state

import com.zerra.client.render.GameWindow
import com.zerra.client.shader.Shader
import com.zerra.client.vertex.VertexBuilder
import com.zerra.common.util.TransformationHelper
import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import org.joml.Matrix4f
import org.lwjgl.opengl.GL33C.*
import kotlin.math.sin

class TestRenderState(resourceManager: ResourceManager) : ClientState {

    private val testShader: Shader = Shader(ResourceLocation(resourceManager, "zerra", "test"))
    private var vao = 0
    private var vbo1 = 0
    private var vbo2 = 0
    private var test = 0

    override fun render() {
        testShader.use {
            testShader.loadMatrix4f("projection", Matrix4f().perspective(45f, GameWindow.framebufferWidth.toFloat() / GameWindow.framebufferHeight.toFloat(), 0.3f, 10000.0f))
            testShader.loadMatrix4f("transformation", TransformationHelper.get().translate(0f, 0f, -2f).rotate(test.toDouble(), 0f, 1f, 0f).value())

            glBindVertexArray(vao)
            glEnableVertexAttribArray(0)
            glEnableVertexAttribArray(1)
            glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0)
            glDisableVertexAttribArray(1)
            glDisableVertexAttribArray(0)
            glBindVertexArray(0)
        }
    }

    override fun init() {
        testShader.load()

        vao = glGenVertexArrays()
        vbo1 = glGenBuffers()
        vbo2 = glGenBuffers()

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
        VertexBuilder.put(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f)
        VertexBuilder.segment(GL_FLOAT, 3)
        VertexBuilder.put(0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f)
        VertexBuilder.indices(2, 1, 0, 3, 2, 0)
        VertexBuilder.copy(vao, vbo1, vbo2)

//        glEnable(GL_CULL_FACE)
//        glCullFace(GL_BACK)
        glClearColor(1f, 1f, 1f, 1f)
    }

    override fun update() {
        test++
    }

    override fun dispose() {
        glDeleteBuffers(vbo1)
        glDeleteBuffers(vbo2)
        glDeleteVertexArrays(vao)
        testShader.free()
    }
}