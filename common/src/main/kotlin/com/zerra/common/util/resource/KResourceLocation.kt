package com.zerra.common.util.resource

import bvanseg.kotlincommons.number.HashCodeBuilder
import bvanseg.kotlincommons.string.ToStringBuilder
import com.zerra.common.util.resource.KResource.Companion.defaultDomain
import com.zerra.common.util.resource.KResource.Companion.defaultRoot
import java.io.File
import java.io.InputStream

/**
 * Represents a resource either in the classpath or on disk.
 *
 * @author Boston Vanseghi
 * @author Ocelot5836
 * @since 0.0.1
 */
class KResourceLocation: KResource, AutoCloseable {

    override var domain: String = defaultDomain
        private set
    override var location: String = ""
        private set

    override val path: String
        get() = "$defaultRoot$domain/$location"

    constructor(domain: String, location: String) {
        this.domain = domain
        this.location = location
    }

    constructor(parent: KResourceLocation) {
        this.domain = parent.domain
        this.location = parent.location
    }

    constructor(location: String) {
        val resourceLocationRaw = location.split(":".toRegex(), 2).toTypedArray()
        if (resourceLocationRaw.size > 1) {
            this.domain = resourceLocationRaw[0]
            this.location = resourceLocationRaw[1]
        } else {
            this.domain = defaultDomain
            this.location = location
        }
    }

    private val resource by lazy { Resources.scan.getResourcesWithPath(path)?.firstOrNull() }

    val inputStream: InputStream by lazy { resource!!.open() }
    val file: File by lazy { resource!!.classpathElementFile }

    fun getInputStreamSafe(): InputStream? = if(resource != null) inputStream else null
    fun getFileSafe(): File? = if(resource != null) file else null

    override fun toResourceMode(): KResourceLocation = this

    override fun toActorMode(shouldClose: Boolean): KActor {
        if(shouldClose) close()
        return KActor(domain, location)
    }

    override fun close() = inputStream.close()

    override fun toString(): String = ToStringBuilder.builder(this)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()

    override fun hashCode(): Int = HashCodeBuilder(this).append(path).hashCode()
    override fun equals(other: Any?): Boolean = other is KResourceLocation && this.domain == other.domain && this.location == other.location
}