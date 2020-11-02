package com.zerra.common.util.resource

import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

/**
 * Used to access resources with help from [ClassGraph].
 *
 * @author Mark Passey
 * @since 0.0.1
 */
object Resources {

    private var initialized = false
    val scan: ScanResult by lazy {
        initialized = true
        return@lazy ClassGraph().enableAllInfo().scan()
    }

    private fun close() = if (initialized) scan.close() else Unit
}