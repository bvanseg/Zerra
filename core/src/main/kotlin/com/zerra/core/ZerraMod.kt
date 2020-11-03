package com.zerra.core

import com.zerra.client.ClientRegistryManager
import com.zerra.common.Zerra
import com.zerra.common.api.event.ModInitializationEvent
import com.zerra.common.api.mod.Mod
import com.zerra.common.api.mod.ModLoader
import com.zerra.common.network.Side
import com.zerra.server.ServerRegistryManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
@Mod("zerra")
internal object ZerraMod {

    init {
        ModLoader.EVENT_BUS.addListener(ZerraMod::init)
    }

    private fun init(event: ModInitializationEvent) {
        when(event.side) {
            Side.CLIENT -> ClientRegistryManager.init()
            Side.SERVER -> ServerRegistryManager.init()
        }
    }
}