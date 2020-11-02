package com.zerra.common.util.resource

import bvanseg.kotlincommons.any.getLogger

/**
 * Used to manage files within a specified domain-space.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class ResourceManager(val root: String, private val domain: String) {

    val logger = getLogger()

    fun getResourceAtLocation(location: String): ResourceLocation = ResourceLocation(this, domain, location)

    fun resourceExists(location: String): Boolean = !MasterResourceManager.resources.getResourcesWithPath("$root$domain/$location").isEmpty()
}