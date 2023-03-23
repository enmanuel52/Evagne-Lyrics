package com.example.evagnelyrics.core

fun String.matchName() = run {
    val name = "[A-Z][a-z]+"
    val initialDot = "[A-Z][.]"
    val patterns = listOf(
        name,
        "$initialDot\\s$name",
        "$name\\s$initialDot",
        "$name\\s$name"
    ).map(String::toRegex)
    return@run patterns.any(::matches)
}

class MyMatch {
    fun matchName(value: String) = value.matchName()
}