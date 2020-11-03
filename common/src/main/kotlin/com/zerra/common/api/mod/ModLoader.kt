package com.zerra.common.api.mod

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.evenir.bus.EventBus
import bvanseg.kotlincommons.javaclass.createNewInstance
import com.zerra.common.util.ModLoadException
import com.zerra.common.util.resource.MasterResourceManager
import com.zerra.common.util.storage.FileManager
import io.github.classgraph.ClassGraph
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import kotlin.reflect.jvm.jvmName


/**
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object ModLoader {

    val logger = getLogger()

    val EVENT_BUS = EventBus()

    val mods = mutableMapOf<String, Any>()

    private var hasLoaded = false

    fun loadAllMods() {
        injectMods()
        findAndLoadMods()
    }

    private fun findAndLoadMods() {
        if (hasLoaded) {
            throw IllegalStateException("Attempted to load mods when mods have already been loaded!")
        }

        MasterResourceManager.resources.apply {
            logger.debug("Loading mod instances...")
            val start = System.currentTimeMillis()
            getClassesWithAnnotation(Mod::class.jvmName).loadClasses().forEach { modClass ->
                loadMod(modClass)
            }
            logger.debug("Finished loading ${mods.size} mods in ${System.currentTimeMillis() - start}ms")
        }
        hasLoaded = true
    }

    private fun loadMod(modClass: Class<*>) = try {
        logger.trace("Attempting to load mod class ${modClass.name}")

        val mod = try {
            modClass.kotlin.objectInstance ?: createNewInstance(modClass)
        } catch(e: NoSuchMethodException) {
            throw ModLoadException("Failed to construct mod class instance for $modClass. Make sure you have a no-arg constructor!", e)
        } catch(e: UninitializedPropertyAccessException) {
            throw ModLoadException("Failed to construct mod class instance for $modClass. Make sure you have a no-arg constructor!", e)
        } ?: throw ModLoadException("Failed to load mod for class $modClass")

        val metadata = modClass.getAnnotation(Mod::class.java)
        val domain = metadata.domain

        if (domain.equals("zerra", true) && mod !is ZerraMod) {
            throw ModLoadException("The domain name '$domain' is the same as the game's domain")
        }

        if (mods.containsKey(domain)) {
            throw ModLoadException("The domain name '$domain' is already being used by another mod")
        }

        mods[domain] = mod
    } catch (e: Exception) {
        logger.error("Error trying to load mod class ${modClass.name}", e)
    }

    /**
     * Loads all mods onto the classpath from their respective JARs.
     */
    internal fun injectMods() {
        logger.info("Beginning mod injection")
        val start = System.currentTimeMillis()
        val mods = FileManager.getModJARs()

        val classGraph = ClassGraph().enableAllInfo()

        val urls = mutableListOf<URL>()

        mods.filter(Files::isRegularFile).forEach {
            val file = it.toFile()
            if (file.extension.toLowerCase() == "jar") {
                logger.info("Found potential mod JAR $it")
                urls.add(file.toURI().toURL())
            }
        }

        classGraph.addClassLoader(URLClassLoader(urls.toTypedArray(), this::class.java.classLoader))

        // Scan the resources after performing the injection.
        MasterResourceManager.scanResources(true, classGraph)
        logger.info("Finished mod injection in ${System.currentTimeMillis() - start}ms")
    }
}