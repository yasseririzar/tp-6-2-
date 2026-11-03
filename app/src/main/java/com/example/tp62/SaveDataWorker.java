package com.example.tp62;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SaveDataWorker<WorkerParameters> extends Worker {

    private static final String TAG = "SaveDataWorker";

    public SaveDataWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {

        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "Backup started...");

        try {

            // Simulate backup for 3 seconds
            Thread.sleep(3000);

            Log.d(TAG, "Backup completed successfully!");

            // Display notification
            NotificationHelper.showNotification(
                    getApplicationContext()
            );

            return Result.success();

        } catch (InterruptedException e) {

            Log.e(TAG, "Backup failed!", e);

            Thread.currentThread().interrupt();

            return Result.failure();
        }
    }
}