package com.zerra.common.api.registry

import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Registry<K, V: Any> {
    protected open val entries = ConcurrentHashMap<K, RegistryEntry<K, out V>>()
}