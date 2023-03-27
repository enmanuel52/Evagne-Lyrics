package com.example.evagnelyrics.data.repo

import com.example.evagnelyrics.domain.repo.ScreenStack
import com.example.evagnelyrics.ui.navigation.Route

class ScreenStackImpl : ScreenStack {
    private val stack = mutableListOf<Route>()

    private var previousScreen: Route? = null

    override val previous: Route?
        get() = previousScreen

    override fun push(screen: Route) {
        if (stack.isNotEmpty() && !stack.contains(screen)) {
            previousScreen = stack.last()
        }
        stack.add(screen)
    }

    override fun pop(): Route? = stack.let {
        if (stack.isNotEmpty()) {
            previousScreen = stack.last()
        }
        it.removeLastOrNull()
    }
}