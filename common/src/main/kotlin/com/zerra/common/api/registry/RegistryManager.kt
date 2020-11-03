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

    @PublishedApi
    internal val registries = hashMapOf<KClass<*>, Registry<*, *>>()

    val REALM_REGISTRY = addClassInstanceRegistry<Realm>()
    val ENTITY_REGISTRY = addFactoryRegistry<Entity>()

    inline fun <reified V : Any> addClassInstanceRegistry(): InstanceRegistry<KClass<out V>, V> = addInstanceRegistry()

    inline fun <reified K, reified V : Any> addInstanceRegistry(): InstanceRegistry<K, V> {
        logger.info("Creating new registry for object of type: ${V::class}")
        val newRegistry = InstanceRegistry<K, V>()
        registries[V::class] = newRegistry
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

    inline fun <reified T: Any> getFactoryRegistry() = registries[T::class] as FactoryRegistry<T>? ?: throw IllegalStateException("Attempted to get a registry that does not exist: ${T::class}")
    inline fun <reified T: Any> getInstanceRegistry() = registries[T::class] as InstanceRegistry<*, T>? ?: throw IllegalStateException("Attempted to get a registry that does not exist: ${T::class}")
    inline fun <reified T> getRegistry() = registries[T::class] ?: throw IllegalStateException("Attempted to get a registry that does not exist: ${T::class}")
    fun getRegistries(): Map<KClass<*>, Registry<*, *>> = registries
}