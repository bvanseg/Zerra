package com.zerra.common.api.event

import com.zerra.common.api.registry.RegistryManager
import com.zerra.common.network.Side

interface GenericEvent

class ModInitializationEvent(val registryManager: RegistryManager, val side: Side): GenericEvent