package com.example.semfour.data.repository

import android.util.Log
import com.example.semfour.data.local.dao.*
import com.example.semfour.data.local.entity.*
import com.example.semfour.data.remote.DriveResult
import com.example.semfour.data.remote.DriveService
import com.example.semfour.data.remote.GoogleAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositorio de sincronización con Google Drive.
 */
@Singleton
class SyncRepository @Inject constructor(
    private val driveService: DriveService,
    private val authService: GoogleAuthService,
    private val subjectDao: SubjectDao,
    private val topicDao: TopicDao,
    private val evaluationDao: EvaluationDao,
    private val habitDayDao: HabitDayDao
) {
    private val json = Json { ignoreUnknownKeys = true; prettyPrint = false }
    private val tag = "SyncRepository"

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    fun reportError(message: String) {
        _syncState.value = SyncState.Error(message)
    }

    /**
     * Ejecuta un ciclo completo de sincronización bidireccional con Google Drive.
     *
     * @param accessToken Token de acceso OAuth 2.0 válido con scope drive.appdata
     */
    suspend fun sync(accessToken: String) {
        if (_syncState.value is SyncState.Syncing) {
            Log.d(tag, "Sync ya en progreso, omitiendo")
            return
        }
        _syncState.value = SyncState.Syncing

        try {
            // 1. Serializar estado local
            val localBackup = exportLocalToBackup()

            // 2. Intentar descargar backup de Drive
            when (val downloadResult = driveService.downloadBackup(accessToken)) {
                is DriveResult.Success -> {
                    val remoteBackup = try {
                        json.decodeFromString<AppBackup>(downloadResult.data)
                    } catch (e: Exception) {
                        Log.w(tag, "No se pudo deserializar backup remoto, subiendo local", e)
                        null
                    }

                    if (remoteBackup != null && remoteBackup.exportedAt > localBackup.exportedAt) {
                        Log.d(tag, "Drive es más nuevo, haciendo merge")
                        mergeRemoteIntoLocal(localBackup, remoteBackup)
                    }
                }
                is DriveResult.NotFound -> {
                    Log.d(tag, "No hay backup en Drive, subiendo por primera vez")
                }
                is DriveResult.Error -> {
                    Log.e(tag, "Error descargando: ${downloadResult.message}")
                }
            }

            // 3. Subir estado actualizado (ya sea local o post-merge)
            val finalBackup = exportLocalToBackup()
            val uploadJson = json.encodeToString(finalBackup)

            when (val uploadResult = driveService.uploadBackup(accessToken, uploadJson)) {
                is DriveResult.Success -> {
                    Log.d(tag, "Sync completado. Drive file ID: ${uploadResult.data}")
                    _syncState.value = SyncState.Success(System.currentTimeMillis())
                }
                is DriveResult.Error -> {
                    authService.invalidateCachedDriveToken(accessToken)
                    _syncState.value = SyncState.Error("Upload falló: ${uploadResult.message}")
                }
                else -> {}
            }
        } catch (e: Exception) {
            Log.e(tag, "Sync exception", e)
            _syncState.value = SyncState.Error(e.message ?: "Error desconocido")
        }
    }

    // ── Serialización ────────────────────────────────────────────────────────

    private suspend fun exportLocalToBackup(): AppBackup {
        return AppBackup(
            exportedAt = System.currentTimeMillis(),
            subjects = subjectDao.getAllSubjects().first(),
            topics = topicDao.getAllTopics().first(),
            evaluations = evaluationDao.getUpcomingEvaluations().first(),
            habitDays = habitDayDao.getLast90Days().first()
        )
    }

    /**
     * Merge de datos remotos en la DB local, usando [updatedAt] como árbitro.
     * La entidad con mayor [updatedAt] gana (última escritura gana).
     */
    private suspend fun mergeRemoteIntoLocal(local: AppBackup, remote: AppBackup) {
        remote.subjects.forEach { remoteSubject ->
            val localSubject = local.subjects.find { it.id == remoteSubject.id }
            if (localSubject == null || remoteSubject.updatedAt > localSubject.updatedAt) {
                subjectDao.insertSubject(remoteSubject)
            }
        }

        remote.topics.forEach { remoteTopic ->
            val localTopic = local.topics.find { it.id == remoteTopic.id }
            if (localTopic == null || remoteTopic.updatedAt > localTopic.updatedAt) {
                topicDao.insertTopic(remoteTopic)
            }
        }

        remote.evaluations.forEach { remoteEval ->
            val localEval = local.evaluations.find { it.id == remoteEval.id }
            if (localEval == null || remoteEval.updatedAt > localEval.updatedAt) {
                evaluationDao.insertEvaluation(remoteEval)
            }
        }

        remote.habitDays.forEach { remoteDay ->
            val localDay = local.habitDays.find { it.dateKey == remoteDay.dateKey }
            if (localDay == null) {
                habitDayDao.upsertHabitDay(remoteDay)
            } else if (remoteDay.totalMinutes > localDay.totalMinutes) {
                habitDayDao.upsertHabitDay(
                    localDay.copy(
                        totalMinutes = remoteDay.totalMinutes,
                        sessionsCount = maxOf(localDay.sessionsCount, remoteDay.sessionsCount),
                        streakDay = maxOf(localDay.streakDay, remoteDay.streakDay)
                    )
                )
            }
        }
    }
}

// ── Modelos de backup ────────────────────────────────────────────────────────

@Serializable
data class AppBackup(
    val version: Int = 2,
    val exportedAt: Long,
    val packageName: String = "com.example.semfour",
    val subjects: List<SubjectEntity>,
    val topics: List<TopicEntity>,
    val evaluations: List<EvaluationEntity>,
    val habitDays: List<HabitDayEntity>
)

// ── Estados de sincronización ────────────────────────────────────────────────

sealed class SyncState {
    data object Idle : SyncState()
    data object Syncing : SyncState()
    data class Success(val timestamp: Long) : SyncState()
    data class Error(val message: String) : SyncState()
}
