package com.guayaba.shotokankata.ui.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guayaba.shotokankata.R
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.ui.common.AppTopBar
import com.guayaba.shotokankata.utils.toLocalDate
import java.time.format.DateTimeFormatter
import com.guayaba.shotokankata.ui.common.animation.slideDown
import com.guayaba.shotokankata.ui.common.animation.slideUp
@Composable
fun SessionKataDaySelector(
    dateLong: Long,
    viewModel: SessionViewModel,
    onNavigateBack: () -> Unit
) {
    val date = dateLong.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
    val formattedDate = date.format(formatter)


    val records = viewModel.sessionDay.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getSessionsForDay(date)
    }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(title = "Sessions", onBackArrowPressed = onNavigateBack)
                Text(
                    text = formattedDate,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Select the Katas you practiced",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            KataInfo.entries.map { kata ->
                val defaultValue = records.value.filter { it.kataId == kata.id }.size == 1
                SessionKata(kata.kataName, defaultValue) { selected ->
                    viewModel.updateKata(kata.id, date, selected)
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    HorizontalDivider(
                     Modifier.padding(30.dp, 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SessionKata(name: String, defaultValue: Boolean, onClick: (Boolean) -> Unit) {
    val state = remember {
        mutableStateOf(defaultValue)
    }
    Box(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(32.dp, 8.dp)
                .fillMaxWidth()
                .clickable {
                    state.value = !state.value
                    onClick(state.value)
                }, verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            AnimatedVisibility(
                visible = state.value,
                enter = fadeIn(animationSpec = tween(durationMillis = 150)) +
                        EnterTransition.None.slideDown(),
                exit = fadeOut(animationSpec = tween(durationMillis = 150)) +
                        ExitTransition.None.slideUp()
                ) {
                Icon(
                    painter = painterResource(id = R.drawable.mushin),
                    contentDescription = "Mushin",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

    }
}
