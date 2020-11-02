package com.zerra.server.state

import com.zerra.common.api.state.State
import com.zerra.common.realm.Universe

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ServerGameState: State {

    private val universe = Universe()

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