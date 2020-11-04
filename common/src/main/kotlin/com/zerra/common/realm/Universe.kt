package com.zerra.common.realm

import com.zerra.common.Zerra

/**
 * Represents the entirety of all that exists within the game.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Universe internal constructor(val name: String) {

    var ticksExisted: Long = 0
    private val realmManager = RealmManager()

    fun update() {
        realmManager.loadedRealms.forEach {
            it.update()
        }
        ticksExisted++
    }

    fun load() {

    }

    fun unload() {
        val logger = Zerra.getInstance().logger
        logger.info("Saving universe...")
        val start = System.currentTimeMillis()
        realmManager.loadedRealms.forEach {
            it.unload()
        }
        logger.info("Finished saving universe in ${System.currentTimeMillis() - start}ms.")
    }
}