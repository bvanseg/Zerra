package com.zerra.common.realm.chunk

import org.joml.Vector3i
import org.joml.Vector3ic
import java.util.concurrent.ConcurrentHashMap

internal class ChunkManager {
    private val chunks = ConcurrentHashMap<Vector3ic, Chunk>()

    fun loadChunk(posX: Int, posY: Int, posZ: Int) {

    }

    fun unloadChunk(posX: Int, posY: Int, posZ: Int) = unloadChunk(Vector3i(posX, posY, posZ))

    fun unloadChunk(pos: Vector3i) {
        val chunk = chunks[pos] ?: return

        // TODO: write the chunk to file.
        
        chunks.remove(pos)
    }
}