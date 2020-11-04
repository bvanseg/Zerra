package com.zerra.client.vertex;

import com.zerra.client.util.Bindable
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30C.*
import org.lwjgl.system.NativeResource

class VertexArray(private val vao: Int, private val vbos: IntArray, private val attributes: IntArray, private val vertexCount: Int, private val indicesType: Int) : Bindable, NativeResource {

    fun render(mode: Int = GL_TRIANGLES) {
        use {
            if (indicesType != -1) {
                glDrawElements(mode, vertexCount, indicesType, 0)
            } else {
                glDrawArrays(mode, 0, vertexCount)
            }
        }
    }

    override fun bind() {
        glBindVertexArray(vao)
        attributes.forEach(GL30::glEnableVertexAttribArray)
    }

    override fun unbind() {
        attributes.forEach(GL30::glDisableVertexAttribArray)
        glBindVertexArray(0)
    }

    override fun free() {
        glDeleteVertexArrays(vao)
        vbos.forEach(GL15::glDeleteBuffers)
    }
}
