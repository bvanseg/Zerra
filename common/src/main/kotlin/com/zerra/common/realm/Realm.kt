package com.zerra.common.realm

import com.zerra.common.Zerra
import com.zerra.common.entity.Entity
import com.zerra.common.entity.EntityManager
import com.zerra.common.network.Side
import com.zerra.common.realm.chunk.ChunkManager
import org.joml.Vector3ic

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Realm internal constructor(val name: String) {

    var ticksExisted: Long = 0
    internal val entityManager = EntityManager(this)
    private val chunkManager = ChunkManager(this)

    fun update() {
        ticksExisted++
        entityManager.loadedEntities.forEach {
            it.value.update()
        }
    }

    fun load() {
        // TODO load chunks around all players
    }

    fun unload() {
        when(Zerra.getInstance().getSide()) {
            Side.CLIENT -> {
                entityManager.loadedEntities.clear()
                chunkManager.loadedChunks.clear()
            }
            Side.SERVER -> {
                val entitiesPerChunkPos = hashMapOf<Vector3ic, MutableList<Entity>>()

                entityManager.loadedEntities.forEach { (uuid, entity) ->
                    val chunkPos = entity.getChunkPosition()

                    if(entitiesPerChunkPos[chunkPos] == null) {
                        entitiesPerChunkPos[chunkPos] = mutableListOf()
                    }

                    entitiesPerChunkPos[chunkPos]!!.add(entity)
                }

                chunkManager.loadedChunks.forEach { (chunkPos, chunk) ->
                    val entities = entitiesPerChunkPos[chunkPos]

                    chunkManager.unloadChunk(chunkPos, entities)
                }
            }
        }
    }
}