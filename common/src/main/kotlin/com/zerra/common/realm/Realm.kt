package com.zerra.common.realm

import com.zerra.common.entity.EntityManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Realm {

    var ticksExisted: Long = 0
    private val entityManager = EntityManager(this)

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