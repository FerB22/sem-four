package com.example.semfour.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.semfour.data.local.entity.QuizQuestionEntity
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.data.local.entity.TopicEntity
import com.example.semfour.data.repository.StudyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUiState(
    val topic: TopicEntity? = null,
    val subject: SubjectEntity? = null,
    val questions: List<QuizQuestionEntity> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswered: Boolean = false,
    val correctAnswersCount: Int = 0,
    val isCompleted: Boolean = false,
    val calculatedSM2Rating: Int = 3,
    val startTimeMs: Long = System.currentTimeMillis()
)

@HiltViewModel
class QuizStudyViewModel @Inject constructor(
    private val studyRepository: StudyRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val topicId: String = savedStateHandle["topicId"] ?: ""

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuizData()
    }

    fun loadQuizData() {
        viewModelScope.launch {
            val topic = studyRepository.getTopicById(topicId)
            val subject = topic?.let { studyRepository.getSubjectById(it.subjectId) }
            val questions = studyRepository.getQuestionsForTopicSync(topicId)

            // Si hay preguntas, tomar hasta 3 aleatorias para un repaso rápido y fresco
            val selectedQuestions = if (questions.size > 3) {
                questions.shuffled().take(3)
            } else {
                questions
            }

            _uiState.value = QuizUiState(
                topic = topic,
                subject = subject,
                questions = selectedQuestions,
                currentIndex = 0,
                selectedOptionIndex = null,
                isAnswered = false,
                correctAnswersCount = 0,
                isCompleted = false,
                startTimeMs = System.currentTimeMillis()
            )
        }
    }

    fun selectOption(optionIndex: Int) {
        val state = _uiState.value
        if (state.isAnswered || state.questions.isEmpty() || state.isCompleted) return

        val currentQuestion = state.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = optionIndex == currentQuestion.correctOptionIndex
        val newCorrectCount = if (isCorrect) state.correctAnswersCount + 1 else state.correctAnswersCount

        _uiState.value = state.copy(
            selectedOptionIndex = optionIndex,
            isAnswered = true,
            correctAnswersCount = newCorrectCount
        )

        // Actualizar estadísticas de la pregunta en segundo plano
        viewModelScope.launch {
            studyRepository.updateQuestion(
                currentQuestion.copy(
                    timesAnswered = currentQuestion.timesAnswered + 1,
                    timesCorrect = if (isCorrect) currentQuestion.timesCorrect + 1 else currentQuestion.timesCorrect,
                    lastAnsweredAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.currentIndex + 1

        if (nextIndex < state.questions.size) {
            _uiState.value = state.copy(
                currentIndex = nextIndex,
                selectedOptionIndex = null,
                isAnswered = false
            )
        } else {
            // Fin del Quiz: calcular SM-2 y persistir sesión
            completeQuiz()
        }
    }

    private fun completeQuiz() {
        val state = _uiState.value
        val total = maxOf(1, state.questions.size)
        val correct = state.correctAnswersCount
        val ratio = correct.toFloat() / total.toFloat()

        val sm2Rating = when {
            ratio >= 1.0f -> 5 // Perfecto -> Fácil
            ratio >= 0.66f -> 4 // Muy bien -> Buena retención
            ratio >= 0.33f -> 3 // Regular -> Repaso estándar
            else -> 1           // Bajo -> Repasar pronto
        }

        val elapsedMinutes = maxOf(1, ((System.currentTimeMillis() - state.startTimeMs) / 60000L).toInt())

        _uiState.value = state.copy(
            isCompleted = true,
            calculatedSM2Rating = sm2Rating
        )

        viewModelScope.launch {
            state.topic?.let { topic ->
                studyRepository.registrarSesion(
                    topicId = topic.id,
                    subjectId = topic.subjectId,
                    durationMinutes = elapsedMinutes,
                    calificacionSM2 = sm2Rating,
                    sessionType = "QUIZ_RECALL"
                )
            }
        }
    }
}
