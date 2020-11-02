package com.zerra.common.realm

import com.zerra.common.entity.EntityManager

class Realm(val id: Long) {

    var ticksExisted: Long = 0
    val entityManager = EntityManager(this)

    fun update() {
        ticksExisted++
        entityManager.entities.forEach {
            it.value.update()
        }
    }

    fun load() {
        // TODO
    }

    fun unload() {
        // TODO
    }
}