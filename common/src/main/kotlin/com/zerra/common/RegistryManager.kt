package com.zerra.common

import bvanseg.kotlincommons.any.getLogger
import com.zerra.common.api.registry.Registry
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

    inline fun <reified T : Any> addRegistry(): Registry<T> {
        logger.info("Creating new registry for type: ${T::class}")
        val newRegistry = Registry<T>()
        registries[T::class] = Registry<T>()
        return newRegistry
    }

    fun init() {
        ENTITY_REGISTRY.register(EntityPlayer::class, "entity.player")
    }
}