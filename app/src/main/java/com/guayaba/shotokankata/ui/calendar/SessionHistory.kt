package com.guayaba.shotokankata.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.guayaba.shotokankata.utils.toEpochMilli
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun SessionCalendarPage(
    viewModel: SessionViewModel,
    onAddPracticeSession: () -> Unit,
    onDayClicked: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Training Sessions")
        },
        floatingActionButton = {

        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Click on any day to edit",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Calendar(viewModel, onDayClicked = {
                onDayClicked(
                    it.localDate.toEpochMilli()
                )
            })
            Spacer(modifier = Modifier.height(32.dp))
            OutlinedIconButton(
                onClick = {
                    onAddPracticeSession()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = "Record new session",
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        painter = painterResource(id = R.drawable.mushin_tiger),
                        tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        contentDescription = "Mushin",
                    )
                }

            }
        }
    }
}

@Composable
private fun Calendar(
    viewModel: SessionViewModel,
    onDayClicked: (CalendarUiState.Date) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val days = DateUtil.daysOfWeek
    Card(
        shape = CardDefaults.outlinedShape,
        modifier = Modifier
            .wrapContentHeight()
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
                onDateClicked = onDayClicked
            )

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
            style = MaterialTheme.typography.bodySmall,
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
    onDateClicked: (CalendarUiState.Date) -> Unit,
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
                        onClick = onDateClicked,
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
    onClick: (CalendarUiState.Date) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (date.isSelected) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                // disable for dates after today
                enabled = date.localDate.isBefore(
                    LocalDate
                        .now()
                        .plusDays(1)
                )
            ) {
                onClick(date)
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
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        )
    }
}