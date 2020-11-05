package com.zerra.client.shader

import bvanseg.kotlincommons.any.getLogger
import bvanseg.kotlincommons.stream.stream
import com.zerra.client.util.Bindable
import com.zerra.common.util.resource.ResourceLocation
import org.joml.*
import org.lwjgl.opengl.GL20C
import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.NativeResource
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

/**
 * @author Ocelot5836
 * @since 0.0.1
 */
class Shader internal constructor(private val name: ResourceLocation) : Bindable, NativeResource {

    private var program = 0
    private val shaders = IntArray(ShaderType.values().size)
    private val uniformLocations = HashMap<CharSequence, Int>()

    private fun getUniformLocation(uniformName: CharSequence): Int {
        if (program == 0)
            return -1
        return uniformLocations.computeIfAbsent(uniformName) {
            val uniformLocation = glGetUniformLocation(program, it)
            if (uniformLocation == -1)
                logger.warn("Could not find uniform '$uniformName'")
            uniformLocation
        }
    }

    private fun compile(type: ShaderType, source: CharSequence) {
        val shader = glCreateShader(type.shaderType)
        glShaderSource(shader, source)
        glCompileShader(shader)

        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE)
            throw IOException(GL20C.glGetShaderInfoLog(shader))
        logger.debug("Compiled ${type.name.toLowerCase(Locale.ROOT)} shader for ${name.domain}/${name.location} shader")

        if (program == 0)
            program = glCreateProgram()

