package com.guayaba.shotokankata.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guayaba.shotokankata.R
import com.guayaba.shotokankata.data.KataInfo
import com.guayaba.shotokankata.data.kata_records.KataRecord
import com.guayaba.shotokankata.ui.common.AppTopBar
import com.guayaba.shotokankata.utils.toLocalDate
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun SessionDayView(
    viewModel: SessionViewModel,
    dateLong: Long,
    onBackPressed: () -> Unit,
    onLogSessions: () -> Unit,
) {
    val date = dateLong.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
    val formattedDate = date.format(formatter)
    val records = viewModel.sessionDay.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(date) {
        viewModel.getSessionsForDay(date)
    }

    Scaffold(
        topBar = {
            Column {
                AppTopBar(title = "Sessions", onBackArrowPressed = onBackPressed)
                Text(
                    text = formattedDate,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )
            }

        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { paddingValues ->
        Box(
            Modifier.fillMaxSize()
        ) {
            if (records.value.isEmpty()) {
                Text(
                    text = "Nothing to see here \uD83E\uDD21...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else {
                Column(
                    Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    records.value.forEach { record ->
                        SessionKataItem(record = record, onDelete = {
                            viewModel.deleteKataRecord(record)
                            val kata = KataInfo.findById(record.kataId)
                            scope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                val result = snackbarHostState
                                    .showSnackbar(
                                        message = "Deleted ${kata.kataName}",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long,
                                        withDismissAction = true
                                    )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.undoDeleteLastRecord()
                                    }

                                    SnackbarResult.Dismissed -> {
                                        /* Handle snackbar dismissed */
                                    }
                                }
                            }
                        })
                    }
                    Spacer(modifier = Modifier.height(100.dp))
                }

            }

            Button(
                modifier = Modifier.align(Alignment.BottomCenter),
                onClick = onLogSessions ) {
                Text(text = if (records.value.isEmpty()) "Log sessions for this day" else "Log more Katas for this day")
            }
        }
    }
}

@Composable
fun SessionKataItem(record: KataRecord, onDelete: () -> Unit) {
    val kata = KataInfo.findById(record.kataId)
    Box(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.padding(4.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
            ) {
                Icon(
                    painterResource(R.drawable.mushin_tiger),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(Modifier.width(16.dp))

            Text(kata.kataName, style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.width(8.dp))

        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.TwoTone.Delete,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }
    }
}