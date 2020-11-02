package com.zerra.client

import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.entity.RenderClientPlayer
import com.zerra.client.render.entity.RenderEntity
import com.zerra.common.api.registry.RegistryManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ClientRegistryManager: RegistryManager() {

    val ENTITY_RENDER_REGISTRY = addInstanceRegistry<RenderEntity<*>>()

    override fun init() {
        super.init()
        // Entities
        ENTITY_REGISTRY.register(ClientEntityPlayer::class)

        // Renders
        ENTITY_RENDER_REGISTRY.register(RenderClientPlayer())
    }
}