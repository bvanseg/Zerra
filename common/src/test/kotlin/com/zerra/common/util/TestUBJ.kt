package com.zerra.common.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigInteger

class TestUBJ {

    @Test
    fun testUBJChar() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putChar("value", 'a')

        // THEN
        assertEquals('a', ubj.readChar("value"))
    }

    @Test
    fun testUBJBoolean() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putBoolean("value", true)

        // THEN
        assertEquals(true, ubj.readBoolean("value"))
    }

    @Test
    fun testUBJByte() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putByte("value", -1)

        // THEN
        assertEquals(-1, ubj.readByte("value"))
    }

    @Test
    fun testUBJInt() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putInt("value", -1)

        // THEN
        assertEquals(-1, ubj.readInt("value"))
    }

    @Test
    fun testUBJLong() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putLong("value", -1)

        // THEN
        assertEquals(-1, ubj.readLong("value"))
    }

    @Test
    fun testUBJFloat() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putFloat("value", -1.1F)

        // THEN
        assertEquals(-1.1F, ubj.readFloat("value"))
    }

    @Test
    fun testUBJDouble() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putDouble("value", -1.1)

        // THEN
        assertEquals(-1.1, ubj.readDouble("value"))
    }

    @Test
    fun testUBJString() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putString("value", "Hello, world!")

        // THEN
        assertEquals("Hello, world!", ubj.readString("value"))
    }

    @Test
    fun testUBJBigInteger() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putBigInteger("value", BigInteger.ONE)

        // THEN
        assertEquals(BigInteger.ONE, ubj.readBigInteger("value"))
    }

    @Test
    fun testUBJBooleanArray() {
        // GIVEN
        val ubj = UBJ()
        val booleanArray = booleanArrayOf(true, false, false)

        // WHEN
        ubj.putBooleanArray("value", booleanArray)

        // THEN
        assertTrue(booleanArray.contentEquals(ubj.readBooleanArray("value")))
    }

    @Test
    fun testUBJByteArray() {
        // GIVEN
        val ubj = UBJ()
        val byteArray = byteArrayOf(-1, 0, 1)

        // WHEN
        ubj.putByteArray("value", byteArray)

        // THEN
        assertTrue(byteArray.contentEquals(ubj.readByteArray("value")))
    }

    @Test
    fun testUBJShortArray() {
        // GIVEN
        val ubj = UBJ()
        val shortArray = shortArrayOf(-1, 0, 1)

        // WHEN
        ubj.putShortArray("value", shortArray)

        // THEN
        assertTrue(shortArray.contentEquals(ubj.readShortArray("value")))
    }

    @Test
    fun testUBJIntArray() {
        // GIVEN
        val ubj = UBJ()
        val intArray = intArrayOf(-1, 0, 1)

        // WHEN
        ubj.putIntArray("value", intArray)

        // THEN
        assertTrue(intArray.contentEquals(ubj.readIntArray("value")))
    }

    @Test
    fun testUBJLongArray() {
        // GIVEN
        val ubj = UBJ()
        val longArray = longArrayOf(-1, 0, 1)

        // WHEN
        ubj.putLongArray("value", longArray)

        // THEN
        assertTrue(longArray.contentEquals(ubj.readLongArray("value")))
    }

    @Test
    fun testUBJFloatArray() {
        // GIVEN
        val ubj = UBJ()
        val floatArray = floatArrayOf(0.000001F, 0.03F, -0.2F)

        // WHEN
        ubj.putFloatArray("value", floatArray)

        // THEN
        assertTrue(floatArray.contentEquals(ubj.readFloatArray("value")))
    }

    @Test
    fun testUBJDoubleArray() {
        // GIVEN
        val ubj = UBJ()
        val doubleArray = doubleArrayOf(0.000000000000000001, 0.03, -0.2)

        // WHEN
        ubj.putDoubleArray("value", doubleArray)

        // THEN
        assertTrue(doubleArray.contentEquals(ubj.readDoubleArray("value")))
    }

    @Test
    fun testUBJStringArray() {
        // GIVEN
        val ubj = UBJ()
        val stringArray = arrayOf("Hello", ",", "world", "!")

        // WHEN
        ubj.putStringArray("value", stringArray)

        // THEN
        assertTrue(stringArray.contentEquals(ubj.readStringArray("value")))
    }
}