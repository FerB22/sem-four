package com.example.semfour.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semfour.data.local.entity.TopicEntity
import com.example.semfour.data.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de sesión de estudio.
 * Gestiona el temporizador Pomodoro / Micro-sesión / Cronómetro libre
 * y persiste la sesión completada con la calificación SM-2.
 */
@HiltViewModel
class StudySessionViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Recibido desde NavArgs: topicId y sessionType
    val topicId: String = savedStateHandle["topicId"] ?: ""
    val sessionTypeArg: String = savedStateHandle["sessionType"] ?: SessionType.POMODORO.name

    // ── Estado del tema ────────────────────────────────────────────────────────
    private val _topic = MutableStateFlow<TopicEntity?>(null)
    val topic: StateFlow<TopicEntity?> = _topic.asStateFlow()

    // ── Estado del temporizador ────────────────────────────────────────────────
    private val _timerState = MutableStateFlow<TimerState>(TimerState.Idle)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private val _secondsRemaining = MutableStateFlow(0)
    val secondsRemaining: StateFlow<Int> = _secondsRemaining.asStateFlow()

    private val _secondsElapsed = MutableStateFlow(0)
    val secondsElapsed: StateFlow<Int> = _secondsElapsed.asStateFlow()

    private val _sessionType = MutableStateFlow(SessionType.valueOf(sessionTypeArg))
    val sessionType: StateFlow<SessionType> = _sessionType.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            studyRepository.getTopicById(topicId)?.let {
                _topic.value = it
            }
            resetTimer()
        }
    }

    fun startTimer() {
        if (_timerState.value == TimerState.Running) return
        _timerState.value = TimerState.Running

        val type = _sessionType.value
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L)
                _secondsElapsed.value++

                if (type != SessionType.FREE) {
                    val remaining = _secondsRemaining.value - 1
                    _secondsRemaining.value = remaining
                    if (remaining <= 0) {
                        _timerState.value = TimerState.Completed
                        break
                    }
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.Paused
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = TimerState.Idle
        resetTimer()
    }

    fun resetTimer() {
        _secondsElapsed.value = 0
        _secondsRemaining.value = _sessionType.value.durationSeconds
    }

    fun changeSessionType(type: SessionType) {
        timerJob?.cancel()
        _sessionType.value = type
        _timerState.value = TimerState.Idle
        resetTimer()
    }

    /**
     * Llama al repositorio para persistir la sesión y aplicar SM-2.
     * Se llama cuando el estudiante entrega su calificación en el diálogo final.
     *
     * @param calificacion Calificación SM-2 del estudiante (0-5)
     */
    fun completarSesion(calificacion: Int) {
        val topic = _topic.value ?: return
        val minutos = (_secondsElapsed.value / 60).coerceAtLeast(1)

        viewModelScope.launch {
            studyRepository.registrarSesion(
                topicId = topicId,
                subjectId = topic.subjectId,
                durationMinutes = minutos,
                calificacionSM2 = calificacion,
                sessionType = _sessionType.value.name
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
