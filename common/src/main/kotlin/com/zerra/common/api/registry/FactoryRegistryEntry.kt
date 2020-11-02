package com.zerra.common.api.registry

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class FactoryRegistryEntry<T: Any>(private val factoryRegistry: FactoryRegistry<T>, val value: KClass<out T>): RegistryEntry<KClass<out T>, T>(factoryRegistry) {
    fun createInstance(): T? = factoryRegistry.factory(this)
}