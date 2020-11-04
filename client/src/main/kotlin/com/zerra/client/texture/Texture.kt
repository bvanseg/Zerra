package com.zerra.client.texture

import com.zerra.common.util.resource.ResourceManager
import org.lwjgl.system.NativeResource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface Texture : NativeResource {
    fun bind()

    fun load(resourceManager: ResourceManager, textureManager: TextureManager, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void>
}