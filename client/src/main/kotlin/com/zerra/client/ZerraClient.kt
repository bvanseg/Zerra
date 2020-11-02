package com.zerra.client

import com.zerra.client.render.GameWindow
import com.zerra.client.render.RenderManager
import com.zerra.common.Zerra
import com.zerra.common.network.Side

class ZerraClient private constructor(): Zerra() {

    companion object {
        private var instance: ZerraClient? = null

        /**
         * Gets a client-sided instance of Zerra.
         */
        fun getInstance(): ZerraClient {
            if(instance == null) {
                instance = ZerraClient()
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.CLIENT)
    }

    val renderManager: RenderManager = RenderManager()

    fun startup() {
        // Create window
        val window = GameWindow(1920, 1080)

        // create context
        // TODO
    }

    fun render() {
        renderManager.render()
    }

    override fun update() = TODO("Not yet implemented")
}