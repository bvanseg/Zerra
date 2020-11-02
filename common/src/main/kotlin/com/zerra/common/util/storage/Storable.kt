package com.zerra.common.util.storage

/**
 * An interface representing an object that can be written to and read from the Universal Binary JSON (UBJSON) format.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
interface Storable {
    fun write(): UBJ
    fun read(ubj: UBJ)
}