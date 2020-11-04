package com.zerra.common.realm.chunk

import com.zerra.common.Zerra
import com.zerra.common.realm.Realm
import com.zerra.common.state.GameState
import com.zerra.common.util.storage.FileManager
import org.joml.Vector3i
import org.joml.Vector3ic
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

internal class ChunkManager(val realm: Realm) {
    private val chunks = ConcurrentHashMap<Vector3ic, Chunk>()

    fun loadChunk(posX: Int, posY: Int, posZ: Int) {

    }

    fun unloadChunk(posX: Int, posY: Int, posZ: Int) = unloadChunk(Vector3i(posX, posY, posZ))

    fun unloadChunk(pos: Vector3i) {
        val chunk = chunks[pos] ?: return

        if(chunk.isDirty) {
            val chunkData = chunk.write()

            val savePath = FileManager.getCurrentSaveFolder()
            val dataPath = savePath.resolve("data")
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

        chunks.remove(pos)
    }
}