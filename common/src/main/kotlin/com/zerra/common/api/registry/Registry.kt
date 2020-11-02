package com.zerra.common.api.registry

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.javaclass.createNewInstance
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import kotlin.reflect.KClass

/**
 * Represents the base of a registry.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Registry<T : Any>(val factory: (RegistryEntry<T>) -> T?) {

    private val currentID = AtomicLong()

    private val classEntries = ConcurrentHashMap<KClass<out T>, RegistryEntry<out T>>()
    private val idEntries = ConcurrentHashMap<Long, RegistryEntry<out T>>()

    private val logger = getLogger()

    fun register(value: KClass<out T>, localizedName: String) {
        val entry = RegistryEntry(this, value, currentID.getAndIncrement(), localizedName)

        if(classEntries.contains(entry)) {
            throw IllegalStateException("Attempted to register an entry that already exists: $value")
        }


        idEntries[entry.id] = entry
        classEntries[value] = entry

        logger.info("Successfully registered entry $value")
    }

    fun unregister(value: KClass<T>) {
        val entry = classEntries.remove(value)
        entry?.let {
            idEntries.remove(it.id)
        }
        logger.info("Successfully unregistered entry $value")
    }

    fun getEntryById(id: Long): RegistryEntry<out T>? = idEntries[id]
}