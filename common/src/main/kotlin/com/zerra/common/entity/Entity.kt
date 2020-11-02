package com.zerra.common.entity

import com.zerra.common.util.storage.Storable
import com.zerra.common.util.storage.UBJ
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

    fun update() {
        ticksExisted++
    }

    override fun read(ubj: UBJ) {
        this.uuid = ubj.readUUIDOrDefault("uuid", UUID.randomUUID())
        this.ticksExisted = ubj.readLongOrDefault("ticksExisted", 0)
    }

    override fun write(): UBJ {
        val ubj = UBJ()
        ubj.put("uuid", uuid)
        ubj.put("ticksExisted", ticksExisted)

        return ubj
    }
}