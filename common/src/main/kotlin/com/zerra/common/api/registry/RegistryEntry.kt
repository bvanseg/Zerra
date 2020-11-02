package com.zerra.common.api.registry

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class RegistryEntry<T: Any>(private val registry: Registry<T>, val value: KClass<out T>) {
    fun createInstance(): T? = registry.factory(this)
}