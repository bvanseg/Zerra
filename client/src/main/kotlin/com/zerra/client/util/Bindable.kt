package com.zerra.client.util

interface Bindable {

    fun bind()

    fun unbind()

    fun use(callback: () -> Unit) {
        bind()
        callback()
        unbind()
    }
}