package com.guayaba.shotokankata.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionRequest(permissionGranted: ()-> Unit) {
    val context = LocalContext.current
    val permissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    } else {
        TODO("VERSION.SDK_INT < TIRAMISU")
        return
    }

    LaunchedEffect(key1 = Unit) {
        permissionState.launchPermissionRequest()
    }

    when {
        permissionState.status.isGranted -> {
            // Permission granted, proceed with showing notifications
            permissionGranted.invoke()
        }
        permissionState.status.shouldShowRationale || !permissionState.status.isGranted -> {
            // Show UI to explain why the app needs the permission
            Text("This app needs notification permission to show daily reminders.")
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Grant Permission")
            }
        }
        else -> {
            // Permission denied
            Text("Notification permission denied.")
        }
    }
}