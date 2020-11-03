package com.zerra.common.api.registry

import bvanseg.kotlincommons.any.getLogger
import kotlin.reflect.KClass

/**
 * Represents the base of a registry.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class InstanceRegistry<K, V : Any>: Registry<K, V>() {

    private val logger = getLogger()

    fun register(key: K, value: V) {
        val entry = InstanceRegistryEntry(this, value)

        if(entries.contains(entry)) {
            throw IllegalStateException("Attempted to register an entry that already exists: $value")
        }


        entries[key] = entry

        logger.info("Successfully registered entry $value")
    }

    fun unregister(value: KClass<V>) {
        val entry = entries.remove(value)
        logger.info("Successfully unregistered entry $value")
    }

    fun getEntry(key: K) = entries[key] as InstanceRegistryEntry?
}