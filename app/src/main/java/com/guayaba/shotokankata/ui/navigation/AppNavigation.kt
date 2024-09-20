package com.guayaba.shotokankata.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.ui.calendar.SessionCalendar
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
    philosophyViewModel: PhilosophyViewModel,
    kataViewModel: KataViewModel,
    quizViewModel: QuizViewModel,
    sessionViewModel: SessionViewModel
) {
    val navController = rememberNavController()
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
            NijuKun(viewModel = philosophyViewModel, navController, Routes.SESSION_CALENDAR)
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
        composable(Routes.SESSION_CALENDAR.route) { 
            SessionCalendar(sessionViewModel){
                navController.navigate(Routes.HOME.route)
            }
        }

    }
}