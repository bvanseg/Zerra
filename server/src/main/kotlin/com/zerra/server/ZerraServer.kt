package com.zerra.server

import com.zerra.common.Zerra
import com.zerra.common.network.Side

class ZerraServer: Zerra() {

    companion object {
        private var instance: ZerraServer? = null

        /**
         * Gets a server-sided instance of Zerra.
         */
        fun getInstance(): ZerraServer {
            if(instance == null) {
                instance = ZerraServer()
            }

            return instance!!
        }
    }

    init {
        localSide.set(Side.SERVER)
    }

    override fun update() = TODO("Not yet implemented")

    override fun preInit() = TODO("Not yet implemented")
    override fun init() = TODO("Not yet implemented")
    override fun postInit() = TODO("Not yet implemented")
}