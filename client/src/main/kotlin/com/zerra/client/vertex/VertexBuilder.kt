package com.zerra.client.vertex

import com.zerra.client.ZerraClient
import com.zerra.client.util.HardwareConstraints
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.NativeResource
import java.nio.ByteBuffer
import java.util.*

object VertexBuilder : NativeResource {

    private val segments = LinkedList<Segment>()
    private var dataBuffer = BufferUtils.createByteBuffer(256)
    private var indicesBuffer = BufferUtils.createByteBuffer(256)
    private var currentType = -1
    private var vertexSize = -1
    private var vao = -1
    private var vbo = -1
    private var indicesVbo = -1

    var indicesType = -1
        private set
    var vertexCount = 0

    private fun getName(type: Int): String {
        return when (type) {
            GL_BYTE -> "Byte"
            GL_UNSIGNED_BYTE -> "Unsigned Byte"
            GL_SHORT -> "Short"
            GL_UNSIGNED_SHORT -> "Unsigned Short"
            GL_INT -> "Int"
            GL_UNSIGNED_INT -> "Unsigned Int"
            GL_FLOAT -> "Float"
            GL_DOUBLE -> "Double"
            else -> "INVALID_VALUE"
        }
    }

    private fun growBuffer(buffer: ByteBuffer, increaseAmount: Int): ByteBuffer {
        if (increaseAmount > buffer.capacity()) {
            val bytebuffer = BufferUtils.createByteBuffer(buffer.capacity() + increaseAmount)
            buffer.position(0)
            bytebuffer.put(buffer)
            bytebuffer.rewind()
            return bytebuffer
        }
        return buffer
    }

    private fun put(data: Number) {
        when (currentType) {
            GL_BYTE, GL_UNSIGNED_BYTE -> {
                growBuffer(dataBuffer, Byte.SIZE_BYTES)
                dataBuffer.put(data.toByte())
            }
            GL_SHORT, GL_UNSIGNED_SHORT -> {
                growBuffer(dataBuffer, Short.SIZE_BYTES)
                dataBuffer.putShort(data.toShort())
            }
            GL_INT, GL_UNSIGNED_INT -> {
                growBuffer(dataBuffer, Int.SIZE_BYTES)
                dataBuffer.putInt(data.toInt())
            }
            GL_FLOAT -> {
                growBuffer(dataBuffer, Float.SIZE_BYTES)
                dataBuffer.putFloat(data.toFloat())
            }
            GL_DOUBLE -> {
                growBuffer(dataBuffer, Double.SIZE_BYTES)
                dataBuffer.putDouble(data.toDouble())
            }
        }
    }

    private fun putIndex(data: Number) {
        when (indicesType) {
            GL_BYTE, GL_UNSIGNED_BYTE -> {
                growBuffer(indicesBuffer, Byte.SIZE_BYTES)
                indicesBuffer.put(data.toByte())
            }
            GL_SHORT, GL_UNSIGNED_SHORT -> {
                growBuffer(indicesBuffer, Short.SIZE_BYTES)
                indicesBuffer.putShort(data.toShort())
            }
            GL_INT, GL_UNSIGNED_INT -> {
                growBuffer(indicesBuffer, Int.SIZE_BYTES)
                indicesBuffer.putInt(data.toInt())
            }
        }
    }

    fun reset(): VertexBuilder {
        currentType = -1
        indicesType = -1
        vertexCount = 0
        vertexSize = -1
        segments.clear()
        dataBuffer.clear()
        indicesBuffer.clear()
        return this
    }

    fun segment(type: Int, dataSize: Byte): VertexBuilder {
        if (dataSize <= 0)
            throw IllegalArgumentException("Data size must be greater than zero")
        if (segments.size + 1 > HardwareConstraints.maxVertexAttributes)
            throw IllegalArgumentException("Maximum vertex attributes ${HardwareConstraints.maxVertexAttributes} exceeded")

        segments.add(Segment(dataBuffer.position(), type, dataSize))
        currentType = type
        return this
    }

    fun put(vararg data: Number, vertexData: Boolean = false): VertexBuilder {
        if (currentType == -1)
            throw IllegalStateException("No Segments specified")
        data.forEach(this::put)
        if (vertexData && vertexSize != 0) {
            if (vertexSize == -1)
                vertexSize = segments[segments.size - 1].dataSize.toInt()
            vertexCount += data.size
        }
        return this
    }

    fun indices(vararg data: Number, type: Int = GL_UNSIGNED_SHORT): VertexBuilder {
        if (indicesType != -1)
            throw IllegalArgumentException("Indices type ${getName(indicesType)} is already in use. Cannot use ${getName(type)}")
        if (indicesType == GL_FLOAT || indicesType == GL_DOUBLE)
            throw IllegalArgumentException("Indices can only be ${getName(GL_FLOAT)} or ${getName(GL_DOUBLE)}")
        indicesType = type
        if (vertexSize > 0) {
            vertexCount = 0
            vertexSize = 0
        }
        vertexCount += data.size
        data.forEach(this::putIndex)
        return this
    }

    fun render(mode: Int = GL_TRIANGLES): VertexBuilder {
        if (vertexSize == -1)
            throw IllegalStateException("No vertex count specified")

        val useIndices = indicesBuffer.position() > 0
        if (vao == -1)
            vao = glGenVertexArrays()
        if (vbo == -1)
            vbo = glGenBuffers()
        if (indicesVbo == -1 && useIndices)
            indicesVbo = glGenBuffers()

        copy(vao, vbo, if (useIndices) indicesVbo else -1)

        glBindVertexArray(vao)
        for (i in segments.indices)
            glEnableVertexAttribArray(i)

        if (useIndices) {
            glDrawElements(mode, vertexCount, indicesType, 0L)
        } else {
            glDrawArrays(mode, 0, vertexCount / vertexSize)
        }

        for (i in segments.indices)
            glDisableVertexAttribArray(i)
        glBindVertexArray(0)

        return this
    }

    fun copy(vao: Int, vbo: Int, indicesVbo: Int = -1, attributeOffset: Int = 0): VertexBuilder {
        glBindVertexArray(vao)

        dataBuffer.flip()
        indicesBuffer.flip()

        glBindBuffer(GL_ARRAY_BUFFER, vbo)
        glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_DYNAMIC_DRAW)
        for (i in segments.indices) {
            val segment = segments[i]
            glVertexAttribPointer(attributeOffset + i, segment.dataSize.toInt(), segment.type, false, 0, segment.position.toLong())
        }
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        if (indicesVbo != -1) {
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVbo)
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_DYNAMIC_DRAW)
        }

        glBindVertexArray(0)
        return this
    }

    override fun free() {
        if (vbo != -1) {
            glDeleteBuffers(vbo)
            vbo = -1
        }
        if (indicesVbo != -1) {
            glDeleteBuffers(indicesVbo)
            indicesVbo = -1
        }
        if (vao != -1) {
            glDeleteVertexArrays(vao)
            vao = -1
        }
    }

    private data class Segment(val position: Int, val type: Int, val dataSize: Byte)
}