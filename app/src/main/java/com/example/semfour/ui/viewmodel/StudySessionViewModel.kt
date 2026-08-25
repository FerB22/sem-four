package com.example.semfour.ui.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semfour.data.local.entity.TopicEntity
import com.example.semfour.data.repository.StudyRepository
import com.example.semfour.service.PomodoroManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de sesión de estudio.
 * Gestiona el temporizador Pomodoro / Micro-sesión / Cronómetro libre en segundo plano
 * y persiste la sesión completada con la calificación SM-2.
 */
@HiltViewModel
class StudySessionViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val topicId: String = savedStateHandle["topicId"] ?: ""
    val sessionTypeArg: String = savedStateHandle["sessionType"] ?: SessionType.POMODORO.name

    private val _topic = MutableStateFlow<TopicEntity?>(null)
    val topic: StateFlow<TopicEntity?> = _topic.asStateFlow()

    private val sessionData = PomodoroManager.sessionData

    val timerState: StateFlow<TimerState> = sessionData.map { it.timerState }
        .stateIn(viewModelScope, SharingStarted.Eagerly, TimerState.Idle)

    val secondsRemaining: StateFlow<Int> = sessionData.map { it.secondsRemaining }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val secondsElapsed: StateFlow<Int> = sessionData.map { it.secondsElapsed }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val sessionType: StateFlow<SessionType> = sessionData.map { it.sessionType }
        .stateIn(viewModelScope, SharingStarted.Eagerly, try { SessionType.valueOf(sessionTypeArg) } catch (_: Exception) { SessionType.POMODORO })

    init {
        viewModelScope.launch {
            val loadedTopic = studyRepository.getTopicById(topicId)
            _topic.value = loadedTopic
            val initialType = try { SessionType.valueOf(sessionTypeArg) } catch (_: Exception) { SessionType.POMODORO }
            PomodoroManager.initialize(
                topicId = topicId,
                topicName = loadedTopic?.nombre ?: "Sesión de Estudio",
                sessionType = initialType
            )
        }
    }

    fun startTimer() {
        PomodoroManager.start(context)
    }

    fun pauseTimer() {
        PomodoroManager.pause(context)
    }

    fun stopTimer() {
        PomodoroManager.stop(context)
    }

    fun changeSessionType(type: SessionType) {
        PomodoroManager.changeSessionType(context, type)
    }

    /**
     * Llama al repositorio para persistir la sesión y aplicar SM-2.
     *
     * @param calificacion Calificación SM-2 del estudiante (0-5)
     */
    fun completarSesion(calificacion: Int) {
        val currentTopic = _topic.value ?: return
        val currentElapsed = sessionData.value.secondsElapsed
        val minutos = (currentElapsed / 60).coerceAtLeast(1)
        val typeName = sessionData.value.sessionType.name

        PomodoroManager.stop(context)

        viewModelScope.launch {
            studyRepository.registrarSesion(
                topicId = topicId,
                subjectId = currentTopic.subjectId,
                durationMinutes = minutos,
                calificacionSM2 = calificacion,
                sessionType = typeName
            )
        }
    }
}

// ── Tipos de sesión ───────────────────────────────────────────────────────────

enum class SessionType(val durationSeconds: Int, val label: String, val emoji: String) {
    MICRO(5 * 60, "Micro-sesión", "⚡"),
    POMODORO(25 * 60, "Pomodoro", "🍅"),
    FREE(0, "Libre", "⏱️")
}

// ── Estados del temporizador ──────────────────────────────────────────────────

sealed class TimerState {
    data object Idle : TimerState()
    data object Running : TimerState()
    data object Paused : TimerState()
    data object Completed : TimerState()
}
