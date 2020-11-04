package com.zerra.client.state

import com.zerra.common.api.state.State
import com.zerra.common.api.state.StateManager

interface ClientState: State {
    fun render(partialTicks: Float)
}

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ClientStateManager: StateManager()