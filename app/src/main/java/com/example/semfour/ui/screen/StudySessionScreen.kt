package com.example.semfour.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semfour.ui.viewmodel.SessionType
import com.example.semfour.ui.viewmodel.StudySessionViewModel
import com.example.semfour.ui.viewmodel.TimerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionScreen(
    viewModel: StudySessionViewModel = hiltViewModel(),
    onSessionCompleted: () -> Unit,
    onBack: () -> Unit
) {
    val topic by viewModel.topic.collectAsStateWithLifecycle()
    val timerState by viewModel.timerState.collectAsStateWithLifecycle()
    val secondsRemaining by viewModel.secondsRemaining.collectAsStateWithLifecycle()
    val secondsElapsed by viewModel.secondsElapsed.collectAsStateWithLifecycle()
    val sessionType by viewModel.sessionType.collectAsStateWithLifecycle()

    var showRatingDialog by remember { mutableStateOf(false) }

    // Disparar diálogo de calificación cuando el temporizador completa
    LaunchedEffect(timerState) {
        if (timerState is TimerState.Completed) {
            showRatingDialog = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic?.nombre ?: "Sesión de Estudio", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    // Selector de tipo de sesión
                    SessionType.values().forEach { type ->
                        FilterChip(
                            selected = sessionType == type,
                            onClick = { viewModel.changeSessionType(type) },
                            label = { Text(type.emoji) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Info del tema
            topic?.let {
                Text(
                    sessionType.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
            }

            // Temporizador circular
            CircularTimer(
                secondsRemaining = if (sessionType == SessionType.FREE) 0 else secondsRemaining,
                totalSeconds = sessionType.durationSeconds,
                secondsElapsed = secondsElapsed,
                sessionType = sessionType,
                isRunning = timerState == TimerState.Running,
                size = 260.dp
            )

            // Controles
            TimerControls(
                timerState = timerState,
                onStart = { viewModel.startTimer() },
                onPause = { viewModel.pauseTimer() },
                onStop = { viewModel.stopTimer() },
                onFinish = { showRatingDialog = true }
            )
        }
    }

    // Diálogo de calificación SM-2
    if (showRatingDialog) {
        SM2RatingDialog(
            topicName = topic?.nombre ?: "",
            minutesStudied = secondsElapsed / 60,
            onRating = { rating ->
                viewModel.completarSesion(rating)
                showRatingDialog = false
                onSessionCompleted()
            },
            onDismiss = { showRatingDialog = false }
        )
    }
}

@Composable
private fun CircularTimer(
    secondsRemaining: Int,
    totalSeconds: Int,
    secondsElapsed: Int,
    sessionType: SessionType,
    isRunning: Boolean,
    size: Dp
) {
    val progress = if (sessionType == SessionType.FREE || totalSeconds == 0) {
        // Para modo libre, mostrar spinner de actividad
        if (isRunning) {
            val rotation by rememberInfiniteTransition(label = "spinner")
                .animateFloat(
                    0f, 360f,
                    animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
                    label = "rot"
                )
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(size - 20.dp).rotate(rotation),
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round
                )
                TimerText(
                    label = formatTime(secondsElapsed),
                    sublabel = "transcurrido",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            return
        }
        1f
    } else {
        secondsRemaining.toFloat() / totalSeconds.toFloat()
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val progressColor = when {
        progress > 0.6f -> MaterialTheme.colorScheme.primary
        progress > 0.3f -> Color(0xFFFFA726)
        else -> Color(0xFFFF6B6B)
    }

    Box(modifier = Modifier.size(size), contentAlignment = Alignment.Center) {
        // Background track
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(size),
            color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round
        )
        // Animated progress
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(size),
            color = progressColor,
            strokeWidth = 10.dp,
            strokeCap = StrokeCap.Round
        )
        TimerText(
            label = if (sessionType == SessionType.FREE) formatTime(secondsElapsed) else formatTime(secondsRemaining),
            sublabel = if (sessionType == SessionType.FREE) "transcurrido" else "restante",
            color = progressColor
        )
    }
}

@Composable
private fun TimerText(label: String, sublabel: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label,
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            ),
            color = color
        )
        Text(sublabel, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TimerControls(
    timerState: TimerState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    onFinish: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Stop
        if (timerState != TimerState.Idle) {
            OutlinedIconButton(
                onClick = onStop,
                modifier = Modifier.size(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(Icons.Default.Stop, "Detener", modifier = Modifier.size(28.dp))
            }
        }

        // Play / Pause — botón principal
        FilledIconButton(
            onClick = if (timerState == TimerState.Running) onPause else onStart,
            modifier = Modifier.size(80.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                if (timerState == TimerState.Running) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (timerState == TimerState.Running) "Pausar" else "Iniciar",
                modifier = Modifier.size(40.dp)
            )
        }

        // Terminar manualmente
        if (timerState == TimerState.Running || timerState == TimerState.Paused) {
            OutlinedIconButton(
                onClick = onFinish,
                modifier = Modifier.size(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(0.5f))
            ) {
                Icon(Icons.Default.Check, "Terminar", tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
private fun SM2RatingDialog(
    topicName: String,
    minutesStudied: Int,
    onRating: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("¡Sesión completada! 🎉", fontWeight = FontWeight.Bold)
                Text(
                    "$minutesStudied min estudiados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column {
                Text(
                    "¿Con cuánta claridad recuerdas «$topicName»?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(16.dp))

                val ratings = listOf(
                    Triple(0, "😵", "No recuerdo nada"),
                    Triple(1, "😕", "Muy difícil"),
                    Triple(2, "😐", "Difícil"),
                    Triple(3, "🙂", "Con esfuerzo"),
                    Triple(4, "😊", "Casi perfecto"),
                    Triple(5, "🤩", "¡Perfecto!")
                )

                ratings.forEach { (rating, emoji, label) ->
                    OutlinedButton(
                        onClick = { onRating(rating) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = when (rating) {
                                0, 1 -> Color(0xFFFF6B6B)
                                2, 3 -> Color(0xFFFFA726)
                                else -> Color(0xFF4CAF50)
                            }
                        )
                    ) {
                        Text("$emoji  $label ($rating/5)", textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun formatTime(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
