package com.zerra.common.util.resource

/**
 * Represents a resource/asset in a classpath, disk, or from a URL.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
interface KResource {

    val domain: String
    val location: String
    val path: String

    fun toResourceMode(): KResourceLocation
    fun toActorMode(shouldClose: Boolean = false): KActor

    companion object {
        @JvmStatic
        var defaultRoot = "assets/"
        @JvmStatic
        var defaultDomain = "base"
    }
}