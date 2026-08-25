package com.example.semfour.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.semfour.ui.viewmodel.SessionType
import com.example.semfour.ui.viewmodel.TimerState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PomodoroSessionData(
    val topicId: String = "",
    val topicName: String = "",
    val sessionType: SessionType = SessionType.POMODORO,
    val timerState: TimerState = TimerState.Idle,
    val secondsRemaining: Int = 0,
    val secondsElapsed: Int = 0,
    val totalSeconds: Int = 0,
    val startTimeMs: Long = 0L,
    val targetEndTimeMs: Long = 0L,
    val pausedElapsedSeconds: Int = 0
)

object PomodoroManager {

    private val _sessionData = MutableStateFlow(PomodoroSessionData())
    val sessionData: StateFlow<PomodoroSessionData> = _sessionData.asStateFlow()

    private var tickJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun initialize(topicId: String, topicName: String, sessionType: SessionType) {
        val current = _sessionData.value
        // Si ya hay una sesión corriendo para el mismo tema, no reiniciar
        if (current.topicId == topicId && current.timerState == TimerState.Running) {
            return
        }

        tickJob?.cancel()
        val duration = sessionType.durationSeconds
        _sessionData.value = PomodoroSessionData(
            topicId = topicId,
            topicName = topicName,
            sessionType = sessionType,
            timerState = TimerState.Idle,
            secondsRemaining = duration,
            secondsElapsed = 0,
            totalSeconds = duration
        )
    }

    fun start(context: Context) {
        val current = _sessionData.value
        if (current.timerState == TimerState.Running) return

        val now = System.currentTimeMillis()
        val remaining = if (current.secondsRemaining > 0) current.secondsRemaining else current.sessionType.durationSeconds
        val targetEnd = now + (remaining * 1000L)

        _sessionData.value = current.copy(
            timerState = TimerState.Running,
            startTimeMs = now,
            targetEndTimeMs = targetEnd,
            secondsRemaining = remaining
        )

        // Iniciar servicio en primer plano para mantener ejecución con pantalla apagada
        startForegroundService(context)

        // Programar alarma exacta en caso de Doze profundo
        if (current.sessionType != SessionType.FREE && remaining > 0) {
            scheduleExactAlarm(context, targetEnd, current.topicName)
        }

        startTicker(context)
    }

    fun pause(context: Context) {
        val current = _sessionData.value
        if (current.timerState != TimerState.Running) return

        tickJob?.cancel()
        cancelExactAlarm(context)

        val updatedElapsed = calculateCurrentElapsed()
        val updatedRemaining = calculateCurrentRemaining()

        _sessionData.value = current.copy(
            timerState = TimerState.Paused,
            pausedElapsedSeconds = updatedElapsed,
            secondsElapsed = updatedElapsed,
            secondsRemaining = updatedRemaining
        )

        stopForegroundService(context)
    }

    fun stop(context: Context) {
        tickJob?.cancel()
        cancelExactAlarm(context)
        stopForegroundService(context)

        val current = _sessionData.value
        val total = current.sessionType.durationSeconds
        _sessionData.value = current.copy(
            timerState = TimerState.Idle,
            secondsElapsed = 0,
            secondsRemaining = total,
            pausedElapsedSeconds = 0
        )
    }

    fun changeSessionType(context: Context, type: SessionType) {
        stop(context)
        val current = _sessionData.value
        _sessionData.value = current.copy(
            sessionType = type,
            totalSeconds = type.durationSeconds,
            secondsRemaining = type.durationSeconds,
            secondsElapsed = 0
        )
    }

    fun onAlarmTriggered(context: Context) {
        tickJob?.cancel()
        stopForegroundService(context)

        val current = _sessionData.value
        val total = current.totalSeconds
        _sessionData.value = current.copy(
            timerState = TimerState.Completed,
            secondsRemaining = 0,
            secondsElapsed = if (total > 0) total else current.secondsElapsed
        )
    }

    private fun startTicker(context: Context) {
        tickJob?.cancel()
        tickJob = scope.launch {
            while (isActive) {
                delay(1000L)
                val current = _sessionData.value
                if (current.timerState != TimerState.Running) break

                val elapsed = calculateCurrentElapsed()
                val remaining = calculateCurrentRemaining()

                if (current.sessionType != SessionType.FREE && remaining <= 0) {
                    // Temporizador terminado
                    onAlarmTriggered(context)
                    PomodoroAlarmReceiver.triggerAlarm(context, current.topicName)
                    break
                } else {
                    _sessionData.value = current.copy(
                        secondsElapsed = elapsed,
                        secondsRemaining = remaining
                    )
                }
            }
        }
    }

    private fun calculateCurrentElapsed(): Int {
        val current = _sessionData.value
        if (current.timerState != TimerState.Running) return current.secondsElapsed
        val diffSecs = ((System.currentTimeMillis() - current.startTimeMs) / 1000L).toInt()
        return current.pausedElapsedSeconds + maxOf(0, diffSecs)
    }

    private fun calculateCurrentRemaining(): Int {
        val current = _sessionData.value
        if (current.sessionType == SessionType.FREE) return 0
        if (current.timerState != TimerState.Running) return current.secondsRemaining
        val diffSecs = ((current.targetEndTimeMs - System.currentTimeMillis()) / 1000L).toInt()
        return maxOf(0, diffSecs)
    }

    private fun startForegroundService(context: Context) {
        try {
            val intent = Intent(context, PomodoroTimerService::class.java).apply {
                action = PomodoroTimerService.ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (_: Exception) {}
    }

    private fun stopForegroundService(context: Context) {
        try {
            val intent = Intent(context, PomodoroTimerService::class.java).apply {
                action = PomodoroTimerService.ACTION_STOP
            }
            context.startService(intent)
        } catch (_: Exception) {}
    }

    private fun scheduleExactAlarm(context: Context, triggerAtMs: Long, topicName: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PomodoroAlarmReceiver::class.java).apply {
            putExtra(PomodoroAlarmReceiver.EXTRA_TOPIC_NAME, topicName)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMs,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMs,
                    pendingIntent
                )
            }
        } catch (_: Exception) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerAtMs,
                pendingIntent
            )
        }
    }

    private fun cancelExactAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, PomodoroAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private const val ALARM_REQUEST_CODE = 9942
}
