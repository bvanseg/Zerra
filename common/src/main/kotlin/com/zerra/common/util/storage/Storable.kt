package com.zerra.common.util.storage

import com.devsmart.ubjson.UBObject

/**
 * An interface representing an object that can be written to and read from the Universal Binary JSON (UBJSON) format.
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
interface Storable {
    fun read(): UBObject
    fun write(ubj: UBObject)
}