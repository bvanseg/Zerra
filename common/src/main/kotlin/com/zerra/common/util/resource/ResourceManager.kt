package com.zerra.common.util.resource

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.bool.ifTrue
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

/**
 * Used to manage files within a specified domain-space.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class ResourceManager(val root: String, private val domain: String) {

    val logger = getLogger()

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

    fun getResourceAtLocation(location: String): ResourceLocation = ResourceLocation(this, domain, location)

    fun resourceExists(location: String): Boolean = !resources.getResourcesWithPath("$root$domain/$location").isEmpty()

    private fun close() = scanInitialized.ifTrue { resources.close() }
}