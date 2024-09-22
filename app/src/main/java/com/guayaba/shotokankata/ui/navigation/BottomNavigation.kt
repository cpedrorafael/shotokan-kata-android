package com.guayaba.shotokankata.ui.navigation

import com.guayaba.shotokankata.R

enum class BottomNavItem(val route: String, val resource: Int, val label: String) {
    HOME(Routes.SESSION_CALENDAR.route, R.drawable.mushin, "Home"),
    KATA(Routes.HOME.route, R.drawable.manjiuke, "Katas"),
    QUIZ(Routes.QUIZZES.route, R.drawable.brain, "Quiz")
}