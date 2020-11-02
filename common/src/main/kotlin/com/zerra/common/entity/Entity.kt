package com.zerra.common.entity

import java.util.*

/**
 * Represents the core of an entity within the world.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
abstract class Entity {

    var uuid: UUID = UUID.randomUUID()
    var ticksExisted: Long = 0

    fun update() {
        ticksExisted++
    }
}