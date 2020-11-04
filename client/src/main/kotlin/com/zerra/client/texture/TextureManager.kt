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

    private fun preload(location: ResourceLocation) {
        println("TODO preload $location")
    }

    override fun free() {
        textures.values.forEach(NativeResource::free)
        textures.clear()
    }

    // TODO make async
    fun load() {
        MasterResourceManager.getAllResourceLocations { it == "textures/preload.json" }.forEach {
            try {
                it.inputStream?.use { stream ->
                    InputStreamReader(stream).use { reader ->
                        JsonParser.parseReader(reader).asJsonArray.forEach { element ->
                            preload(it.resourceManager.createResourceLocation(element.asString))
                        }
                    }
                }
            } catch (e: Exception) {
                logger.error("Failed to load preloaded textures from $it", e)
            }
        }
        CompletableFuture.allOf(*textures.values.stream().map { it.load(resourceManager, this) }.toArray { size -> arrayOfNulls<CompletableFuture<Void>>(size) }).join()
    }

    companion object {

        private val logger = getLogger()
    }
}
