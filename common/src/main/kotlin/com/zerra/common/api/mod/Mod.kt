package com.zerra.common.api.mod

import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.resource.ResourceManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Mod {

    abstract val domain: String
    val resourceManager: ResourceManager

    init {
        resourceManager = MasterResourceManager.createResourceManager("assets/", domain)
    }
}