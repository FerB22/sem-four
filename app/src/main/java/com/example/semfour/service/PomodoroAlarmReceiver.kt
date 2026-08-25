package com.example.semfour.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.semfour.MainActivity
import com.example.semfour.R
import com.example.semfour.SemFourApplication

class PomodoroAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val topicName = intent.getStringExtra(EXTRA_TOPIC_NAME) ?: "tu sesión"
        PomodoroManager.onAlarmTriggered(context)
        triggerAlarm(context, topicName)
    }

    companion object {
        const val EXTRA_TOPIC_NAME = "extra_topic_name"
        const val ALARM_NOTIFICATION_ID = 8842

        fun triggerAlarm(context: Context, topicName: String) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Intent para abrir la app al tocar la alarma
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val openAppPendingIntent = PendingIntent.getActivity(
                context,
                ALARM_NOTIFICATION_ID,
                openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Sonido de Alarma
            val alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notification = NotificationCompat.Builder(context, SemFourApplication.CHANNEL_POMODORO_ALARM)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("🔔 ¡Tiempo Cumplido!")
                .setContentText("Has completado tu bloque de estudio de $topicName. ¡Excelente trabajo!")
                .setStyle(
                    NotificationCompat.BigTextStyle().bigText(
                        "🎉 ¡Tiempo de estudio finalizado!\n\nCompletaste tu sesión de $topicName. Abre la app para calificar tu nivel de retención con el algoritmo SM-2."
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setSound(alarmSoundUri)
                .setVibrate(longArrayOf(0, 600, 300, 600, 300, 600))
                .setAutoCancel(true)
                .setContentIntent(openAppPendingIntent)
                .build()

            notificationManager.notify(ALARM_NOTIFICATION_ID, notification)

            // Reproducir sonido de alarma directamente
            try {
                val ringtone = RingtoneManager.getRingtone(context.applicationContext, alarmSoundUri)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ringtone.audioAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                }
                ringtone.play()
            } catch (_: Exception) {}

            // Vibración háptica del dispositivo
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                    val vibrator = vibratorManager?.defaultVibrator
                    val pattern = longArrayOf(0, 600, 300, 600, 300, 600)
                    val effect = VibrationEffect.createWaveform(pattern, -1)
                    vibrator?.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    val pattern = longArrayOf(0, 600, 300, 600, 300, 600)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator?.vibrate(pattern, -1)
                    }
                }
            } catch (_: Exception) {}
        }
    }
}
