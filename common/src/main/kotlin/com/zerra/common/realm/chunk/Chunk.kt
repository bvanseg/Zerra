package com.zerra.common.realm.chunk

import bvanseg.kotlincommons.number.HashCodeBuilder
import com.zerra.common.block.Block
import com.zerra.common.block.Blocks
import com.zerra.common.realm.Realm

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
class Chunk(val realm: Realm, val posX: Int, val posY: Int, val posZ: Int) {

    companion object {
        const val CHUNK_SIZE = 16 * 16 * 16
    }

    private val blocks = Array(CHUNK_SIZE) { Blocks.AIR }

    fun getBlock(x: Int, y: Int, z: Int) = blocks[x + y * 16 + z * 256]
    fun setBlock(block: Block, x: Int, y: Int, z: Int) = blocks[x + y * 16 + z * 256]

    override fun hashCode(): Int = HashCodeBuilder(this::class).append(realm).append(posX).append(posY).append(posZ).hashCode()
}