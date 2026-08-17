package com.example.semfour.notification

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.semfour.MainActivity
import com.example.semfour.R
import com.example.semfour.SemFourApplication

/**
 * Receptor de difusión que se activa 30 minutos después de iniciada una clase
 * para mostrar la notificación de registro de asistencia.
 */
class AttendanceNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val scheduleId = intent.getStringExtra(AttendanceScheduler.EXTRA_SCHEDULE_ID) ?: return
        val subjectName = intent.getStringExtra(AttendanceScheduler.EXTRA_SUBJECT_NAME) ?: "tu clase"
        val room = intent.getStringExtra(AttendanceScheduler.EXTRA_ROOM) ?: "tu sala"
        val startTime = intent.getStringExtra(AttendanceScheduler.EXTRA_START_TIME) ?: "10:00"
        val dayOfWeek = intent.getIntExtra(AttendanceScheduler.EXTRA_DAY_OF_WEEK, 1)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent al tocar la notificación (abre la app)
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            scheduleId.hashCode(),
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, SemFourApplication.CHANNEL_ATTENDANCE)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("📋 Registro de Asistencia: $subjectName")
            .setContentText("¡Han pasado 30 min de clase en $room! Recuerda registrar tu asistencia en el portal.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "¡Han pasado 30 minutos desde el inicio de la clase ($startTime hrs en $room)!\n\nRecuerda ingresar al portal o AVA para registrar tu asistencia a tiempo."
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .build()

        notificationManager.notify(scheduleId.hashCode(), notification)

        // Reprogramar automáticamente para la siguiente semana (7 días después)
        rescheduleForNextWeek(context, intent, scheduleId, dayOfWeek, startTime)
    }

    private fun rescheduleForNextWeek(
        context: Context,
        originalIntent: Intent,
        scheduleId: String,
        dayOfWeek: Int,
        startTime: String
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextTriggerMs = AttendanceScheduler.calculateNextTriggerTime(dayOfWeek, startTime)

        val newIntent = Intent(context, AttendanceNotificationReceiver::class.java).apply {
            putExtras(originalIntent)
        }

        val requestCode = scheduleId.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            newIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerMs,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    nextTriggerMs,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                nextTriggerMs,
                pendingIntent
            )
        }
    }
}
