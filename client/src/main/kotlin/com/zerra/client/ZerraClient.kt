package com.zerra.client

import com.zerra.common.Zerra
import com.zerra.common.network.Side

class ZerraClient private constructor(): Zerra() {

    companion object {
        private var instance: ZerraClient? = null

        /**
         * Gets a client-sided instance of Zerra.
         */
        fun getInstance(): ZerraClient {
            if(instance == null) {
                instance = ZerraClient()
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.CLIENT)
    }

    override fun update() = TODO("Not yet implemented")

    override fun preInit() = TODO("Not yet implemented")
    override fun init() = TODO("Not yet implemented")
    override fun postInit() = TODO("Not yet implemented")
}