package com.zerra.common.entity

import bvanseg.kotlincommons.javaclass.createNewInstance
import com.zerra.common.realm.Realm
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
class EntityManager(val realm: Realm) {

    val entities = ConcurrentHashMap<UUID, Entity>()

    inline fun <reified T: Entity> createEntity(): Entity {
        val entity = createNewInstance(T::class.java, arrayOf(Realm::class.java), realm)
        entities[entity.uuid] = entity
        return entity
    }
}