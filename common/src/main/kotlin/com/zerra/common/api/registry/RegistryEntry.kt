package com.zerra.common.api.registry

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class RegistryEntry<K, V : Any>(private val registry: Registry<K, V>)