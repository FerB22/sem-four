package com.example.semfour

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.semfour.data.local.DatabaseSeeder
import com.example.semfour.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Application class principal de SemFour.
 *
 * Responsabilidades:
 * 1. Inicializar Hilt (inyección de dependencias)
 * 2. Poblar la base de datos en el primer lanzamiento (seed data)
 * 3. Configurar WorkManager con HiltWorkerFactory
 * 4. Programar sincronización periódica con Google Drive
 */
@HiltAndroidApp
class SemFourApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var databaseSeeder: DatabaseSeeder
    @Inject lateinit var attendanceScheduler: com.example.semfour.notification.AttendanceScheduler

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // 1. Crear canales de notificación
        createNotificationChannels()

        // 2. Inicializar DB con seed data y programar recordatorios de asistencia
        applicationScope.launch {
            databaseSeeder.seedIfEmpty()
            attendanceScheduler.scheduleAllFromDatabase()
        }

        // 3. Programar sincronización automática con Drive (requiere red)
        SyncWorker.enqueuePeriodic(this)
    }

    private fun createNotificationChannels() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(android.app.NotificationManager::class.java)

            // Canal 1: Asistencia
            val attendanceChannel = android.app.NotificationChannel(
                CHANNEL_ATTENDANCE,
                "Recordatorios de Asistencia",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones automáticas 30 minutos después de iniciar cada clase para registrar asistencia"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager?.createNotificationChannel(attendanceChannel)

            // Canal 2: Temporizador Pomodoro en curso (Baja prioridad para no emitir pitidos continuos)
            val pomodoroTimerChannel = android.app.NotificationChannel(
                CHANNEL_POMODORO_TIMER,
                "Temporizador Pomodoro Activo",
                android.app.NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Progreso del temporizador de estudio en segundo plano"
                setShowBadge(false)
            }
            notificationManager?.createNotificationChannel(pomodoroTimerChannel)

            // Canal 3: Alarma de Fin de Pomodoro (Alta prioridad con sonido y vibración)
            val pomodoroAlarmChannel = android.app.NotificationChannel(
                CHANNEL_POMODORO_ALARM,
                "Alarma de Pomodoro Completado",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alarma sonora y vibración al terminar un bloque de estudio o descanso"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager?.createNotificationChannel(pomodoroAlarmChannel)
        }
    }

    companion object {
        const val CHANNEL_ATTENDANCE = "attendance_channel"
        const val CHANNEL_POMODORO_TIMER = "pomodoro_timer_channel"
        const val CHANNEL_POMODORO_ALARM = "pomodoro_alarm_channel"
    }

    // Hilt + WorkManager
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
