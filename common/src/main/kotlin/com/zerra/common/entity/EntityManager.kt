package com.zerra.common.entity

import bvanseg.kotlincommons.javaclass.createNewInstance
import com.zerra.common.realm.Realm
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class EntityManager(val realm: Realm) {

    val entities = ConcurrentHashMap<UUID, Entity>()

    inline fun <reified T: Entity> createEntity(): Entity {
        val entity = createNewInstance(T::class.java, arrayOf(Realm::class.java), realm)
        entities[entity.uuid] = entity
        return entity
    }
}