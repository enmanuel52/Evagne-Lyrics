package com.example.evagnelyrics.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.evagnelyrics.ui.navigation.AppNavigation
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EndToEndTest {

    @get: Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {

        composeRule.setContent {
            EvagneLyricsTheme(darkTheme = true) {
                Surface(color = MaterialTheme.colorScheme.background){ AppNavigation() }
            }
        }
    }

    @Test
    fun checkAppNameDisplays(){

        composeRule.onNodeWithTag("app title")
            .assertTextEquals("Evagne Lyrics")

        composeRule.onNodeWithText("Fotografías")
            .assertExists()
            .performClick()

        composeRule.onNodeWithTag("gallery title")
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithContentDescription("back on song")
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithTag("app title")
            .assertTextEquals("Evagne Lyrics")
    }
}