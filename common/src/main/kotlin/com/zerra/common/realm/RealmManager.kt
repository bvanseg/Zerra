package com.zerra.common.realm

import com.zerra.common.Zerra
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class RealmManager {

    val loadedRealms = ConcurrentHashMap<Long, Realm>()

    fun loadRealm(realm: Realm) = loadRealm(realm.id)
    fun unloadRealm(realm: Realm) = unloadRealm(realm.id)

    fun loadRealm(id: Long) {
        if (loadedRealms[id] != null) {
            throw IllegalStateException("Attempted to load a Realm that is already loaded: id $id")
        }

        val realmEntry = Zerra.getInstance().getRegistryManager().REALM_REGISTRY.getEntryById(id)

        realmEntry?.let {
            val realm = it.createInstance() ?: throw IllegalStateException("Failed to create Realm with id $id")
            realm.load()
            loadedRealms[it.id] = realm
        } ?: throw IllegalStateException("Realm does not exist: id $id")
    }

    fun unloadRealm(id: Long) {
        val realm = loadedRealms[id] ?: return
        realm.unload()
        loadedRealms.remove(id)
    }
}