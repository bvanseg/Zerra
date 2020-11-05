package com.zerra.client.texture

import bvanseg.kotlincommons.any.getLogger
import com.zerra.common.util.Reloadable
import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import org.lwjgl.opengl.GL11C.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

open class SimpleTexture(private val location: ResourceLocation) : Texture {

    protected var texture = 0

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.supplyAsync({
            try {
                return@supplyAsync TextureLoader.load(ResourceLocation(location))
            } catch (e: Exception) {
                logger.error("Failed to load texture from $location", e)
            }
            null
        }, backgroundExecutor).thenAcceptAsync({
            if (texture == 0) {
                texture = glGenTextures()
                bind()
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            }

            bind()
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, it?.width ?: MissingTexture.width, it?.height ?: MissingTexture.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, it?.data ?: MissingTexture.createData())
            it?.free()
            logger.debug("Loaded texture from $location")
        }, mainExecutor)
    }

    override fun bind() {
        glBindTexture(GL_TEXTURE_2D, texture)
    }

    override fun free() {
        if (texture != 0) {
            glDeleteTextures(texture)
            texture = 0
        }
    }

    companion object {
        val logger = getLogger()
    }
}