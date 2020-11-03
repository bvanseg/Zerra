package com.zerra.core.server

import com.zerra.core.common.ZerraCommonRegistry

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ZerraServerRegistry: ZerraCommonRegistry {
    override fun init() {
        println("Initializing server registry")
    }
}