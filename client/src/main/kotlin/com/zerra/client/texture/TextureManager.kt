package com.zerra.client.texture;

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.stream.stream
import com.google.gson.JsonParser
import com.zerra.common.util.Reloadable
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.system.NativeResource
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.collections.HashMap
import kotlin.streams.toList

object TextureManager : Reloadable, NativeResource {

    private val logger = getLogger()
    private val textures = HashMap<ResourceLocation, Texture>()
    private val mainTextures = arrayOf(MasterResourceManager.createResourceLocation("textures/loading.png"))

    private fun preload(location: ResourceLocation, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(CompletableFuture.runAsync({ textures[location] = SimpleTexture(location) }, mainExecutor))
    }

    private fun reloadTextures(reloadMain: Boolean, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(*textures.entries.stream().filter { reloadMain || !mainTextures.contains(it.key) }.map { it.value.reload(backgroundExecutor, mainExecutor) }.toList().toTypedArray())
    }

    internal fun load(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(CompletableFuture.allOf(*mainTextures.stream().map { preload(ResourceLocation(it), backgroundExecutor) }.toList().toTypedArray()))
            .whenComplete { _, _ -> reloadTextures(true, backgroundExecutor, mainExecutor) }
    }

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            textures.entries.removeIf { !mainTextures.contains(it.key) }
        }, mainExecutor).thenApplyAsync({
            MasterResourceManager.getAllResourceLocations { it == "textures/preload.json" }.stream().map { location ->
                try {
                    location.use {
                        InputStreamReader(it!!.inputStream!!).use { reader ->
                            JsonParser.parseReader(reader).asJsonArray.forEach { element ->
                                return@map location.resourceManager.createResourceLocation(element.asString)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Failed to load preloaded textures from $location", e)
                }
                return@map null
            }.filter(Objects::nonNull).distinct()
        }, backgroundExecutor)
            .whenComplete { it, _ -> CompletableFuture.allOf(*it.map { location -> preload(location!!, backgroundExecutor) }.toList().toTypedArray())
                .whenComplete { _, _ -> reloadTextures(false, backgroundExecutor, mainExecutor)
                    .thenRunAsync({ logger.debug("Loaded preloaded textures") }, mainExecutor) } }
    }

    override fun free() {
        MissingTexture.free()
        textures.values.forEach(NativeResource::free)
        textures.clear()
    }

    fun loadTexture(location: ResourceLocation, texture: Texture) {
        textures[location]?.free()
        texture.reload(Runnable::run, Runnable::run).join()
        textures[location] = texture
    }

    fun bind(location: ResourceLocation) {
        if (!textures.containsKey(location))
            loadTexture(location, SimpleTexture(location))
        textures[location]?.bind()
    }
}
