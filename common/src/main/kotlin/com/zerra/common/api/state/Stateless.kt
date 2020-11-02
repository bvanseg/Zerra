package com.zerra.common.api.state

/**
 * A default state that functionally does nothing.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object Stateless: State {
    override fun init() = Unit
    override fun update() = Unit
    override fun dispose() = Unit
}