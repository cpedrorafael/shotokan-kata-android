package com.guayaba.shotokankata.ui.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.guayaba.shotokankata.ui.common.AppTopBar
import com.guayaba.shotokankata.utils.toLocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SessionDayView(
    viewModel: SessionViewModel,
    dateLong: Long,
    onBackPressed: () -> Unit
) {
    val date = dateLong.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")
    val formattedDate = date.format(formatter)
    Scaffold(
        topBar = {
            AppTopBar(title = "Sessions", onBackArrowPressed = onBackPressed)
        }
    ) { paddingValues ->
        Column(
            Modifier.padding(paddingValues)
        ) {
            Text(
                text = formattedDate,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}