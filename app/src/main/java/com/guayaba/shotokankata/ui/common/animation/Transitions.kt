package com.guayaba.shotokankata.ui.common.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

fun EnterTransition.slideDown(): EnterTransition =
    this + scaleIn(initialScale = 0.5f) + fadeIn() +
            slideInVertically(initialOffsetY = { -40 }) +
            scaleIn(initialScale = 5.0f) +
            fadeIn(initialAlpha = 0.3f)


fun ExitTransition.slideUp(): ExitTransition =
    this + scaleOut(targetScale = 0.5f) + fadeOut() +
            slideOutVertically(targetOffsetY = { -40 }) +
            scaleOut(targetScale = .3f) +
            fadeOut(targetAlpha = 0.7f)
