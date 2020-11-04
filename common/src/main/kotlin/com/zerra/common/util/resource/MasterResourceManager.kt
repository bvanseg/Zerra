package com.zerra.common.util.resource

import bvanseg.kotlincommons.bool.ifTrue
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import java.util.function.Predicate

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object MasterResourceManager : ResourceManager("", "master") {

    private val resourceManagers = hashMapOf<String, ResourceManager>()

    private var scanInitialized = false

    lateinit var resources: ScanResult

    init {
        resourceManagers[domain] = this
    }

    fun scanResources(forceScan: Boolean = false, classGraphSupplier: ClassGraph? = null) {
        logger.info("Scanning all resources...")
        val start = System.currentTimeMillis()
        if (!scanInitialized || forceScan) {
            scanInitialized = true
            resources = (classGraphSupplier ?: ClassGraph().enableAllInfo()).scan()
        }
        logger.info("Resource scan finished in ${System.currentTimeMillis() - start}ms")
    }

    private fun close() = scanInitialized.ifTrue { resources.close() }

    fun createResourceManager(root: String, domain: String): ResourceManager {
        val lowerDomain = domain.toLowerCase()

        if (resourceManagers[lowerDomain] != null) {
            throw IllegalStateException("Attempted to register a domain that already exists: $domain")
        }

        val manager = ResourceManager(root, domain)

        resourceManagers[domain] = manager

        return manager
    }

    fun getAllResourceLocations(): Collection<ResourceLocation> {
        val locations = HashSet<ResourceLocation>()
        resourceManagers.values.forEach { locations.addAll(it.getResourceLocations()) }
        return locations
    }

    fun getAllResourceLocations(predicate: Predicate<String>): Collection<ResourceLocation> {
        val locations = HashSet<ResourceLocation>()
        resourceManagers.values.forEach { locations.addAll(it.getResourceLocations(predicate)) }
        return locations
    }

    fun getResourceManager(domain: String) = resourceManagers[domain.toLowerCase()]
    fun getAllResourceManagers(): Collection<ResourceManager> = resourceManagers.values

    fun createResourceLocation(domain: String, location: String) = ResourceLocation(this, domain, location)
    // This doesn't make any sense? Why should you be able to access files under another manager if it isn't "ours"
//    fun createResourceLocation(root: String, domain: String, location: String) = ResourceLocation(this, root + domain, location)
}