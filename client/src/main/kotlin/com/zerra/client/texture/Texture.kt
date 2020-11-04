package com.zerra.client.texture

import com.zerra.client.util.Bindable
import com.zerra.common.util.resource.ResourceManager
import java.util.concurrent.CompletableFuture

interface Texture : Bindable {
    fun load(resourceManager: ResourceManager, textureManager: TextureManager): CompletableFuture<Void>

    fun s(): CompletableFuture<Void>
}