package com.zerra.common.util

interface Phased {
    fun preInit()
    fun init()
    fun postInit()
}