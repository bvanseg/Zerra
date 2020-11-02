package com.zerra.client

import com.zerra.client.entity.ClientEntityPlayer
import com.zerra.client.render.entity.RenderClientPlayer
import com.zerra.client.render.entity.RenderEntity
import com.zerra.common.api.registry.RegistryManager
import com.zerra.common.entity.Entity
import kotlin.reflect.KClass

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ClientRegistryManager: RegistryManager() {

    val ENTITY_RENDER_REGISTRY = addInstanceRegistry<KClass<out Entity>, RenderEntity<*>>()

    override fun init() {
        super.init()
        // Entities
        ENTITY_REGISTRY.register(ClientEntityPlayer::class)

        // Renders
        ENTITY_RENDER_REGISTRY.register(ClientEntityPlayer::class, RenderClientPlayer())
    }
}