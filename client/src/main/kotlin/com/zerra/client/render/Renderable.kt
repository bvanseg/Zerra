package com.zerra.client.render

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
interface Renderable<T> {
    fun render(renderable: T)
}