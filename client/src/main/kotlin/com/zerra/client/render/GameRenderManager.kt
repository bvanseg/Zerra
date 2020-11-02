package com.zerra.client.render

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class GameRenderManager {

    fun <T> getRendererFor(target: T): Renderable<T> = object: Renderable<T> {
        override fun render(renderable: T) {
            println("rendering...")
        }
    }

    fun renderHUD() {
        // TODO
    }
}