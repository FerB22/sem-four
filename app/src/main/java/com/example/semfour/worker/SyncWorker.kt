package com.example.semfour.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.semfour.data.remote.GoogleAuthService
import com.example.semfour.data.repository.SyncRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

/**
 * WorkManager Worker para sincronización automática con Google Drive.
 *
 * Se programa para ejecutarse:
 * 1. Al abrir la app (si hay red y usuario autenticado)
 * 2. Al cerrar la app (enqueue oneTime)
 * 3. Periódicamente cada 30 minutos mientras hay red (PeriodicWorkRequest)
 * 4. Al completar una sesión de estudio
 *
 * Constraint: solo con red disponible (NetworkType.CONNECTED)
 */
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncRepository: SyncRepository,
    private val authService: GoogleAuthService
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME_PERIODIC = "semfour_sync_periodic"
        const val WORK_NAME_IMMEDIATE = "semfour_sync_immediate"
        private const val TAG = "SyncWorker"

        /** Encola sincronización inmediata (OneTime) */
        fun enqueueImmediate(context: Context) {
            val request = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)
                .addTag(WORK_NAME_IMMEDIATE)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    WORK_NAME_IMMEDIATE,
                    ExistingWorkPolicy.REPLACE,
                    request
                )
        }

        /** Programa sincronización periódica cada 30 minutos */
        fun enqueuePeriodic(context: Context) {
            val request = PeriodicWorkRequestBuilder<SyncWorker>(30, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .addTag(WORK_NAME_PERIODIC)
                .build()

            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME_PERIODIC,
                    ExistingPeriodicWorkPolicy.KEEP,
                    request
                )
        }
    }

    override suspend fun doWork(): Result {
        Log.d(TAG, "Iniciando sync con Drive...")

        val token = authService.getDriveAccessToken()
        if (token.isNullOrBlank()) {
            Log.d(TAG, "No hay access token de Drive disponible, omitiendo sync")
            return Result.success()
        }

        return try {
            syncRepository.sync(token)
            Log.d(TAG, "Sync completado exitosamente")
            Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Sync falló: ${e.message}", e)
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }
}
