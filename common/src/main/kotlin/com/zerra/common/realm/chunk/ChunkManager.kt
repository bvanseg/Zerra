package com.zerra.common.realm.chunk

import com.zerra.common.Zerra
import com.zerra.common.entity.Entity
import com.zerra.common.network.Side
import com.zerra.common.realm.Realm
import com.zerra.common.util.storage.FileManager
import com.zerra.common.util.storage.UBJ
import org.joml.Vector3i
import org.joml.Vector3ic
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap

internal class ChunkManager(val realm: Realm) {
    internal val loadedChunks = ConcurrentHashMap<Vector3ic, Chunk>()

    fun loadChunk(posX: Int, posY: Int, posZ: Int) {

    }

    fun unloadChunk(posX: Int, posY: Int, posZ: Int) = unloadChunk(Vector3i(posX, posY, posZ))

    fun unloadChunk(pos: Vector3ic, entities: List<Entity>? = emptyList()) {
        if(Zerra.getInstance().getSide() == Side.CLIENT) throw IllegalAccessException("Illegal access: Can not unload chunks client-side!")
        val chunk = loadedChunks[pos] ?: return

        if(chunk.isDirty) {
            val chunkData = chunk.write()

            // Write entities, if present.
            if (entities != null && entities.isNotEmpty()) {
                val entityData = UBJ()

                entities.forEach { entity ->
                    entityData.put(entity.uuid.toString(), entity.write())

                    // Ensure the entity is unloaded as we write it to the chunk.
                    realm.entityManager.unload(entity)
                }
            }

            val dataPath = FileManager.getSaveDataFolder()
            val chunkPath = dataPath.resolve("chunks")

            val chunkFile = chunkPath.resolve(chunk.toSimpleString()).toFile()

            if (chunkFile.exists()) {
                chunkFile.createNewFile()
            }

            val outStream = FileOutputStream(chunkFile)

            outStream.use { fios ->
                fios.write(chunkData.toByteArray())
            }
        }

        loadedChunks.remove(pos)
    }
}