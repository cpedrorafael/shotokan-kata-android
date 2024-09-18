package com.guayaba.shotokankata.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guayaba.shotokankata.core.notifications.NotificationReceiver
import com.guayaba.shotokankata.core.notifications.NotificationUtils
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.ui.kata.KataList
import com.guayaba.shotokankata.ui.kata.KataView
import com.guayaba.shotokankata.ui.kata.KataViewModel
import com.guayaba.shotokankata.ui.philosophy.NijuKun
import com.guayaba.shotokankata.ui.philosophy.PhilosophyViewModel
import com.guayaba.shotokankata.ui.quiz.QuizList
import com.guayaba.shotokankata.ui.quiz.QuizViewModel
import com.guayaba.shotokankata.ui.quiz.WordQuiz
import com.guayaba.shotokankata.ui.theme.ShotokanKataTheme

class MainActivity : ComponentActivity() {
    private val kataViewModel: KataViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()
    private val philosophyViewModel: PhilosophyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShotokanKataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Box(
                        Modifier.padding(8.dp)
                    ) {
                        NotificationPermissionRequest {
                            NotificationUtils.createNotificationChannel(this@MainActivity)
                            setDailyNotification()
                        }
                        NavHost(
                            navController = navController,
                            startDestination = Routes.NIJU_KUN_RANDOM.route
                        ) {
                            composable(
                                Routes.NIJU_KUN_RANDOM.route,
                                enterTransition = {
                                    scaleIntoContainer()
                                },
                                exitTransition = {
                                    scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS)
                                },
                                popEnterTransition = {
                                    scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS)
                                },
                                popExitTransition = {
                                    scaleOutOfContainer()
                                }
                            ) {
                                NijuKun(viewModel = philosophyViewModel, navController)
                            }
                            composable(Routes.HOME.route) { KataList(kataViewModel, navController) }
                            composable(
                                Routes.DETAILS.route,
                                arguments = listOf(navArgument("kataId") { type = NavType.IntType })
                            ) {
                                val kataInfo =
                                    KataInfo.findById(it.arguments?.getInt("kataId") ?: 1)
                                KataView(kataViewModel, kataInfo) {
                                    navController.popBackStack()
                                }
                            }
                            composable(Routes.QUIZZES.route) {
                                QuizList(navController)
                            }
                            composable(
                                Routes.WORD_QUIZ.route + "/{quizId}",
                                arguments = listOf(navArgument("quizId") { type = NavType.IntType })
                                ) {
                                    val quizId = it.arguments?.getInt("quizId") ?: 0
                                    WordQuiz(viewModel = quizViewModel, navController, quizId)
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

