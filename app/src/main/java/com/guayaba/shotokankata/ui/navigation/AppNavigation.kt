package com.guayaba.shotokankata.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.ui.calendar.SessionCalendarPage
import com.guayaba.shotokankata.ui.calendar.SessionDayView
import com.guayaba.shotokankata.ui.calendar.SessionKataDaySelector
import com.guayaba.shotokankata.ui.calendar.SessionViewModel
import com.guayaba.shotokankata.ui.kata.KataList
import com.guayaba.shotokankata.ui.kata.KataView
import com.guayaba.shotokankata.ui.kata.KataViewModel
import com.guayaba.shotokankata.ui.philosophy.NijuKun
import com.guayaba.shotokankata.ui.philosophy.PhilosophyViewModel
import com.guayaba.shotokankata.ui.quiz.QuizList
import com.guayaba.shotokankata.ui.quiz.QuizViewModel
import com.guayaba.shotokankata.ui.quiz.WordQuiz

@Composable
fun AppNavigation(
    navController: NavHostController,
    philosophyViewModel: PhilosophyViewModel,
    kataViewModel: KataViewModel,
    quizViewModel: QuizViewModel,
    sessionViewModel: SessionViewModel,
) {

    NavHost(
        navController = navController,
        startDestination = Routes.NIJU_KUN_RANDOM.route,
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
            NijuKun(
                viewModel = philosophyViewModel,
            ){
                navController.navigate(Routes.SESSION_CALENDAR.route)
            }
        }

        composable(Routes.HOME.route) {
            KataList(kataViewModel, onNavigateToDetails = { kataId ->
                navController.navigate("details/$kataId")
            })
        }

        composable(
            Routes.DETAILS.route,
            arguments = listOf(navArgument("kataId") { type = NavType.IntType })
        ) {
            val kataInfo =
                KataInfo.findById(it.arguments?.getInt("kataId") ?: 1)
            KataView(kataViewModel, kataInfo) {
                navController.popBackStack()
                sessionViewModel.update()
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
        composable(Routes.SESSION_CALENDAR.route)
        {
            SessionCalendarPage(
                sessionViewModel,
                onAddPracticeSession = {
                    navController.navigate(Routes.HOME.route)
                }, onDayClicked = {
                    navController.navigate(Routes.SESSION_DAY.route + "/$it")
                })
        }
        composable(
            Routes.SESSION_DAY.route + "/{date}",
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ) {
            it.arguments?.let { bundle ->
                val arg = bundle.getLong("date")
                SessionDayView(
                    sessionViewModel,
                    dateLong = arg,
                    onBackPressed = {
                        navController.popBackStack()
                        sessionViewModel.update()
                    },
                    onLogSessions = {
                        navController.navigate(Routes.LOG_SESSIONS.route + "/$arg")
                    }
                )
            }
        }
        composable(
            Routes.LOG_SESSIONS.route + "/{date}",
            arguments = listOf(navArgument("date") { type = NavType.LongType })
        ) {
            it.arguments?.let { bundle ->
                val arg = bundle.getLong("date")
                SessionKataDaySelector(
                    dateLong = arg,
                    sessionViewModel,
                ){
                    navController.popBackStack()
                }
            }
        }
    }
}