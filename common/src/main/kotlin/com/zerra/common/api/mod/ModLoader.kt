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

    val mods = mutableMapOf<String, Mod>()

    private var hasLoaded = false

    /**
     * Loads all mods onto the classpath.
     */
    internal fun loadMods() {
        if (hasLoaded) {
            throw IllegalStateException("Attempted to load mods when mods have already been loaded!")
        }

        MasterResourceManager.resources.apply {
            logger.debug("Loading mod instances...")
            getClassesImplementing(Mod::class.jvmName).loadClasses().forEach { modClass ->
                loadMod(modClass)
            }
            logger.debug("Finished loading ${mods.size} mods")

//            logger.debug("Loading ${configAnnotationKClass.qualifiedName} annotated classes...")
//            getClassesWithAnnotation(configAnnotationClass.name).forEach { loadConfigClass(it) }
//            logger.debug("Finished loading ${configAnnotationKClass.simpleName} classes for mods")
//
//            logger.debug("Loading ${registerAnnotationKClass.qualifiedName} annotated classes...")
//            getClassesWithAnnotation(registerAnnotationClass.name).forEach { loadRegisterClass(it) }
//            logger.debug("Finished loading ${registerAnnotationKClass.simpleName} classes for mods")
        }
        hasLoaded = true
    }

    private fun loadMod(modClass: Class<*>) = try {
        logger.trace("Trying to load mod class ${modClass.name}")

        val mod = createNewInstance(modClass) as Mod?
                ?: throw ModLoadException("Failed to load mod for class $modClass")
        val domain = mod.domain

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

    internal fun injectMods() {
        val mods = FileManager.getModJARs()

        val classGraph = ClassGraph().enableClassInfo()

        val urls = mods.filter(Files::isRegularFile).map { it.toFile().toURI().toURL() }.toArray() as Array<out URL>
        classGraph.addClassLoader(URLClassLoader(urls, this::class.java.classLoader))

        val results = classGraph.scan()

        // TODO
    }
}