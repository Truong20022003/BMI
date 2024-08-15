package com.example.bmi

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import com.google.android.material.color.DynamicColors
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<Application>.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
