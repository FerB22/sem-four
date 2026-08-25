package com.example.semfour.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.semfour.MainActivity
import com.example.semfour.R
import com.example.semfour.SemFourApplication
import com.example.semfour.ui.viewmodel.SessionType
import com.example.semfour.ui.viewmodel.TimerState
import kotlinx.coroutines.*

class PomodoroTimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var updateJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startForegroundNotification()
                observeTimer()
            }
            ACTION_PAUSE -> {
                PomodoroManager.pause(this)
            }
            ACTION_STOP -> {
                PomodoroManager.stop(this)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun startForegroundNotification() {
        val notification = buildNotification("Iniciando sesión de estudio...")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                )
            } else {
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            }
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun observeTimer() {
        updateJob?.cancel()
        updateJob = serviceScope.launch {
            PomodoroManager.sessionData.collect { data ->
                if (data.timerState == TimerState.Running) {
                    val formatted = formatTime(data.secondsRemaining, data.secondsElapsed, data.sessionType)
                    val title = "🍅 ${data.sessionType.label}: $formatted"
                    val content = if (data.topicName.isNotBlank()) "Materia: ${data.topicName}" else "Sesión de estudio activa"
                    val notification = buildNotification(title, content)
                    val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    manager.notify(NOTIFICATION_ID, notification)
                } else if (data.timerState == TimerState.Completed || data.timerState == TimerState.Idle) {
                    stopSelf()
                }
            }
        }
    }

    private fun buildNotification(title: String, content: String = ""): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pauseIntent = Intent(this, PomodoroTimerService::class.java).apply {
            action = ACTION_PAUSE
        }
        val pausePendingIntent = PendingIntent.getService(
            this,
            1,
            pauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, SemFourApplication.CHANNEL_POMODORO_TIMER)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(content)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setContentIntent(openAppPendingIntent)
            .addAction(android.R.drawable.ic_media_pause, "Pausar", pausePendingIntent)
            .build()
    }

    private fun formatTime(remaining: Int, elapsed: Int, sessionType: SessionType): String {
        val totalSecs = if (sessionType == SessionType.FREE) elapsed else remaining
        val mins = totalSecs / 60
        val secs = totalSecs % 60
        return "%02d:%02d".format(mins, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val NOTIFICATION_ID = 4421
        const val ACTION_START = "com.example.semfour.ACTION_START_POMODORO"
        const val ACTION_PAUSE = "com.example.semfour.ACTION_PAUSE_POMODORO"
        const val ACTION_STOP = "com.example.semfour.ACTION_STOP_POMODORO"
    }
}
