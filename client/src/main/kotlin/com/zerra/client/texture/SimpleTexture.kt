package com.zerra.client.texture

import bvanseg.kotlincommons.any.getLogger
import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import org.lwjgl.opengl.GL11C.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

open class SimpleTexture(private val location: ResourceLocation) : Texture {

    private var texture = -1

    override fun load(resourceManager: ResourceManager, textureManager: TextureManager, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync({
            try {
                return@supplyAsync TextureLoader.load(location)
            } catch (e: Exception) {
                logger.error("Failed to load texture from $location", e)
            }
            null
        }, backgroundExecutor).thenAcceptAsync({
            if (texture == -1) {
                texture = glGenTextures()
                bind()
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            }

            if (it == null)
                return@thenAcceptAsync
            logger.debug("Loaded texture from $location")
            bind()
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, it.width, it.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, it.data)
            it.free()
        }, mainExecutor)
    }

    override fun bind() {
        glBindTexture(GL_TEXTURE_2D, texture)
    }

    override fun free() {
        if (texture != -1) {
            glDeleteTextures(texture)
            texture = -1
        }
    }

    companion object {
        val logger = getLogger()
    }
}