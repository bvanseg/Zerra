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

    val logger = getLogger()

    val registries = hashMapOf<KClass<*>, Registry<*>>()

    val REALM_REGISTRY = addInstanceRegistry<Realm>()
    val ENTITY_REGISTRY = addFactoryRegistry<Entity>()

    inline fun <reified T : Any> addInstanceRegistry(): InstanceRegistry<T> {
        logger.info("Creating new registry for object of type: ${T::class}")
        val newRegistry = InstanceRegistry<T>()
        registries[T::class] = newRegistry
        return newRegistry
    }

    inline fun <reified T : Any> addFactoryRegistry(noinline factory: (FactoryRegistryEntry<T>) -> T? = { entry ->
        createNewInstance(entry.value.java)
    }): FactoryRegistry<T> {
        logger.info("Creating new registry for type: ${T::class}")
        val newRegistry = FactoryRegistry(factory = factory)
        registries[T::class] = newRegistry
        return newRegistry
    }

    open fun init() {
        logger.info("Initializing registry manager")
        ENTITY_REGISTRY.register(EntityPlayer::class)
    }
}