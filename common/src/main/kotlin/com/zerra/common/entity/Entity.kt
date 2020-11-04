package com.zerra.common.entity

import com.zerra.common.realm.chunk.Chunk
import com.zerra.common.util.storage.Storable
import com.zerra.common.util.storage.UBJ
import org.joml.Vector3f
import org.joml.Vector3i
import org.joml.Vector3ic
import java.util.*

/**
 * Represents the core of an entity within the world.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Entity: Storable {

    var uuid: UUID = UUID.randomUUID()
    var ticksExisted: Long = 0
    var position: Vector3f = Vector3f(0f, 0f, 0f)
    var velocity: Vector3f = Vector3f(0f, 0f, 0f)

    fun update() {
        ticksExisted++
    }

    fun getChunkPosition(): Vector3ic = position.let {
        Vector3i(it.x.toInt() % Chunk.CHUNK_SIZE, it.y.toInt() % Chunk.CHUNK_SIZE, it.z.toInt() % Chunk.CHUNK_SIZE)
    }

    override fun read(ubj: UBJ) {
        this.uuid = ubj.readUUIDOrDefault("uuid", UUID.randomUUID())
        this.ticksExisted = ubj.readLongOrDefault("ticksExisted", 0)
        this.position = ubj.readVector3fOrDefault("pos", Vector3f(0f, 0f, 0f))
        this.velocity = ubj.readVector3fOrDefault("vel", Vector3f(0f, 0f, 0f))
    }

    override fun write(): UBJ {
        val ubj = UBJ()
        ubj.put("uuid", uuid)
        ubj.put("ticksExisted", ticksExisted)
        ubj.put("pos", position)
        ubj.put("vel", velocity)
        return ubj
    }
}