package com.zerra.common.api.registry

import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
data class RegistryEntry<T: Any>(val value: KClass<T>, val localizedName: String)