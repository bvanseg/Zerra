package com.zerra.client.texture;

import bvanseg.kotlincommons.any.getLogger
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.zerra.common.util.Reloadable
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceLocation
import org.lwjgl.system.NativeResource
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.stream.StreamSupport
import kotlin.collections.HashMap

object TextureManager : Reloadable, NativeResource {

    private val logger = getLogger()
    private val textures = HashMap<ResourceLocation, Texture>()
    private val mainTextures = arrayOf(MasterResourceManager.createResourceLocation("textures/loading.png"))

    private fun preload(location: ResourceLocation) {
        textures[location] = SimpleTexture(location)
    }

    private fun reloadTextures(reloadMain: Boolean, backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<Void> {
        return CompletableFuture.allOf(*textures.filter { reloadMain || !mainTextures.contains(it.key) }.map {
            it.value.reload(backgroundExecutor, mainExecutor)
        }.toTypedArray())
    }

    internal fun load(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            mainTextures.forEach { preload(ResourceLocation(it)) }
        }, mainExecutor).thenRunAsync({ reloadTextures(true, backgroundExecutor, mainExecutor).join() }, backgroundExecutor)
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

    override fun reload(backgroundExecutor: Executor, mainExecutor: Executor): CompletableFuture<*> {
        return CompletableFuture.runAsync({
            textures.entries.removeIf { !mainTextures.contains(it.key) }
        }, mainExecutor).thenApplyAsync({
            MasterResourceManager.getAllResourceLocations { it == "textures/preload.json" }.stream().map { location ->
                try {
                    location.use {
                        InputStreamReader(it!!.inputStream!!).use { reader ->
                            val array = JsonParser.parseReader(reader).asJsonArray
                            for (element in array)
                                if (!element.isJsonPrimitive || !element.asJsonPrimitive.isString)
                                    throw JsonSyntaxException("Expected all elements to be primitive string")
                            return@map StreamSupport.stream(array.spliterator(), false).map { element ->
                                location.resourceManager.createResourceLocation(element.asString)
                            }
                        }
                    }
                } catch (e: Exception) {
                    logger.error("Failed to load preloaded textures from $location", e)
                }
                return@map null
            }.filter(Objects::nonNull).flatMap { it }.distinct()
        }, backgroundExecutor).thenAcceptAsync({
            it.forEach { location -> preload(ResourceLocation(location!!)) }
        }, mainExecutor)
            .thenRunAsync({ reloadTextures(false, backgroundExecutor, mainExecutor).join() }, backgroundExecutor)
            .thenRunAsync({ logger.debug("Loaded ${textures.size} preloaded textures") }, mainExecutor)
    }

    override fun free() {
        MissingTexture.free()
        textures.values.forEach(NativeResource::free)
        textures.clear()
    }
}
