package com.zerra.common.api.registry

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
open class InstanceRegistryEntry<T: Any>(instanceRegistry: InstanceRegistry<T>, val value: T): RegistryEntry<T>(instanceRegistry)