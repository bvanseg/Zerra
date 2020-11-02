package com.zerra.common.api.registry

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class InstanceRegistryEntry<K, V: Any>(instanceRegistry: InstanceRegistry<K, V>, val value: V): RegistryEntry<K, V>(instanceRegistry)