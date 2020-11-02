package com.zerra.common.api.registry

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
data class RegistryEntry<T: Any>(val registry: Registry<T>, val value: KClass<out T>, val id: Long, val localizedName: String) {
    fun createInstance(): T? = registry.factory(this)
}