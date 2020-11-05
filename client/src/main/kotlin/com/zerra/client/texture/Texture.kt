package com.zerra.client.texture

import com.zerra.common.util.Reloadable
import org.lwjgl.system.NativeResource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

interface Texture : Reloadable, NativeResource {
    fun bind()
}