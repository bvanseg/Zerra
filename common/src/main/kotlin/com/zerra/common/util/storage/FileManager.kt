package com.zerra.common.util.storage

import com.zerra.common.Zerra
import com.zerra.common.realm.Universe
import com.zerra.common.state.GameState
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
internal object FileManager {

    val ROOT_DIR = Path.of("data")

    val SAVES_DIR by lazy {
        val path = ROOT_DIR.resolve("saves")
        Files.createDirectories(path)
        path
    }

    val MODS_DIR: Path by lazy {
        val path = ROOT_DIR.resolve("mods")
        Files.createDirectories(path)
        path
    }

    fun getModJARs(): Stream<Path> = Files.walk(MODS_DIR).filter(Files::isRegularFile)

    fun getCurrentSaveFolder(): Path {
        val zerra = Zerra.getInstance()
        val state = zerra.getStateManager().activeState

        if (state !is GameState) throw IllegalStateException("Attempted to fetch save folder while outside of game state!")
        val universe = state.universe

        return SAVES_DIR.resolve(universe.name)
    }
}