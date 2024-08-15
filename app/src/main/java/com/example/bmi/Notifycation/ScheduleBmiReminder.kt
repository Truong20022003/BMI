package com.example.bmi.Notifycation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import java.util.Calendar

class ScheduleBmiReminder(private val context: Context) {

    fun scheduleReminder() {
        // Kiểm tra quyền SCHEDULE_EXACT_ALARM trên Android 13 và mới hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.SCHEDULE_EXACT_ALARM
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                    return
                } else {
                    // Hành động không được hỗ trợ trên thiết bị này
                    Log.e(
                        "AlarmPermission",
                        "No Activity found to handle Intent { act=android.settings.REQUEST_SCHEDULE_EXACT_ALARM }"
                    )
                    return
                }
            }
        }

        // Đặt báo thức nếu quyền đã được cấp hoặc không yêu cầu quyền
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, BmiReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 2)  // Nhắc nhở sau 2 phút

        Log.d("ReminderTime", calendar.time.toString())

        try {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, // Lặp lại mỗi 15 phút (thay đổi nếu cần)
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Xử lý
        }
    }
}
