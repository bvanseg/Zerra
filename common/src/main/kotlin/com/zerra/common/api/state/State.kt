package com.zerra.common.api.state

interface State {
    fun init()
    fun update()
    fun dispose()
}