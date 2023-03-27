package com.example.evagnelyrics.data.repo

import android.util.Log
import com.example.evagnelyrics.EvagneLyricsApp.Companion.TAG
import com.example.evagnelyrics.domain.repo.ScreenStack
import com.example.evagnelyrics.ui.navigation.Route

class ScreenStackImpl : ScreenStack {
    private val stack = mutableSetOf<Route>()

    private var previousScreen: Route? = null

    override val previous: Route?
        get() = previousScreen

    override fun push(screen: Route) {
        if (stack.isNotEmpty()) {
            previousScreen = stack.last()
        }
        stack.add(screen)
        Log.d(TAG, "push: $stack")
    }

    override fun pop(): Route?{
        if (stack.isNotEmpty()) {
            previousScreen = stack.last()
            stack.remove(previousScreen)
            Log.d(TAG, "pop: $stack")
            return previousScreen
        }
        else return null
    }
}