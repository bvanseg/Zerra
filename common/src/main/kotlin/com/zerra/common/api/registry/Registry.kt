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
class Registry<T : Any>(val factory: (RegistryEntry<T>) -> T?) {

    private val classEntries = ConcurrentHashMap<KClass<out T>, RegistryEntry<out T>>()

    private val logger = getLogger()

    fun register(value: KClass<out T>, unlocalizedName: String) {
        val entry = RegistryEntry(this, value, unlocalizedName)

        if(classEntries.contains(entry)) {
            throw IllegalStateException("Attempted to register an entry that already exists: $value")
        }


        classEntries[value] = entry

        logger.info("Successfully registered entry $value")
    }

    fun unregister(value: KClass<T>) {
        val entry = classEntries.remove(value)
        logger.info("Successfully unregistered entry $value")
    }

    fun getEntry(klass: KClass<*>) = classEntries[klass]
}