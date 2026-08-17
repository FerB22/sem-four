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

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Inicializar DB con seed data del semestre (solo si está vacía)
        applicationScope.launch {
            databaseSeeder.seedIfEmpty()
        }

        // Programar sincronización automática con Drive (requiere red)
        SyncWorker.enqueuePeriodic(this)
    }

    // Hilt + WorkManager
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
