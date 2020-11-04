package com.zerra.common.entity

import bvanseg.kotlincommons.javaclass.createNewInstance
import com.zerra.common.realm.Realm
import com.zerra.common.util.storage.FileManager
import com.zerra.common.util.storage.UBJ
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Class responsible for governing entities within a given [Realm].  Entities are created, stored, transferred, and
 * destroyed through the entity manager.
 *
 * @param realm The realm for which the [EntityManager] manages entities for.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class EntityManager internal constructor(val realm: Realm) {

    val loadedEntities = ConcurrentHashMap<UUID, Entity>()

    inline fun <reified T: Entity> createEntity(): Entity {
        val entity = createNewInstance(T::class.java, arrayOf(Realm::class.java), realm)
        loadedEntities[entity.uuid] = entity
        return entity
    }

    /**
     * Unloads an entity from the loaded entities collection.
     */
    fun unload(entity: Entity) = loadedEntities.remove(entity.uuid)
}