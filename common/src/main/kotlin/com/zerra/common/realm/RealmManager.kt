package com.zerra.common.realm

import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class RealmManager internal constructor() {

    val loadedRealms = ConcurrentHashMap.newKeySet<Realm>()

    fun load(realm: Realm) {
        if (loadedRealms.contains(realm)) {
            throw IllegalStateException("Attempted to load a Realm that is already loaded: id $realm")
        }

        realm.load()
        loadedRealms.add(realm)
    }

    fun unload(realm: Realm) {
        if (!loadedRealms.contains(realm)) {
            throw IllegalStateException("Attempted to unload a Realm that is already unloaded: id $realm")
        }

        realm.unload()
        loadedRealms.remove(realm)
    }
}