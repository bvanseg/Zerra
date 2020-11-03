package com.zerra.common.util.resource

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestResourceManager {

    @Test
    fun testResourceLocationDetails() {
        // GIVEN
        val manager = MasterResourceManager.createResourceManager("assets/", "zerra")

        // WHEN
        MasterResourceManager.scanResources()
        val foobar = manager.createResourceLocation("foobar.txt")

        // THEN
        assertEquals("zerra", foobar.domain)
        assertEquals("foobar.txt", foobar.location)
        assertEquals("assets/zerra/foobar.txt", foobar.path)
    }

    @Test
    fun testResourceLocationAccess() {
        // GIVEN
        val manager = MasterResourceManager.createResourceManager("assets/", "zerra")

        // WHEN
        MasterResourceManager.scanResources()
        val foobar = manager.createResourceLocation("foobar.txt")

        // THEN
        assertTrue(foobar.file != null)
        assertTrue(foobar.inputStream != null)

        foobar.inputStream?.close()
    }
}