        glAttachShader(program, shader)
        shaders[type.ordinal] = shader
    }

    private fun link() {
        glLinkProgram(program)
        if (glGetProgrami(program, GL_LINK_STATUS) != GL_TRUE)
            throw IOException(glGetProgramInfoLog(program))
        logger.debug("Linked ${name.domain}/${name.location} shader")
    }

    /**
     * Reloads all shader types and regenerates the program.
     */
    fun load() {
        free()
        try {
            for (type in ShaderType.values()) {
                val source = loadShader(ResourceLocation(name.resourceManager, name.domain, "shaders/${name.location}_${type.extension}.glsl")) ?: continue
                compile(type, source)
            }

            if (program == 0) {
                logger.warn("No shader sources were found for ${name.domain}/${name.location} shader.")
                return
            }

            link()
        } catch (e: Exception) {
            logger.error("Failed to create ${name.domain}/${name.location} shader.", e)
            free()
        }
    }

    /**
     * Only reloads the specified shader types.
     */
    fun reload(vararg types: ShaderType) {
        if (types.isEmpty())
            throw IllegalArgumentException("No shaders specified to reload")
        if (program == 0)
            throw IllegalStateException("Shader has not been loaded")

        try {
            for (type in types.stream().distinct()) {
                logger.debug("Reloading ${type.name.toLowerCase(Locale.ROOT)}")
                val shader = shaders[type.ordinal]
                if (shader != 0) {
                    glDetachShader(program, shader)
                    glDeleteShader(shader)
                }

                if (!name.resourceManager.resourceExists("${name.location}_${type.extension}.glsl"))
                    continue
                val source = loadShader(ResourceLocation(name.resourceManager, name.domain, "${name.location}_${type.extension}.glsl"))!!
                compile(type, source)
            }
            link()
        } catch (e: Exception) {
            logger.error("Failed to reload $name shader.", e)
            free()
        }
    }

    override fun bind() {
        glUseProgram(program)
    }

    override fun unbind() {
        glUseProgram(0)
    }

    override fun free() {
        if (program != 0) {
            glDeleteProgram(program)
            program = 0
        }
        for (shader in shaders)
            glDeleteShader(shader)
        shaders.fill(0)
    }

    fun loadBoolean(uniformName: CharSequence, value: Boolean) =
        glUniform1i(getUniformLocation(uniformName), if (value) 1 else 0)

    fun loadUInteger(uniformName: CharSequence, value: Int) =
        glUniform1ui(getUniformLocation(uniformName), value)

    fun loadInteger(uniformName: CharSequence, value: Int) =
        glUniform1i(getUniformLocation(uniformName), value)

    fun loadFloat(uniformName: CharSequence, value: Float) =
        glUniform1f(getUniformLocation(uniformName), value)

    fun loadVector2i(uniformName: CharSequence, value: Vector2ic) =
        glUniform2i(getUniformLocation(uniformName), value[0], value[1])

    fun loadVector2i(uniformName: CharSequence, x: Int, y: Int) =
        glUniform2i(getUniformLocation(uniformName), x, y)

    fun loadVector2f(uniformName: CharSequence, value: Vector2fc) =
        glUniform2f(getUniformLocation(uniformName), value[0], value[1])

    fun loadVector2f(uniformName: CharSequence, x: Float, y: Float) =
        glUniform2f(getUniformLocation(uniformName), x, y)

    fun loadVector3i(uniformName: CharSequence, value: Vector3ic) =
        glUniform3i(getUniformLocation(uniformName), value[0], value[1], value[2])

    fun loadVector3i(uniformName: CharSequence, x: Int, y: Int, z: Int) =
        glUniform3i(getUniformLocation(uniformName), x, y, z)

    fun loadVector3f(uniformName: CharSequence, value: Vector3fc) =
        glUniform3f(getUniformLocation(uniformName), value[0], value[1], value[2])

    fun loadVector3f(uniformName: CharSequence, x: Float, y: Float, z: Float) =
        glUniform3f(getUniformLocation(uniformName), x, y, z)

    fun loadVector4i(uniformName: CharSequence, value: Vector4ic) =
        glUniform4i(getUniformLocation(uniformName), value[0], value[1], value[2], value[3])

    fun loadVector4i(uniformName: CharSequence, x: Int, y: Int, z: Int, w: Int) =
        glUniform4i(getUniformLocation(uniformName), x, y, z, w)

    fun loadVector4f(uniformName: CharSequence, value: Vector4fc) =
        glUniform4f(getUniformLocation(uniformName), value[0], value[1], value[2], value[3])

    fun loadVector4f(uniformName: CharSequence, x: Float, y: Float, z: Float, w: Float) =
        glUniform4f(getUniformLocation(uniformName), x, y, z, w)

    fun loadMatrix4f(uniformName: CharSequence, matrix4f: Matrix4fc) =
        glUniformMatrix4fv(getUniformLocation(uniformName), false, matrix4f.get(MATRIX_4_4))

    fun loadMatrix4x3f(uniformName: CharSequence, matrix4x3f: Matrix4x3fc) =
        glUniformMatrix4x3fv(getUniformLocation(uniformName), false, matrix4x3f.get(MATRIX_4_3))

    fun loadMatrix3f(uniformName: CharSequence, matrix3f: Matrix3fc) =
        glUniformMatrix3fv(getUniformLocation(uniformName), false, matrix3f.get(MATRIX_3_3))

    fun loadMatrix3x2f(uniformName: CharSequence, matrix3x2f: Matrix3x2fc) =
        glUniformMatrix3x2fv(getUniformLocation(uniformName), false, matrix3x2f.get(MATRIX_3_2))

    fun loadMatrix2f(uniformName: CharSequence, matrix2f: Matrix2fc) =
        glUniformMatrix2fv(getUniformLocation(uniformName), false, matrix2f.get(MATRIX_2_2))

    companion object {
        private val logger = getLogger()
        private val MATRIX_4_4 = FloatArray(16)
        private val MATRIX_4_3 = FloatArray(12)
        private val MATRIX_3_3 = FloatArray(9)
        private val MATRIX_3_2 = FloatArray(6)
        private val MATRIX_2_2 = FloatArray(4)

        // TODO make async
        private fun loadShader(shaderLocation: ResourceLocation): CharSequence? {
            if (!shaderLocation.resourceExists)
                return null
            return shaderLocation.inputStream!!.use {
                val out = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var length: Int
                while (it.read(buffer).also { l -> length = l } >= 0)
                    out.write(buffer, 0, length)

                out.toString(Charsets.UTF_8)
            }
        }
    }
}