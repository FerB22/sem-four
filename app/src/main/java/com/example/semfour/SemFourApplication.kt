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
            val name = "Recordatorios de Asistencia"
            val descriptionText = "Notificaciones automáticas 30 minutos después de iniciar cada clase para registrar asistencia"
            val importance = android.app.NotificationManager.IMPORTANCE_HIGH
            val channel = android.app.NotificationChannel(CHANNEL_ATTENDANCE, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager = getSystemService(android.app.NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ATTENDANCE = "attendance_channel"
    }

    // Hilt + WorkManager
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
