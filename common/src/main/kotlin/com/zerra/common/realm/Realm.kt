package com.zerra.common.realm

import com.zerra.common.entity.EntityManager
import com.zerra.common.realm.chunk.ChunkManager

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Realm internal constructor(val name: String) {

    var ticksExisted: Long = 0
    private val entityManager = EntityManager(this)
    private val chunkManager = ChunkManager(this)

    fun update() {
        ticksExisted++
        entityManager.entities.forEach {
            it.value.update()
        }
    }

    fun load() {
        // TODO load chunks around all players
    }

    fun unload() {
        // TODO load chunks around all players
    }
}