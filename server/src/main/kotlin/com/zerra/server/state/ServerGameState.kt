package com.zerra.server.state

import com.zerra.common.api.state.State
import com.zerra.common.realm.Universe
import com.zerra.common.state.GameState

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ServerGameState: GameState() {

    override fun init() {
        // TODO
    }

    override fun update() {
        universe.update()
    }

    override fun dispose() {
        // TODO
    }
}