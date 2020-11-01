package com.zerra.common.util

import org.joml.Matrix4f
import org.joml.Matrix4fc
import org.joml.Vector3fc

/**
 * Utility to transform matrices. The value of [get] is not persistent and must be copied.
 *
 * @author Ocelot
 * @since 0.0.1
 */
class TransformationHelper {

    private val matrix4f = Matrix4f()

    fun reset(): TransformationHelper {
        matrix4f.identity()
        return this
    }

    fun mul(matrix4fc: Matrix4fc): TransformationHelper {
        matrix4f *= matrix4fc
        return this
    }

    fun translate(value: Vector3fc): TransformationHelper {
        matrix4f += value
        return this
    }

    fun translate(x: Number, y: Number, z: Number): TransformationHelper {
        matrix4f.translate(x.toFloat(), y.toFloat(), z.toFloat())
        return this
    }

    fun rotate(angle: Double, x: Float, y: Float, z: Float, degrees: Boolean = true): TransformationHelper {
        matrix4f.rotate(if (degrees) angle.toRadians() else angle.toFloat(), x, y, z)
        return this
    }

    fun rotate(angle: Double, value: Vector3fc, degrees: Boolean = true): TransformationHelper {
        matrix4f.rotate(if (degrees) angle.toRadians() else angle.toFloat(), value)
        return this
    }

    fun scale(factor: Float): TransformationHelper {
        matrix4f.scale(factor)
        return this
    }

    fun scale(x: Float, y: Float, z: Float): TransformationHelper {
        matrix4f.scale(x, y, z)
        return this
    }

    fun scale(vector3fc: Vector3fc): TransformationHelper {
        matrix4f.scale(vector3fc)
        return this
    }

    fun value(): Matrix4fc = matrix4f

    companion object {
        private val TRANSFORMATIONS: ThreadLocal<TransformationHelper> =
            ThreadLocal.withInitial { TransformationHelper() }

        fun get(): TransformationHelper {
            return TRANSFORMATIONS.get().reset()
        }
    }
}