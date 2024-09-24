package com.guayaba.shotokankata.ui.navigation

enum class Routes (val route: String){
    HOME("home"),
    DETAILS("details/{kataId}"),
    QUIZZES("quizzes"),
    WORD_QUIZ("word_quiz"),
    NIJU_KUN_RANDOM("nijukunrandom"),
    SESSION_CALENDAR("calendar"),
    SESSION_DAY("session"),
    LOG_SESSIONS("log")
}