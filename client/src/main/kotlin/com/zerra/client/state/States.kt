package com.zerra.client.state

interface State {
    fun init()
    fun render()
    fun update()
    fun dispose()
}

/**
 * A default state that functionally does nothing.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object Stateless: State {
    override fun init() = Unit
    override fun render() = Unit
    override fun update() = Unit
    override fun dispose() = Unit
}

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class StateManager {

    var activeState: State = Stateless
        private set

    fun setState(state: State) {
        activeState.dispose()
        activeState = state
        activeState.init()
    }
}