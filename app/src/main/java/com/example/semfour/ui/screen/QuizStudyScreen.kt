package com.example.semfour.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semfour.data.local.entity.QuizQuestionEntity
import com.example.semfour.ui.viewmodel.QuizStudyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizStudyScreen(
    viewModel: QuizStudyViewModel = hiltViewModel(),
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val topic = uiState.topic
    val subject = uiState.subject
    val questions = uiState.questions
    val subjectColor = subject?.color?.let { parseHexColor(it) } ?: MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic?.nombre ?: "Repaso Activo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        if (subject != null) {
                            Text(
                                text = "${subject.codigo} • ${subject.nombre}",
                                style = MaterialTheme.typography.labelSmall,
                                color = subjectColor
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                questions.isEmpty() && !uiState.isCompleted -> {
                    // Estado sin preguntas
                    Box(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📝", fontSize = 48.sp)
                            Spacer(Modifier.height(16.dp))
                            Text(
                                text = "Aún no hay preguntas para este tema",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Puedes crear preguntas personalizadas desde el detalle del tema.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(Modifier.height(24.dp))
                            Button(onClick = onBack) {
                                Text("Volver")
                            }
                        }
                    }
                }

                uiState.isCompleted -> {
                    // Pantalla de Resultados Finales
                    QuizResultsView(
                        correctCount = uiState.correctAnswersCount,
                        totalCount = uiState.questions.size,
                        sm2Rating = uiState.calculatedSM2Rating,
                        onRepeat = { viewModel.loadQuizData() },
                        onFinish = onFinish
                    )
                }

                else -> {
                    // Vista interactiva de preguntas
                    val currentQuestion = questions.getOrNull(uiState.currentIndex)
                    if (currentQuestion != null) {
                        QuizQuestionContent(
                            question = currentQuestion,
                            currentIndex = uiState.currentIndex,
                            totalQuestions = questions.size,
                            selectedOptionIndex = uiState.selectedOptionIndex,
                            isAnswered = uiState.isAnswered,
                            onOptionSelected = { viewModel.selectOption(it) },
                            onNext = { viewModel.nextQuestion() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuizQuestionContent(
    question: QuizQuestionEntity,
    currentIndex: Int,
    totalQuestions: Int,
    selectedOptionIndex: Int?,
    isAnswered: Boolean,
    onOptionSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    val scrollState = rememberScrollState()
    val progress = (currentIndex + 1).toFloat() / totalQuestions.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Barra de progreso y contador
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Pregunta ${currentIndex + 1} de $totalQuestions",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64748B)
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF64748B)
            )
        }

        Spacer(Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(Modifier.height(16.dp))

        // Tarjeta Didáctica: Concepto Teórico Clave (Aprender antes de responder)
        if (question.theoryContext.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📖", fontSize = 18.sp)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Concepto Clave",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = question.theoryContext,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1E3A8A),
                        lineHeight = 21.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
        }

        // Tarjeta del Enunciado de la Pregunta
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "🎯 Pregunta",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Opciones de Respuesta A, B, C, D
        val options = listOf(
            0 to ("A" to question.optionA),
            1 to ("B" to question.optionB),
            2 to ("C" to question.optionC),
            3 to ("D" to question.optionD)
        ).filter { it.second.second.isNotBlank() }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            options.forEach { (index, optionPair) ->
                val (letter, text) = optionPair
                val isSelected = selectedOptionIndex == index
                val isCorrectOption = index == question.correctOptionIndex

                val cardBgColor = when {
                    !isAnswered -> if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.White
                    isCorrectOption -> Color(0xFFDCFCE7) // Verde claro para la correcta
                    isSelected && !isCorrectOption -> Color(0xFFFEE2E2) // Rojo claro si se equivocó
                    else -> Color(0xFFF8FAFC)
                }

                val borderColor = when {
                    !isAnswered -> if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFE2E8F0)
                    isCorrectOption -> Color(0xFF22C55E) // Borde verde
                    isSelected && !isCorrectOption -> Color(0xFFEF4444) // Borde rojo
                    else -> Color(0xFFE2E8F0)
                }

                Card(
                    onClick = { if (!isAnswered) onOptionSelected(index) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                    border = BorderStroke(1.5.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Badge con letra A, B, C, D
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        !isAnswered -> if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFFF1F5F9)
                                        isCorrectOption -> Color(0xFF22C55E)
                                        isSelected && !isCorrectOption -> Color(0xFFEF4444)
                                        else -> Color(0xFFF1F5F9)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isAnswered && (isCorrectOption || isSelected)) Color.White
                                else if (!isAnswered && isSelected) MaterialTheme.colorScheme.onPrimary
                                else Color(0xFF475569)
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected || (isAnswered && isCorrectOption)) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isAnswered && isCorrectOption) Color(0xFF14532D)
                            else if (isAnswered && isSelected && !isCorrectOption) Color(0xFF7F1D1D)
                            else Color(0xFF1E293B),
                            modifier = Modifier.weight(1f)
                        )

                        if (isAnswered) {
                            if (isCorrectOption) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Correcto",
                                    tint = Color(0xFF16A34A),
                                    modifier = Modifier.size(22.dp)
                                )
                            } else if (isSelected) {
                                Icon(
                                    Icons.Default.Cancel,
                                    contentDescription = "Incorrecto",
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Caja de Explicación Didáctica cuando ya se respondió
        AnimatedVisibility(
            visible = isAnswered && question.explanation.isNotBlank(),
            enter = fadeIn() + expandVertically()
        ) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    border = BorderStroke(1.dp, Color(0xFFBAE6FD)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("💡", fontSize = 20.sp)
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Explicación Conceptual",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = question.explanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0C4A6E),
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // Botón de avance
        if (isAnswered) {
            Button(
                onClick = onNext,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
            ) {
                Text(
                    text = if (currentIndex + 1 < totalQuestions) "Siguiente pregunta ▸" else "Ver resultados 🎉",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun QuizResultsView(
    correctCount: Int,
    totalCount: Int,
    sm2Rating: Int,
    onRepeat: () -> Unit,
    onFinish: () -> Unit
) {
    val scoreRatio = correctCount.toFloat() / maxOf(1, totalCount).toFloat()
    val isPerfect = scoreRatio >= 1.0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(if (isPerfect) Color(0xFFDCFCE7) else Color(0xFFFEF3C7)),
            contentAlignment = Alignment.Center
        ) {
            Text(if (isPerfect) "🏆" else "🎯", fontSize = 44.sp)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = if (isPerfect) "¡Excelente Dominio!" else "¡Sesión Completada!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Acertaste $correctCount de $totalCount preguntas (${(scoreRatio * 100).toInt()}%)",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF475569)
        )

        Spacer(Modifier.height(24.dp))

        // Card con el impacto en el algoritmo SM-2
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🧠 Algoritmo SM-2 Actualizado",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Calificación obtenida: ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF475569)
                    )
                    Text(
                        text = "$sm2Rating / 5 ⭐",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = when (sm2Rating) {
                        5 -> "¡Retención perfecta! Tu próximo repaso se ha espaciado."
                        4 -> "¡Buen recuerdo! El tema se mantiene afianzado."
                        3 -> "Repaso estándar. Volveremos a evaluarlo pronto."
                        else -> "Requiere refuerzo. El tema aparecerá prioritario en tu cola."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
        ) {
            Text("Volver al Inicio", fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onRepeat,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Repetir Quiz con otras preguntas", fontWeight = FontWeight.SemiBold)
        }
    }
}
