package com.example.evagnelyrics.domain.repo

import com.example.evagnelyrics.ui.navigation.Route

interface ScreenStack {

    val previous: Route?

    fun push(screen: Route)

    fun pop(): Route?
}