package com.zerra.common.api.registry

import bvanseg.kotlincommons.any.getLogger
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

/**
 * Represents the base of a registry.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class InstanceRegistry<T : Any>(): Registry<T> {

    private val entries = ConcurrentHashMap<KClass<out T>, InstanceRegistryEntry<out T>>()

    private val logger = getLogger()

    fun register(value: T) {
        val entry = InstanceRegistryEntry(this, value)

        if(entries.contains(entry)) {
            throw IllegalStateException("Attempted to register an entry that already exists: $value")
        }


        entries[value::class] = entry

        logger.info("Successfully registered entry $value")
    }

    fun unregister(value: KClass<T>) {
        val entry = entries.remove(value)
        logger.info("Successfully unregistered entry $value")
    }

    fun getEntry(klass: KClass<*>) = entries[klass]
    fun getEntry(obj: T) = entries[obj::class]
}