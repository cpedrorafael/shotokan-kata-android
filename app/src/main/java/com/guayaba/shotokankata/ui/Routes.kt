package com.guayaba.shotokankata.ui

enum class Routes (val route: String){
    HOME("home"),
    DETAILS("details/{kataId}"),
    QUIZZES("quizzes"),
    WORD_QUIZ("word_quiz")
}