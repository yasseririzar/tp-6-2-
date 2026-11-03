package com.example.backupapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.tp62.NotificationHelper
import com.example.tp62.R
import com.example.tp62.SaveDataWorker

class MainActivity : AppCompatActivity() {

    private lateinit var btnSave: Button

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(
                    this,
                    "Notifications autorisées",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Find the button from XML
        btnSave = findViewById(R.id.btnSave)

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)

        // Request notification permission
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        // Button click
        btnSave.setOnClickListener {
            startBackup()
        }
    }

    private fun startBackup() {

        Log.d("MainActivity", "Backup request sent")

        // Create one-time work request
        val saveRequest =
            OneTimeWorkRequestBuilder<SaveDataWorker>()
                .build()

        // Observe task status
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(saveRequest.id)
            .observe(this) { workInfo ->

                when (workInfo?.state) {

                    WorkInfo.State.ENQUEUED ->
                        Log.d("MainActivity", "Task ENQUEUED")

                    WorkInfo.State.RUNNING ->
                        Log.d("MainActivity", "Task RUNNING")

                    WorkInfo.State.SUCCEEDED ->
                        Log.d("MainActivity", "Task SUCCEEDED")

                    WorkInfo.State.FAILED ->
                        Log.e("MainActivity", "Task FAILED")

                    WorkInfo.State.CANCELLED ->
                        Log.d("MainActivity", "Task CANCELLED")

                    else -> {}
                }
            }

        // Execute unique work
        WorkManager.getInstance(this).enqueueUniqueWork(
            "SAVE_DATA_WORK",
            ExistingWorkPolicy.KEEP,
            saveRequest
        )
    }
}