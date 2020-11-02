package com.zerra.client

import com.zerra.client.render.entity.RenderClientPlayer
import com.zerra.client.render.entity.RenderEntity
import com.zerra.common.api.registry.RegistryManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ClientRegistryManager: RegistryManager() {

    val ENTITY_RENDER_REGISTRY = addRegistry<RenderEntity<*>>()

    override fun init() {
        super.init()
        ENTITY_RENDER_REGISTRY.register(RenderClientPlayer::class)
    }
}