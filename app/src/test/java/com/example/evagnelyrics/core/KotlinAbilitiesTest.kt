package com.example.evagnelyrics.core

import org.junit.Test

class KotlinAbilitiesTest {

    @Test
    fun `when a passed a invalid name to matchName returns false`() {
        val match = "Jorge M".let(String::matchName)
        assert(!match)
    }

    @Test
    fun `when a passed a valid name to matchName returns true`() {
        val myMatch = MyMatch()
        val match = "Jorge M.".let(myMatch::matchName)
        assert(match)
    }

    @Test
    fun `when a passed a valid names to matchName returns true always`() {
        val names = listOf("Jorge M.", "Juan Carlos", "J. Miguel", "Paco")
        val match = names.all(String::matchName)
        assert(match)
    }
}