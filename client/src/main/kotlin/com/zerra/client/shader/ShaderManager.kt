package com.zerra.client.shader

import bvanseg.kotlincommons.any.getLogger
import com.zerra.common.util.Reloadable
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.system.NativeResource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object ShaderManager : Reloadable, NativeResource {

    private val logger = getLogger()
    private val shaders = HashMap<ResourceLocation, Shader>()
    private val mainShaders = arrayOf(MasterResourceManager.createResourceLocation("quad"))

    private fun preload(shaderLocation: ResourceLocation) {
        shaders[shaderLocation] = Shader(shaderLocation)
    }

    private fun reloadShaders(reloadMain: Boolean, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(*shaders.filter { reloadMain || !mainShaders.contains(it.key) }.map {
            it.value.reload(backgroundExecutor, mainExecutor)
        }.toTypedArray())
    }

    internal fun load(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            mainShaders.forEach { preload(ResourceLocation(it)) }
        }, mainExecutor).thenRunAsync({ reloadShaders(true, backgroundExecutor, mainExecutor).join() }, backgroundExecutor)
    }

    fun getShader(shaderLocation: ResourceLocation): Shader {
        return shaders[shaderLocation] ?: Shader.MISSING
    }

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            shaders.entries.removeIf { !mainShaders.contains(it.key) }
        }, mainExecutor)
            .thenApplyAsync({
                MasterResourceManager.getAllResourceLocations { it.startsWith("shaders/") && it.endsWith(".glsl") }.map { it.resourceManager.createResourceLocation(it.location.substring(8, it.location.length - 10)) }.filter { !mainShaders.contains(it) }.distinct()
            }, backgroundExecutor).thenAcceptAsync({
                it.forEach { location -> preload(ResourceLocation(location)) }
            }, mainExecutor).thenRunAsync({
                reloadShaders(false, backgroundExecutor, mainExecutor).join()
            }, backgroundExecutor).thenRunAsync({ logger.debug("Loaded ${shaders.size} shaders") }, mainExecutor)
    }

    override fun free() {
        shaders.values.forEach(NativeResource::free)
        shaders.clear()
    }
}