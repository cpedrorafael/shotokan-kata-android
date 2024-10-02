package com.guayaba.shotokankata

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guayaba.shotokankata.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog


@RunWith(AndroidJUnit4::class)
@Config(application = Application::class)
class QuizTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }


    @Test
    fun `when all correct answers are clicked, then score is 100%`(){
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity: MainActivity ->
                composeTestRule.onNodeWithTag("Quiz").performClick()
                composeTestRule.onNodeWithTag("How many moves in Kata").performClick()

                composeTestRule
                    .onNodeWithTag("Word Quiz").assertIsDisplayed()

            }
        }
    }
}