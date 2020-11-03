package com.zerra.common.util.resource

import bvanseg.kotlincommons.bool.ifTrue
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object MasterResourceManager: ResourceManager("/", "master") {

    private val resourceManagers = hashMapOf<String, ResourceManager>()

    private var scanInitialized = false

    lateinit var resources: ScanResult

    fun scanResources(forceScan: Boolean = false) {
        logger.info("Scanning all resources...")
        if (!scanInitialized || forceScan) {
            scanInitialized = true
            resources = ClassGraph().enableAllInfo().scan()
        }
        logger.info("Scan finished.")
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

    fun getResourceManager(domain: String) = resourceManagers[domain.toLowerCase()]

    fun createResourceLocation(domain: String, location: String) = ResourceLocation(this, domain, location)
    fun createResourceLocation(root: String, domain: String, location: String) = ResourceLocation(this, root + domain, location)
}