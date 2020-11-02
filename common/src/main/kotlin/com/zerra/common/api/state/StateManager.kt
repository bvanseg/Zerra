package com.zerra.common.api.state

abstract class StateManager {

    var activeState: State = Stateless
        private set

    fun setState(state: State) {
        activeState.dispose()
        activeState = state
        activeState.init()
    }
}