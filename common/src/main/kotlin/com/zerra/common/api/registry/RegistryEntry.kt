package com.zerra.common.api.registry

import bvanseg.kotlincommons.javaclass.createNewInstance
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
data class RegistryEntry<T: Any>(val value: KClass<T>, val id: Long, val localizedName: String) {

    fun createInstance(): T? = createNewInstance(value.java)
}