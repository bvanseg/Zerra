package com.zerra.client.shader

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.stream.stream
import com.zerra.common.util.Reloadable
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.system.NativeResource
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.streams.toList

object ShaderManager : Reloadable, NativeResource {

    private val logger = getLogger()
    private val shaders = HashMap<ResourceLocation, Shader>()
    private val mainShaders = arrayOf(MasterResourceManager.createResourceLocation("quad"))

    private fun loadShader(shaderLocation: ResourceLocation, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({ shaders[shaderLocation] = Shader(shaderLocation) }, mainExecutor)
    }

    private fun reloadShaders(reloadMain: Boolean, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.allOf(*shaders.entries.stream().filter { reloadMain || !mainShaders.contains(it.key) }.map { it.value.reload(backgroundExecutor, mainExecutor) }.toList().toTypedArray())
    }

    internal fun load(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.allOf(CompletableFuture.allOf(*mainShaders.stream().map { loadShader(ResourceLocation(it), backgroundExecutor) }.toList().toTypedArray()))
            .whenCompleteAsync({ _, _ -> reloadShaders(true, backgroundExecutor, mainExecutor) }, backgroundExecutor)
    }

    fun getShader(shaderLocation: ResourceLocation): Shader {
        return shaders[shaderLocation] ?: Shader.MISSING
    }

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            shaders.entries.removeIf { !mainShaders.contains(it.key) }
        }, mainExecutor).thenApplyAsync({
            MasterResourceManager.getAllResourceLocations { it.startsWith("shaders/") }.map { it.resourceManager.createResourceLocation(it.location.substring(8, it.location.length - 10)) }.distinct()
        }, backgroundExecutor)
            .whenCompleteAsync({ it, _ ->
                CompletableFuture.allOf(*it.map { location -> loadShader(location, backgroundExecutor) }.toList().toTypedArray())
                    .whenCompleteAsync({ _, _ ->
                        reloadShaders(false, backgroundExecutor, mainExecutor)
                            .thenRunAsync({ logger.debug("Loaded shaders") }, mainExecutor)
                    }, backgroundExecutor)
            }, backgroundExecutor)
    }

    override fun free() {
        shaders.values.forEach(NativeResource::free)
        shaders.clear()
    }
}