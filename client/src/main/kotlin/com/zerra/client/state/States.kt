package com.zerra.client.state

import com.zerra.common.api.state.State
import com.zerra.common.api.state.StateManager
import org.lwjgl.system.NativeResource

interface ClientState: State {
    fun render()
}

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ClientStateManager: StateManager()