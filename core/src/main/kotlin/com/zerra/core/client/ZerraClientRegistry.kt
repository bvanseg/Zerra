package com.zerra.core.client

import com.zerra.core.common.ZerraCommonRegistry

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ZerraClientRegistry: ZerraCommonRegistry {
    override fun init() {
        println("Initializing client registry")
    }
}