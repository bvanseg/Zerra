package com.zerra.client.texture

import com.zerra.client.util.Bindable
import com.zerra.common.util.resource.ResourceManager
import org.lwjgl.system.NativeResource
import java.util.concurrent.CompletableFuture

interface Texture : Bindable, NativeResource {
    fun load(resourceManager: ResourceManager, textureManager: TextureManager): CompletableFuture<Void>
}