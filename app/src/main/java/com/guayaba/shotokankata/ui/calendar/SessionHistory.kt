package com.guayaba.shotokankata.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guayaba.shotokankata.R
import com.guayaba.shotokankata.ui.common.AppTopBar
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun SessionCalendar(
    viewModel: SessionViewModel,
    onAddPracticeSession: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val days = DateUtil.daysOfWeek

    Scaffold(
        topBar = {
            AppTopBar(title = "Training Sessions")
        },
        floatingActionButton = {
            Button(onClick = {
                onAddPracticeSession()
            }) {
               Text(text = "Add")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Card(
                shape = CardDefaults.outlinedShape,
                modifier = Modifier.wrapContentHeight().padding(0.dp, 32.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                ) {
                    Row {
                        repeat(days.size) {
                            val item = days[it]
                            DayItem(item, modifier = Modifier.weight(1f))
                        }
                    }
                    Header(
                        yearMonth = uiState.yearMonth,
                        onPreviousMonthButtonClicked = {
                            viewModel.toPreviousMonth(it)
                        },
                        onNextMonthButtonClicked = {
                            viewModel.toNextMonth(it)
                        }
                    )
                    Content(
                        dates = uiState.dates,
                        onDateClickListener = {}
                    )
                }
            }
        }
    }
}

@Composable
fun Header(
    yearMonth: YearMonth,
    onPreviousMonthButtonClicked: (YearMonth) -> Unit,
    onNextMonthButtonClicked: (YearMonth) -> Unit,
) {
    Row {
        IconButton(onClick = {
            onPreviousMonthButtonClicked.invoke(yearMonth.minusMonths(1))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back"
            )
        }
        Text(
            text = yearMonth.getDisplayName(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        IconButton(onClick = {
            onNextMonthButtonClicked.invoke(yearMonth.plusMonths(1))
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Next"
            )
        }
    }
}

@Composable
fun DayItem(day: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            text = day,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}

@Composable
fun Content(
    dates: List<CalendarUiState.Date>,
    onDateClickListener: (CalendarUiState.Date) -> Unit,
) {
    Column {
        var index = 0
        repeat(6) {
            if (index >= dates.size) return@repeat
            Row {
                repeat(7) {
                    val item = if (index < dates.size) dates[index] else CalendarUiState.Date.Empty
                    ContentItem(
                        date = item,
                        onClickListener = onDateClickListener,
                        modifier = Modifier.weight(1f)
                    )
                    index++
                }
            }
        }
    }
}

@Composable
fun ContentItem(
    date: CalendarUiState.Date,
    onClickListener: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (date.isSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                // disable for dates after today
                enabled = date.localDate.isBefore(LocalDate.now().plusDays(1))
            ) {
                onClickListener(date)
            }
    ) {
        if (date.trained) {
            Icon(
                painter = painterResource(id = R.drawable.mushin),
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center),
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
            )
        }
        Text(
            text = date.dayOfMonth,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        )
    }
}