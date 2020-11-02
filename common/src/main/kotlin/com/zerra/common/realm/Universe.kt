package com.zerra.common.realm

/**
 * Represents the entirety of all that exists within the game.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Universe {

    var ticksExisted: Long = 0
    val realmManager = RealmManager()

    fun update() {
        realmManager.loadedRealms.forEach {
            it.update()
        }
        ticksExisted++
    }
}