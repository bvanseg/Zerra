package com.zerra.common.api.registry

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.javaclass.createNewInstance
import com.zerra.common.entity.Entity
import com.zerra.common.entity.EntityPlayer
import com.zerra.common.realm.Realm
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class RegistryManager {

    companion object {
        val logger = getLogger()
    }

    val registries = hashMapOf<KClass<*>, Registry<*>>()

    val REALM_REGISTRY = addRegistry<Realm>()
    val ENTITY_REGISTRY = addRegistry<Entity>()

    inline fun <reified T : Any> addRegistry(noinline factory: (RegistryEntry<T>) -> T? = { entry ->
        createNewInstance(entry.value.java)
    }): Registry<T> {
        logger.info("Creating new registry for type: ${T::class}")
        val newRegistry = Registry(factory = factory)
        registries[T::class] = newRegistry
        return newRegistry
    }

    open fun init() {
        ENTITY_REGISTRY.register(EntityPlayer::class)
    }
}