package com.zerra.common

import com.zerra.common.api.event.ModInitializationEvent
import com.zerra.common.api.mod.ModLoader
import com.zerra.common.api.registry.RegistryManager
import com.zerra.common.api.state.StateManager
import com.zerra.common.network.Side
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceManager
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.Executor

/**
 * Common game class for both client and server side instances.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Zerra : Executor {

    companion object {
        const val TICKS_PER_SECOND = 60

        var zerraInstance: Zerra? = null

        fun getInstance(): Zerra {
            if (zerraInstance == null) {
                throw IllegalStateException("Attempted to fetch Zerra instance when it was null!")
            }

            return zerraInstance!!
        }
    }

    /**
     * Stores the side within the current thread. When this variable is set during the initialization of the
     * respective ZerraClient and ZerraServer classes, any threads spawned from those threads will also carry this value.
     * So if the main client thread spawns extra threads for chunk loading, the chunk loading threads will also have
     * this value set for them locally, and any child threads of theirs will be the same, and so on.
     */
    protected val localSide = InheritableThreadLocal<Side>()
    private val executionQueue = ConcurrentLinkedDeque<Runnable>()

    fun getSide(): Side = localSide.get()

    override fun execute(command: Runnable) {
        executionQueue.push(command)
    }

    fun flushTasks() {
        while (executionQueue.isNotEmpty())
            executionQueue.pop().run()
    }

    open fun init() {
        ModLoader.loadAllMods()
        this.getRegistryManager().init()
        ModLoader.EVENT_BUS.fire(ModInitializationEvent(this.getRegistryManager(), this.getSide()))
    }

    abstract fun createGame()

    abstract fun update()

    private val primaryResourceManager = MasterResourceManager.createResourceManager("assets/", "zerra")

    fun getResourceManager(): ResourceManager = primaryResourceManager

    abstract fun getStateManager(): StateManager
    abstract fun getRegistryManager(): RegistryManager

    abstract fun cleanup()
}