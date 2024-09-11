package com.guayaba.shotokankata.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guayaba.shotokankata.core.notifications.NotificationReceiver
import com.guayaba.shotokankata.core.notifications.NotificationUtils
import com.guayaba.shotokankata.ui.theme.ShotokanKataTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: KataViewModel
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = KataViewModel()
        viewModel.initStorage(this)
        setContent {
            ShotokanKataTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Box(
                        Modifier.padding(8.dp)
                    ) {
                        NotificationPermissionRequest {
                            NotificationUtils.createNotificationChannel(this@MainActivity)
                            setDailyNotification()
                        }
                        NavHost(navController = navController, startDestination = "home") {
                            composable("home") { KataList(viewModel, navController) }
                            composable(
                                "details/{kataId}",
                                arguments = listOf(navArgument("kataId") { type = NavType.IntType })
                            ) {
                                val kataInfo = KataInfo.findById(it.arguments?.getInt("kataId")?: 1)!!
                                KataView(viewModel, kataInfo){
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                }
            }
        }

    }


    fun setDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}

