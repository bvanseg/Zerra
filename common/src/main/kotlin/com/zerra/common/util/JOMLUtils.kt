package com.zerra.common.util

import org.joml.*
import java.lang.Math

operator fun Matrix4fc.times(other: Matrix4fc): Matrix4f = this.mul(other, Matrix4f())
operator fun Matrix4f.timesAssign(other: Matrix4fc) {
    this.mul(other)
}

operator fun Matrix4fc.times(other: Vector3fc): Matrix4f = this.scale(other, Matrix4f())
operator fun Matrix4f.timesAssign(other: Vector3fc) {
    this.scale(other)
}

operator fun Matrix4fc.plus(other: Matrix4fc): Matrix4f = this.add(other, Matrix4f())
operator fun Matrix4f.plusAssign(other: Matrix4fc) {
    this.add(other)
}

operator fun Matrix4fc.plus(other: Vector3fc): Matrix4f = this.translate(other, Matrix4f())
operator fun Matrix4f.plusAssign(other: Vector3fc) {
    this.translate(other)
}

operator fun Matrix4fc.minus(other: Matrix4fc): Matrix4f = this.sub(other, Matrix4f())
operator fun Matrix4f.minusAssign(other: Matrix4fc) {
    this.sub(other)
}

operator fun Matrix4fc.minus(other: Vector3fc): Matrix4f = this.translate(-other, Matrix4f())
operator fun Matrix4f.minusAssign(other: Vector3fc) {
    this.translate(-other)
}

operator fun Vector3fc.unaryMinus(): Vector3f = this.mul(-1f, -1f, -1f, Vector3f())

@Suppress("UNCHECKED_CAST")
fun <T : Number> Number.toRadians() = Math.toRadians(this.toDouble()) as T

@Suppress("UNCHECKED_CAST")
fun <T : Number> Number.toDegrees() = Math.toDegrees(this.toDouble()) as T

fun main() {
    val matrix4f = Matrix4f()

    matrix4f += Vector3f(3f)
    matrix4f.rotate(45f.toRadians(), 1f, 0f, 0f)
    matrix4f *= Vector3f(4f)

    println(matrix4f)
}

fun Vector2ic.toArray(): IntArray = intArrayOf(this.x(), this.y())
fun Vector2fc.toArray(): FloatArray = floatArrayOf(this.x(), this.y())
fun Vector2dc.toArray(): DoubleArray = doubleArrayOf(this.x(), this.y())

fun Vector3ic.toArray(): IntArray = intArrayOf(this.x(), this.y(), this.z())
fun Vector3fc.toArray(): FloatArray = floatArrayOf(this.x(), this.y(), this.z())
fun Vector3dc.toArray(): DoubleArray = doubleArrayOf(this.x(), this.y(), this.z())
