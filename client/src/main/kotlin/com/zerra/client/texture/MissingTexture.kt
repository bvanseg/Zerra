package com.zerra.client.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11C.*
import java.nio.ByteBuffer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object MissingTexture : Texture {

    const val width = 16
    const val height = 16
    private var texture = -1

    fun createData(): ByteBuffer {
        val buffer = BufferUtils.createByteBuffer(768)
        for (y in 0 until 16) {
            for (x in 0 until 16) {
                val flag = (x < 8 && y < 8) || (x > 8 && y > 8)
                buffer.put((if (flag) 0x00 else 0xff).toByte())
                buffer.put(0x00.toByte())
                buffer.put((if (flag) 0x00 else 0xff).toByte())
            }
        }
        buffer.flip()
        return buffer
    }

    override fun bind() {
        if (texture == -1) {
            texture = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, texture)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 16, 16, 0, GL_RGB, GL_UNSIGNED_BYTE, createData())
            return
        }
        glBindTexture(GL_TEXTURE_2D, texture)
    }

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.completedFuture(null)
    }

    override fun free() {
        if (texture != -1) {
            glDeleteTextures(texture)
            texture = -1
        }
    }
}