package com.zerra.common.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TestUBJ {

    @Test
    fun testUBJBoolean() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putBoolean("flag", true)

        // THEN
        assertEquals(true, ubj.readBoolean("flag"))
    }

    @Test
    fun testUBJByte() {
        // GIVEN
        val ubj = UBJ()

        // WHEN
        ubj.putByte("byte", -1)

        // THEN
        assertEquals(-1, ubj.readByte("byte"))
    }
}