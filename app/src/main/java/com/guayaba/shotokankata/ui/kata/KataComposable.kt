package com.guayaba.shotokankata.ui.kata

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.guayaba.shotokankata.data.KataInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun KataView(viewModel: KataViewModel, kataInfo: KataInfo, navigateBack: (() -> Unit)?) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(kataInfo.kataName, fontSize = TextUnit(30.0F, TextUnitType.Sp))
                    Text("(${kataInfo.japaneseName})", fontSize = TextUnit(16.0F, TextUnitType.Sp))
                }
            }, navigationIcon = {
                if (navigateBack != null) {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                        )
                    }
                }
            })
        },
    ) { innerPadding ->
        var sessionCount by remember {
            mutableIntStateOf(0)
        }
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current

        LaunchedEffect(sessionCount) {
            sessionCount = viewModel.getSessionsForKata(info = kataInfo).count
        }

        Box(Modifier.padding(innerPadding)) {
            Column(Modifier.padding(16.dp)) {

                Text(
                    text = "Total sessions: $sessionCount",
                    fontSize = TextUnit(20.0F, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Practiced ${kataInfo.kataName} today?")
                    OutlinedButton(onClick = {
                        coroutineScope.launch {
                            if (
                                viewModel.updateKata(kataInfo)) {
                                sessionCount++
                            } else {
                                Toast.makeText(
                                    context,
                                    "Practice for ${kataInfo.kataName} already recorded for today",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }) {
                        Text(text = "Yes")
                    }
                }
            }
        }
    }
}