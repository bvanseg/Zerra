package com.zerra.client.state

import com.zerra.client.entity.EntityClientPlayer
import com.zerra.client.render.GameRenderManager
import com.zerra.common.Universe
import com.zerra.common.realm.Realm

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class GameState: State {

    private val activeRealm: Realm? = null

    private val clientPlayer = EntityClientPlayer()

    private val universe = Universe()
    private val renderManager = GameRenderManager()

    override fun init() {

    }

    override fun render() {

        activeRealm?.let { realm ->
            val realmRenderer = renderManager.getRendererFor(realm)
            realmRenderer.render(realm)
        }

        val playerRenderer = renderManager.getRendererFor(clientPlayer)
        playerRenderer.render(clientPlayer)

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