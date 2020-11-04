package com.zerra.client.texture;

import bvanseg.kotlincommons.any.getLogger
import com.google.gson.JsonParser
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import org.lwjgl.system.NativeResource
import java.io.InputStreamReader
import java.util.concurrent.CompletableFuture

class TextureManager(private val resourceManager: ResourceManager) : NativeResource {

    private val textures = HashMap<ResourceLocation, Texture>()

    override fun free() {
        textures.values.forEach(NativeResource::free)
        textures.clear()
    }

    fun loadTexture(location: ResourceLocation, texture: Texture) {
        textures[location]?.free()
        texture.load(resourceManager, this, Runnable::run, Runnable::run).join()
        textures[location] = texture
    }

    fun bind(location: ResourceLocation) {
        if (!textures.containsKey(location))
            loadTexture(location, SimpleTexture(location))
        textures[location]?.bind()
    }

    // TODO make async
    fun load() {
        MasterResourceManager.getAllResourceLocations { it == "textures/preload.json" }.forEach {
            try {
                it.inputStream?.use { stream ->
                    InputStreamReader(stream).use { reader ->
                        JsonParser.parseReader(reader).asJsonArray.forEach { element ->
                            val location = it.resourceManager.createResourceLocation(element.asString)
                            textures[location] = SimpleTexture(location)
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error("Failed to load preloaded textures from $it", e)
            }
        }
        CompletableFuture.allOf(*textures.values.stream().map {
            it.load(resourceManager, this, Runnable::run, Runnable::run)
        }.toArray { size -> arrayOfNulls<CompletableFuture<Void>>(size) }).join()
    }

    companion object {

        private val logger = getLogger()
    }
}
