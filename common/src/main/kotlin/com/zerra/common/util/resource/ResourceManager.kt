package com.zerra.common.util.resource

import bvanseg.kotlincommons.any.getLogger
import java.util.function.Predicate

/**
 * Used to manage files within a specified domain-space.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class ResourceManager(val root: String, val domain: String) {

    val logger = getLogger()

    fun createResourceLocation(location: String): ResourceLocation = ResourceLocation(this, domain, location)

    fun resourceExists(location: String): Boolean = !MasterResourceManager.resources.getResourcesWithPath("$root$domain/$location").isNullOrEmpty()

    fun getResourceLocations(): Collection<ResourceLocation> {
        return MasterResourceManager.resources.allResources.filter { it.path.startsWith("$root$domain/") }.map { ResourceLocation(this, domain, it.path.substring(root.length + domain.length + 1)) }
    }

    fun getResourceLocations(predicate: Predicate<String>): Collection<ResourceLocation> {
        return MasterResourceManager.resources.allResources.filter { it.path.startsWith("$root$domain/") && predicate.test(it.path.substring(root.length + domain.length + 1)) }.map { ResourceLocation(this, domain, it.path.substring(root.length + domain.length + 1)) }
    }
}