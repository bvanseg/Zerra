package com.zerra.common.util.resource

import bvanseg.kotlincommons.number.HashCodeBuilder
import java.io.File
import java.io.InputStream

/**
 * Represents a resource either in the classpath or on disk.
 *
 * @author Boston Vanseghi
 * @author Ocelot5836
 * @since 0.0.1
 */
data class ResourceLocation(val resourceManager: ResourceManager, val domain: String, val location: String) : AutoCloseable {

    val path: String
        get() = "${resourceManager.root}$domain/$location"
    val resourceExists: Boolean
        get() = resource != null

    constructor(parent: ResourceLocation) : this(parent.resourceManager, parent.domain, parent.location)

    private val resource by lazy { MasterResourceManager.resources.getResourcesWithPath(path)?.firstOrNull() }

    val inputStream: InputStream? by lazy { if (resource != null) resource!!.open() else null }
    val file: File? by lazy { if (resource != null) resource!!.classpathElementFile else null }

    override fun close() {
        resource?.close()
    }

    override fun toString(): String = path

    override fun hashCode(): Int = HashCodeBuilder(this).append(path).hashCode()
    override fun equals(other: Any?): Boolean = other is ResourceLocation && this.domain == other.domain && this.location == other.location
}