package com.zerra.client.render

import com.zerra.client.ZerraClient
import com.zerra.client.render.entity.RenderEntity
import com.zerra.common.entity.Entity

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class GameRenderManager {

    inline fun <reified T: Entity> getEntityRendererFor(entity: T): RenderEntity<T>? = ZerraClient.getInstance().getRegistryManager().ENTITY_RENDER_REGISTRY.getEntry(T::class)?.value as RenderEntity<T>?

    fun renderHUD() {
        // TODO
    }
}