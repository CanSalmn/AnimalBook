package com.cansalman.artbookfragment.Alert;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import io.reactivex.rxjava3.core.Scheduler;

public class AlertWorkManager extends Worker {




    public AlertWorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}
