package com.example.bmi.Notifycation
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.content.BroadcastReceiver
import android.widget.RemoteViews
import com.example.bmi.R
import com.example.bmi.Ui.BmiCalculator.BmiCalculatorActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BmiReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val channelId = "bmi_reminder_channel"
        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "BMI Reminder"
            val channelDescription = "Channel for BMI reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val currentTime = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime.time)

        val remoteViews = RemoteViews(context.packageName, R.layout.custom_popup_notifycation).apply {
            setTextViewText(R.id.tvTitle, "It’s time to track your progress")
            setTextViewText(R.id.tvMessage, "Don’t forget to log your weight today to stay on top of your health goals")

            setTextViewText(R.id.tvTime, formattedTime)
        }

        // Tạo intent để mở ứng dụng khi người dùng nhấp vào thông báo
        val contentIntent = Intent(context, BmiCalculatorActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        // Tạo thông báo với RemoteViews
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_popup)
            .setCustomContentView(remoteViews)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Hiển thị thông báo
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notification)
        }
    }
}

//class BmiReminderReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        // Hiển thị thông báo
//        // Bạn có thể thay đổi cách hiển thị thông báo tại đây
//        Log.d("BmiReminder", "It's time to track your progress!")
//
//        // Đặt lại báo thức để lặp lại sau 2 phút
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val alarmIntent = Intent(context, BmiReminderReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            context, 0, alarmIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val calendar = Calendar.getInstance()
//        calendar.add(Calendar.MINUTE, 2)  // Lặp lại sau 2 phút
//
//        try {
//            alarmManager.setExactAndAllowWhileIdle(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                pendingIntent
//            )
//        } catch (e: SecurityException) {
//            // Xử lý lỗi
//            Log.e("BmiReminderReceiver", "Error setting alarm", e)
//        }
//    }
//}

