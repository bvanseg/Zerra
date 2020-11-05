package com.zerra.client.state

import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.GameRenderManager
import com.zerra.common.Zerra
import com.zerra.common.realm.Realm
import com.zerra.common.state.GameState
import com.zerra.server.ZerraServer
import kotlin.concurrent.thread

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ClientGameState: GameState(), ClientState {

    private val internalServer: ZerraServer
        get() = ZerraServer.getInstance()

    private val activeRealm: Realm? = null

    private val clientPlayer = ClientEntityPlayer()

    private val renderManager = GameRenderManager()

    override fun init() {

    }

    override fun render(partialTicks: Float) {

        // TODO: Render active realm here.

        val playerRenderer = renderManager.getEntityRendererFor(clientPlayer)
        playerRenderer?.render(clientPlayer)

        // TODO: Render HUD here.
    }

    override fun update() {
        clientPlayer.update()
        universe.update()
    }

    override fun dispose() {
        internalServer.cleanup()
    }

    internal fun createInternalServer() = thread {
        internalServer.init()
        internalServer.createGame()
    }
}