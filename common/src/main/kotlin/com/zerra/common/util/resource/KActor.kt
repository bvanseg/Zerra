package com.zerra.common.util.resource

import bvanseg.kotlincommons.number.HashCodeBuilder
import bvanseg.kotlincommons.string.ToStringBuilder
import com.zerra.common.util.resource.KResource.Companion.defaultDomain
import com.zerra.common.util.resource.KResource.Companion.defaultRoot

/**
 * Represents some actor/entity that belongs to a [domain] and has a specific [location].
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class KActor(override var domain: String, override var location: String): KResource {

    constructor(name: String): this(defaultDomain, name)
    constructor(parent: KActor) : this(parent.domain, parent.location)

    override val path: String
        get() = "$defaultRoot$domain/$location"

    override fun toResourceMode(): KResourceLocation = KResourceLocation(domain, location)
    override fun toActorMode(shouldClose: Boolean): KActor = this

    override fun toString(): String = ToStringBuilder.builder(this)
        .append("domain", domain)
        .append("location", location)
        .append("path", path)
        .toString()

    override fun hashCode(): Int = HashCodeBuilder(this).append(path).hashCode()
    override fun equals(other: Any?): Boolean = other is KActor && this.domain == other.domain && this.location == other.location
}