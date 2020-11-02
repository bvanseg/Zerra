package com.zerra.common

import com.zerra.common.network.Side

/**
 * Common game class for both client and server side instances.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Zerra {

    /**
     * Stores the side within the current thread. When this variable is set during the initialization of the
     * respective ZerraClient and ZerraServer classes, any threads spawned from those threads will also carry this value.
     * So if the main client thread spawns extra threads for chunk loading, the chunk loading threads will also have
     * this value set for them locally, and any child threads of theirs will be the same, and so on.
     */
    protected val localSide = InheritableThreadLocal<Side>()

    fun getSide(): Side = localSide.get()

    abstract fun init()

    abstract fun update()
    abstract fun getRegistryManager(): RegistryManager
}