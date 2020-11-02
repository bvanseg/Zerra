package com.zerra.client.state

import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.GameRenderManager
import com.zerra.common.realm.Universe
import com.zerra.common.realm.Realm

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class ClientGameState: ClientState {

    private val activeRealm: Realm? = null

    private val clientPlayer = ClientEntityPlayer()

    private val universe = Universe()
    private val renderManager = GameRenderManager()

    override fun init() {

    }

    override fun render() {

        // TODO: Render active realm here.

        val playerRenderer = renderManager.getEntityRendererFor(clientPlayer)
        playerRenderer?.render(clientPlayer)

        // Render the HUD last
        renderManager.renderHUD()
    }

    override fun update() {
        clientPlayer.update()
        universe.update()
    }

    override fun dispose() {

    }
}