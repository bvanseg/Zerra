package com.zerra.common.realm.chunk

import bvanseg.kotlincommons.number.HashCodeBuilder
import com.zerra.common.block.Block
import com.zerra.common.block.Blocks
import com.zerra.common.realm.Realm
import com.zerra.common.util.storage.Storable
import com.zerra.common.util.storage.UBJ
import org.joml.Vector3i
import org.joml.Vector3ic
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Chunk(val realm: Realm, val posX: Int, val posY: Int, val posZ: Int): Storable {

    companion object {
        const val CHUNK_SIZE = 16 * 16 * 16
    }

    @Volatile
    var isDirty = false
    
    private val changes by lazy { ConcurrentHashMap<Vector3ic, Block>() } // TODO: Store BlockState, not Block
    private val blockStates = Array(CHUNK_SIZE) { Blocks.AIR } // TODO: Store BlockState, not Block

    fun getBlockState(x: Int, y: Int, z: Int) = blockStates[x + y * 16 + z * 256]
    fun setBlockState(blockState: Block, x: Int, y: Int, z: Int) {
        val currentBlockState = getBlockState(x, y, z)

        if (blockState == currentBlockState) return

        changes[Vector3i(x, y, z)] = blockState
        blockStates[x + y * 16 + z * 256]

        if(!isDirty) {
            isDirty = true
        }
    }

    override fun hashCode(): Int = HashCodeBuilder(this::class).append(realm).append(posX).append(posY).append(posZ).hashCode()

    override fun read(ubj: UBJ) = synchronized(blockStates) {
        val posData = ubj.readByteArray("posData")
        val blockData = ubj.readShortArray("blockData")
        val blockStateData = ubj.readByteArray("blockStateData")

        var posIndex = 0
        var blockIndex = 0
        var blockStateIndex = 0

        blockData.forEach { _ ->
            val x = posData[posIndex]
            val y = posData[posIndex + 1]
            val z = posData[posIndex + 2]

            val blockId = blockData[blockIndex]
            val blockStateId = blockStateData[blockStateIndex]

            // TODO: Set block state in blocks array for chunk here.

            posIndex += 3
            blockIndex++
            blockStateIndex++
        }
    }

    override fun write(): UBJ = synchronized(blockStates) {
        val ubj = UBJ()

        val posData = ByteArray(changes.size * 3)
        val blockData = ShortArray(changes.size)
        val blockStateData = ByteArray(changes.size)

        var posIndex = 0
        var blockIndex = 0
        var blockStateIndex = 0
        changes.forEach { (pos, block) ->
            posData[posIndex] = pos.x().toByte()
            posData[posIndex + 1] = pos.y().toByte()
            posData[posIndex + 2] = pos.z().toByte()

            // TODO:
            // blockData[blockIndex] = block.hashCode()
            // blockStateData[blockStateIndex] = block.hashCode()

            posIndex += 3
            blockIndex++
            blockStateIndex++
        }

        ubj.putByteArray("posData", posData)
        ubj.putShortArray("blockData", blockData)
        ubj.putByteArray("blockStateData", blockStateData)

        return ubj
    }

    override fun equals(other: Any?): Boolean {
        if(other !is Chunk) return false

        if (realm != other.realm) return false
        if (posX != other.posX) return false
        if (posY != other.posY) return false
        if (posZ != other.posZ) return false

        return true
    }

    fun toSimpleString(): String = "$posX.$posY.$posZ"
    fun toQualifiedString(): String = "${realm.name}_$posX.$posY.$posZ"
}