package com.example.moviesapp.ui.permission

import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.moviesapp.core.PermissionUtils


@Composable
fun PermissionUI(
    context: Context,
    permission: String,
    permissionAction: (PermissionAction) -> Unit
) {
    val permissionGranted =
        PermissionUtils.checkIfPermissionGranted(
            context,
            permission
        )

    if (permissionGranted) {
        permissionAction(PermissionAction.OnPermissionGranted)
        return
    }


    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission Accepted
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            // Permission Denied
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }

    SideEffect {
        launcher.launch(permission)
    }
}