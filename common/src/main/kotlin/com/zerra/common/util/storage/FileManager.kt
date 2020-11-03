package com.zerra.common.util.storage

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
internal object FileManager {

    val ROOT_DIR = Path.of("data")
    val MODS_DIR: Path by lazy {
        val path = ROOT_DIR.resolve("mods")
        Files.createDirectories(path)
        path
    }

    fun getModJARs(): Stream<Path> = Files.walk(MODS_DIR).filter(Files::isRegularFile)
}