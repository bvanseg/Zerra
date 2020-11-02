package com.zerra.common.realm

import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class RealmManager {

    val loadedRealms = ConcurrentHashMap.newKeySet<Realm>()

    fun loadRealm(id: Long) {

    }

    fun unloadRealm(id: Long) {

    }
}