package com.zerra.client.shader

import bvanseg.kotlincommons.any.getLogger
import org.joml.*
import org.lwjgl.opengl.GL33C.*
import org.lwjgl.system.NativeResource

class ShaderProgram : NativeResource {

    private var program = 0
    private val uniformLocations = HashMap<CharSequence, Int>()

    private fun getUniformLocation(uniformName: CharSequence): Int {
        if (program == 0)
            return -1
        return uniformLocations.computeIfAbsent(uniformName) {
            val uniformLocation = glGetUniformLocation(program, it)
            if (uniformLocation == -1)
                logger.warn("Could not find uniform $uniformName")
            uniformLocation
        }
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

    fun bind() {
        glUseProgram(program)
    }

    override fun free() {
        if (program != 0) {
            glDeleteProgram(program)
            program = 0
        }
    }

    companion object {
        private val logger = getLogger()
        private val MATRIX_4_4 = FloatArray(16)
        private val MATRIX_4_3 = FloatArray(12)
        private val MATRIX_3_3 = FloatArray(9)
        private val MATRIX_3_2 = FloatArray(6)
        private val MATRIX_2_2 = FloatArray(4)

//        private fun loadShader(shaderName: CharSequence, resourceManager: ResourceManager): Shader? {
//            return resourceManager.get
//        }

        fun unbind() {
            glUseProgram(0)
        }
    }
}