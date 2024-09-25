package com.guayaba.shotokankata.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guayaba.shotokankata.core.notifications.NotificationReceiver
import com.guayaba.shotokankata.core.notifications.NotificationUtils
import com.guayaba.shotokankata.ui.calendar.SessionViewModel
import com.guayaba.shotokankata.ui.kata.KataViewModel
import com.guayaba.shotokankata.ui.navigation.AppNavigation
import com.guayaba.shotokankata.ui.navigation.BottomNavigationBar
import com.guayaba.shotokankata.ui.navigation.Routes
import com.guayaba.shotokankata.ui.philosophy.PhilosophyViewModel
import com.guayaba.shotokankata.ui.quiz.QuizViewModel
import com.guayaba.shotokankata.ui.theme.ShotokanKataTheme

class MainActivity : ComponentActivity() {
    private val kataViewModel: KataViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()
    private val philosophyViewModel: PhilosophyViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShotokanKataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    when (navBackStackEntry?.destination?.route) {
                        Routes.SESSION_CALENDAR.route -> {
                            bottomBarState.value = true
                        }

                        Routes.HOME.route -> {
                            bottomBarState.value = true
                        }

                        Routes.QUIZZES.route -> {
                            bottomBarState.value = true
                        }

                        else -> {
                            bottomBarState.value = false
                        }
                    }


                    Box(
                        Modifier.padding(8.dp)
                    ) {
                        NotificationPermissionRequest {
                            NotificationUtils.createNotificationChannel(this@MainActivity)
                            setDailyNotification()
                        }
                        AppNavigation(
                            navController,
                            philosophyViewModel,
                            kataViewModel,
                            quizViewModel,
                            sessionViewModel,
                            Modifier.fillMaxHeight(0.9f)
                        )

                        AnimatedVisibility(
                            visible = bottomBarState.value,
                            enter = slideInVertically(initialOffsetY = { it }),
                            exit = slideOutVertically(targetOffsetY = { it }),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxHeight(0.1f)
                                .fillMaxWidth()
                        ) {
                            BottomNavigationBar(
                                navController = navController, modifier = Modifier
                                    .align(
                                        Alignment.BottomCenter
                                    )
                                    .height(64.dp)
                            ) {
                                sessionViewModel.update()
                            }
                        }
                    }
                }
            }
        }

    }


    fun setDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}

