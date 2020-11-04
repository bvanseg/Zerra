package com.zerra.client.texture;

import com.zerra.common.util.resource.ResourceLocation
import com.zerra.common.util.resource.ResourceManager
import java.util.concurrent.CompletableFuture

class TextureManager(private val resourceManager: ResourceManager) {

    private val textures = HashMap<ResourceLocation, Texture>()

    // TODO make async
    fun load() {
        CompletableFuture.allOf(*textures.values.stream().map(Texture::s).toArray { size -> arrayOfNulls<CompletableFuture<Void>>(size) }).join()
    }
}
