package com.example.semfour.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.data.local.entity.QuizQuestionEntity
import com.example.semfour.ui.viewmodel.DashboardViewModel
import com.example.semfour.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onSubjectClick: (String) -> Unit
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val prioritizedTopics by viewModel.prioritizedTopics.collectAsStateWithLifecycle()

    val topicsBySubject = remember(prioritizedTopics) {
        prioritizedTopics.groupBy { it.topic.subjectId }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis asignaturas", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(subjects, key = { it.id }) { subject ->
                val topicsForSubject = topicsBySubject[subject.id] ?: emptyList()
                val avgConfianza = if (topicsForSubject.isNotEmpty())
                    topicsForSubject.map { it.topic.nivelConfianza }.average() else 0.0
                val pendingCount = topicsForSubject.count { it.estaVencido || it.esNuevo }

                SubjectCard(
                    subject = subject,
                    topicCount = topicsForSubject.size,
                    pendingReviews = pendingCount,
                    avgConfianza = avgConfianza,
                    onClick = { onSubjectClick(subject.id) }
                )
            }
        }
    }
}

@Composable
private fun SubjectCard(
    subject: SubjectEntity,
    topicCount: Int,
    pendingReviews: Int,
    avgConfianza: Double,
    onClick: () -> Unit
) {
    val subjectColor = parseHexColor(subject.color)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color accent
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(56.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(subjectColor)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    subject.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    subject.codigo,
                    style = MaterialTheme.typography.bodySmall,
                    color = subjectColor,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SubjectStat("$topicCount temas", Icons.Default.Topic, MaterialTheme.colorScheme.onSurfaceVariant)
                    SubjectStat(
                        "$pendingReviews pendientes",
                        Icons.Default.AssignmentLate,
                        if (pendingReviews > 0) Color(0xFFFFA726) else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            // Confianza promedio
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "%.1f".format(avgConfianza),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = subjectColor
                )
                Text("/ 5", style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun SubjectStat(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(icon, null, tint = color, modifier = Modifier.size(14.dp))
        Text(text, style = MaterialTheme.typography.labelSmall, color = color)
    }
}

// ── SubjectDetailScreen ───────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    subjectId: String = "",
    viewModel: DashboardViewModel = hiltViewModel(),
    onTopicClick: (String) -> Unit,
    onStartSession: (topicId: String, sessionType: String) -> Unit,
    onBack: () -> Unit
) {
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val allTopics by viewModel.prioritizedTopics.collectAsStateWithLifecycle()

    val subject = remember(subjects, subjectId) {
        subjects.find { it.id == subjectId } ?: subjects.firstOrNull()
    }
    val subjectTopics = remember(allTopics, subjectId) {
        if (subjectId.isNotBlank()) allTopics.filter { it.topic.subjectId == subjectId }
        else allTopics
    }

    val subjectColor = subject?.color?.let { parseHexColor(it) } ?: MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subject?.nombre ?: "Asignatura", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Hero Card de Asignatura ──
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = subjectColor.copy(alpha = 0.12f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, subjectColor.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = subjectColor.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = subject?.codigo ?: "SEMFOUR",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = subjectColor,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "4.º Semestre",
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = subject?.nombre ?: "Asignatura",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column {
                                Text("Temas", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${subjectTopics.size}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = subjectColor)
                            }
                            Column {
                                val avg = if (subjectTopics.isNotEmpty()) subjectTopics.map { it.topic.nivelConfianza }.average() else 0.0
                                Text("Confianza", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%.1f / 5".format(avg), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = subjectColor)
                            }
                            Column {
                                val due = subjectTopics.count { it.estaVencido || it.esNuevo }
                                Text("Para repasar", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("$due", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold,
                                    color = if (due > 0) Color(0xFFFF6B6B) else MaterialTheme.colorScheme.secondary)
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "📚 Temario de la asignatura",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(subjectTopics, key = { it.topic.id }) { prioritized ->
                val topic = prioritized.topic
                Card(
                    onClick = { onTopicClick(topic.id) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(confidenceColor(topic.nivelConfianza).copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "${topic.nivelConfianza}",
                                    fontWeight = FontWeight.Bold,
                                    color = confidenceColor(topic.nivelConfianza)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = topic.nombre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (prioritized.estaVencido) "⚠️ Repaso vencido"
                                    else if (prioritized.esNuevo) "✨ Nuevo para aprender"
                                    else "Próximo repaso en ${(-prioritized.diasVencido).toInt()} días",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (prioritized.estaVencido) Color(0xFFFF6B6B)
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { onStartSession(topic.id, "MICRO") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(vertical = 6.dp)
                            ) {
                                Text("⚡ 5 min", style = MaterialTheme.typography.labelMedium)
                            }
                            Button(
                                onClick = { onStartSession(topic.id, "POMODORO") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = subjectColor),
                                contentPadding = PaddingValues(vertical = 6.dp)
                            ) {
                                Text("🍅 25 min", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── TopicDetailScreen ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicDetailScreen(
    topicId: String = "",
    viewModel: DashboardViewModel = hiltViewModel(),
    onStartQuiz: (topicId: String) -> Unit = {},
    onStartSession: (topicId: String, sessionType: String) -> Unit,
    onBack: () -> Unit
) {
    val allTopics by viewModel.prioritizedTopics.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val questionsList by viewModel.getQuestionsForTopic(topicId).collectAsStateWithLifecycle(emptyList())

    var questionToEdit by remember { mutableStateOf<QuizQuestionEntity?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog || questionToEdit != null) {
        AddEditQuestionDialog(
            topicId = topicId,
            existingQuestion = questionToEdit,
            onDismiss = {
                showAddDialog = false
                questionToEdit = null
            },
            onSave = { question ->
                viewModel.saveQuestion(question)
                showAddDialog = false
                questionToEdit = null
            }
        )
    }

    val prioritized = remember(allTopics, topicId) {
        allTopics.find { it.topic.id == topicId } ?: allTopics.firstOrNull()
    }
    val topic = prioritized?.topic
    val subject = remember(subjects, topic) {
        subjects.find { it.id == topic?.subjectId }
    }
    val subjectColor = subject?.color?.let { parseHexColor(it) } ?: MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topic?.nombre ?: "Detalle del Tema", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (topic != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = subjectColor.copy(alpha = 0.1f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, subjectColor.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = subject?.nombre ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                color = subjectColor,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = topic.nombre,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(16.dp))

                            // Botón de Inicio de Quiz Principal
                            Button(
                                onClick = { onStartQuiz(topic.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                            ) {
                                Text("🎯 Iniciar Quiz de Preguntas (${questionsList.size})", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // ── Banco de Preguntas del Tema ──
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📝 Preguntas del Tema (${questionsList.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        FilledTonalButton(
                            onClick = { showAddDialog = true },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("➕ Añadir", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                if (questionsList.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Sin preguntas aún", fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                                Spacer(Modifier.height(4.dp))
                                Text("Toca '➕ Añadir' para crear tu primera pregunta de repaso.", style = MaterialTheme.typography.bodySmall, color = Color(0xFF94A3B8))
                            }
                        }
                    }
                } else {
                    items(questionsList, key = { it.id }) { question ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(if (question.isCustom) Color(0xFFEFF6FF) else Color(0xFFF1F5F9))
                                            .clip(RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (question.isCustom) "Personalizada" else "Oficial",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (question.isCustom) Color(0xFF2563EB) else Color(0xFF475569),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (question.isCustom) {
                                        Row {
                                            IconButton(
                                                onClick = { questionToEdit = question },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(16.dp), tint = Color(0xFF64748B))
                                            }
                                            IconButton(
                                                onClick = { viewModel.deleteQuestion(question) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(16.dp), tint = Color(0xFFEF4444))
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = question.question,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )

                                Spacer(Modifier.height(6.dp))
                                val correctText = when (question.correctOptionIndex) {
                                    0 -> question.optionA
                                    1 -> question.optionB
                                    2 -> question.optionC
                                    else -> question.optionD
                                }
                                Text(
                                    text = "✓ Respuesta: $correctText",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                item {
                    Text("🧠 Parámetros de Repetición Espaciada (SM-2)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Nivel de Confianza:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${topic.nivelConfianza} / 5 ⭐", fontWeight = FontWeight.Bold, color = confidenceColor(topic.nivelConfianza))
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Factor de Facilidad (EF):", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%.2f".format(topic.factorFacilidad), fontWeight = FontWeight.Bold)
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Intervalo Actual:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${topic.intervaloDias} días", fontWeight = FontWeight.Bold)
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Repeticiones Exitosas:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${topic.repeticiones}", fontWeight = FontWeight.Bold)
                            }
                            HorizontalDivider()
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Tiempo Total Estudiado:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("${topic.tiempoEstudiadoAcumulado} min", fontWeight = FontWeight.Bold, color = subjectColor)
                            }
                        }
                    }
                }

                item {
                    Text("⏱️ Temporizador Alternativo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onStartSession(topic.id, "MICRO") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                        ) {
                            Text("⚡ 5 min", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { onStartSession(topic.id, "POMODORO") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("🍅 25 min", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { onStartSession(topic.id, "FREE") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("⏱️ Libre")
                        }
                    }
                }
            }
        }
    }
}

// ── HabitsScreen & 90-Day Activity Heatmap ────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val todayMinutes by viewModel.todayMinutes.collectAsStateWithLifecycle()
    val weeklyMinutes by viewModel.weeklyMinutes.collectAsStateWithLifecycle()
    val habitsList by viewModel.last90DaysHabits.collectAsStateWithLifecycle()

    val habitMap = remember(habitsList) {
        habitsList.associateBy { it.dateKey }
    }

    // Generar los últimos 90 días ordenados de más antiguo a más reciente
    val dateFormat = remember { java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()) }
    val displayFormat = remember { java.text.SimpleDateFormat("EEE, d MMM", java.util.Locale("es", "ES")) }

    val days90 = remember {
        val list = mutableListOf<String>()
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, -89)
        for (i in 0 until 90) {
            list.add(dateFormat.format(cal.time))
            cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    var selectedDayKey by remember { mutableStateOf<String?>(days90.lastOrNull()) }
    val selectedDayHabit = remember(selectedDayKey, habitMap) {
        selectedDayKey?.let { habitMap[it] }
    }

    val totalMin90 = remember(habitsList) {
        habitsList.sumOf { it.totalMinutes }
    }
    val activeDaysCount = remember(habitsList) {
        habitsList.count { it.sessionsCount > 0 }
    }

    // Calcular racha actual
    val currentStreak = remember(days90, habitMap, todayMinutes) {
        var streak = 0
        for (day in days90.reversed()) {
            val h = habitMap[day]
            if (h != null && h.sessionsCount > 0) {
                streak++
            } else if (day == days90.last() && todayMinutes == 0) {
                // Si hoy aún no estudia, permitir continuar racha de ayer
                continue
            } else {
                break
            }
        }
        streak
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis hábitos", fontWeight = FontWeight.Bold) })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Métricas de Racha y Tiempo ──
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🔥", style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("$currentStreak días", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Racha actual", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("⏱️", style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("$todayMinutes min", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Estudio hoy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📅", style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("$activeDaysCount", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("Días activos (90 días)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // ── Heatmap de Actividad de 90 Días (GitHub Style Grid) ──
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "📅 Actividad (90 días)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    "${totalMin90 / 60} h ${totalMin90 % 60} min totales",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Grid desplazable horizontal de 13 semanas x 7 días
                        val scrollState = rememberScrollState()
                        val weeks = remember(days90) { days90.chunked(7) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            weeks.forEach { weekDays ->
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        weekDays.forEach { dateKey ->
                                            val habit = habitMap[dateKey]
                                            val minutes = habit?.totalMinutes ?: 0
                                            val isSelected = selectedDayKey == dateKey

                                            val cellColor = when {
                                                minutes == 0 -> MaterialTheme.colorScheme.surfaceVariant
                                                minutes <= 15 -> Color(0xFFBBF7D0) // Verde claro
                                                minutes <= 30 -> Color(0xFF4ADE80) // Verde medio
                                                minutes <= 60 -> Color(0xFF22C55E) // Verde sólido
                                                else -> Color(0xFF15803D)          // Verde intenso
                                            }

                                            Box(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(cellColor)
                                                    .border(
                                                        width = if (isSelected) 2.dp else 1.dp,
                                                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .clickable { selectedDayKey = dateKey }
                                            )
                                        }
                                    }
                                }
                            }

                        Spacer(Modifier.height(16.dp))

                        // Leyenda de Intensidad
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Menos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.width(6.dp))
                            listOf(
                                MaterialTheme.colorScheme.surfaceVariant,
                                Color(0xFFBBF7D0),
                                Color(0xFF4ADE80),
                                Color(0xFF22C55E),
                                Color(0xFF15803D)
                            ).forEach { color ->
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(color)
                                        .border(0.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(3.dp))
                                )
                                Spacer(Modifier.width(3.dp))
                            }
                            Spacer(Modifier.width(3.dp))
                            Text("Más", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(12.dp))

                        // Detalle del día seleccionado
                        selectedDayKey?.let { dateStr ->
                            val parsedDate = try { dateFormat.parse(dateStr) } catch (_: Exception) { null }
                            val formattedDate = parsedDate?.let { displayFormat.format(it) } ?: dateStr
                            val mins = selectedDayHabit?.totalMinutes ?: 0
                            val sessions = selectedDayHabit?.sessionsCount ?: 0

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = formattedDate.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (mins > 0) "$mins min estudiados ($sessions sesiones)"
                                        else "Sin sesiones de estudio registradas",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (mins > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (mins > 0) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF22C55E).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            "✅ Completado",
                                            color = Color(0xFF15803D),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ── Consejos de Hábitos ──
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.width(10.dp))
                            Text("Consistencia sobre cantidad", fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Dedicar solo 5 a 15 minutos diarios con el algoritmo SM-2 mantiene tus 28 temas frescos en la memoria a largo plazo y evita el estudio masivo de última hora.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// ── SettingsScreen ────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: com.example.semfour.ui.viewmodel.SettingsViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()
    val exp2Enabled by viewModel.exp2Enabled.collectAsStateWithLifecycle()
    val exp3Enabled by viewModel.exp3Enabled.collectAsStateWithLifecycle()
    val englishEnabled by viewModel.englishEnabled.collectAsStateWithLifecycle()
    val attendanceRemindersEnabled by viewModel.attendanceRemindersEnabled.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val dateFormat = remember { java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", java.util.Locale.getDefault()) }
    var showGuideDialog by remember { mutableStateOf(false) }

    if (showGuideDialog) {
        AppGuideDialog(onDismiss = { showGuideDialog = false })
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            // Usuario denegó permiso
        }
    }

    // Launcher de Activity nativo para Google Sign-In con selección de cuentas
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.onSignInResult(result.data)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ajustes", fontWeight = FontWeight.Bold) })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Sección 0: Temario y Asignaturas ──
            item {
                Text(
                    "TEMARIO Y ASIGNATURAS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Experiencia 1 (Base siempre activa)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Experiencia 1 (Base)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "28 temas iniciales y evaluaciones 1",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = true,
                                onCheckedChange = null,
                                enabled = false
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )

                        // Experiencia 2 Selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Experiencia 2",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Jetpack Compose avanzado, Maven/JavaFX MVC, React/AWS, APEX y Regresión",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = exp2Enabled,
                                onCheckedChange = { viewModel.toggleExperience(2, it) }
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )

                        // Experiencia 3 Selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Experiencia 3",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "API REST Retrofit, JDBC, Microservicios, NoSQL MongoDB y Ética Digital",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = exp3Enabled,
                                onCheckedChange = { viewModel.toggleExperience(3, it) }
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )

                        // Inglés Intermedio 1 Selector
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Inglés Intermedio 1 (INI4111)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Vocabulario técnico, correos, lectura de APIs, entrevistas y metodología ágil",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = englishEnabled,
                                onCheckedChange = { viewModel.toggleEnglish(it) }
                            )
                        }
                    }
                }
            }

            // ── Sección: Notificaciones de Asistencia ──
            item {
                Text(
                    "NOTIFICACIONES Y ASISTENCIA",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Recordatorios de asistencia",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Notificar a los 30 min de haber iniciado cada clase para registrar asistencia",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = attendanceRemindersEnabled,
                                onCheckedChange = { isChecked ->
                                    if (isChecked && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                        notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                    }
                                    viewModel.toggleAttendanceReminders(isChecked)
                                }
                            )
                        }
                    }
                }
            }

            // ── Sección 1: Guía e Instructivo ──
            item {
                Text(
                    "GUÍA E INSTRUCTIVO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    onClick = { showGuideDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Instructivo y preguntas frecuentes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Aprende a usar el algoritmo SM-2, los widgets y revisa las asignaturas",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // ── Sección 2: Cuenta de Google ──
            item {
                Text(
                    "CUENTA Y RESPALDO",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (authState is com.example.semfour.data.remote.AuthState.SignedIn)
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = if (authState is com.example.semfour.data.remote.AuthState.SignedIn)
                                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                when (val state = authState) {
                                    is com.example.semfour.data.remote.AuthState.SignedIn -> {
                                        Text(
                                            text = state.displayName.ifBlank { "Usuario conectado" },
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = state.email,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    is com.example.semfour.data.remote.AuthState.SignedOut -> {
                                        Text(
                                            text = "Cuenta de Google",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "No conectado (Toca para conectar)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botón de Conectar / Desconectar
                        when (authState) {
                            is com.example.semfour.data.remote.AuthState.SignedIn -> {
                                OutlinedButton(
                                    onClick = { viewModel.signOut(context) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Cerrar sesión en Google")
                                }
                            }
                            is com.example.semfour.data.remote.AuthState.SignedOut -> {
                                Button(
                                    onClick = {
                                        try {
                                            val signInIntent = viewModel.getSignInIntent(context)
                                            googleSignInLauncher.launch(signInIntent)
                                        } catch (e: Exception) {
                                            viewModel.signInWithCredentialManager(context)
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Conectar con Google Drive")
                                }
                            }
                        }
                    }
                }
            }

            // ── Sección 3: Sincronización ──
            item {
                Text(
                    "SINCRONIZACIÓN CON GOOGLE DRIVE",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (syncState is com.example.semfour.data.repository.SyncState.Syncing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CloudSync,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sincronización en la nube",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                val estadoText = when (val state = syncState) {
                                    is com.example.semfour.data.repository.SyncState.Syncing -> "Sincronizando datos ahora..."
                                    is com.example.semfour.data.repository.SyncState.Success ->
                                        "Última sync: ${dateFormat.format(java.util.Date(state.timestamp))}"
                                    is com.example.semfour.data.repository.SyncState.Error -> "Error: ${state.message}"
                                    is com.example.semfour.data.repository.SyncState.Idle ->
                                        if (authState is com.example.semfour.data.remote.AuthState.SignedIn)
                                            "Listo para sincronizar"
                                        else "Inicia sesión primero"
                                }

                                Text(
                                    text = estadoText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (syncState is com.example.semfour.data.repository.SyncState.Error)
                                        MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (authState is com.example.semfour.data.remote.AuthState.SignedOut) {
                                    try {
                                        val signInIntent = viewModel.getSignInIntent(context)
                                        googleSignInLauncher.launch(signInIntent)
                                    } catch (e: Exception) {
                                        viewModel.signInWithCredentialManager(context)
                                    }
                                } else {
                                    viewModel.syncNow(context)
                                }
                            },
                            enabled = syncState !is com.example.semfour.data.repository.SyncState.Syncing,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            if (syncState is com.example.semfour.data.repository.SyncState.Syncing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sincronizando...")
                            } else {
                                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Sincronizar ahora con Google Drive")
                            }
                        }
                    }
                }
            }

            // ── Sección 4: Info y Descarga ──
            item {
                Text(
                    "INFORMACIÓN Y DESCARGAS",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("SemFour", fontWeight = FontWeight.Bold)
                                Text("Planificador de estudio inteligente • 4.º semestre", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Versión 1.0 • Offline-First + SM-2 + Drive", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                        }

                        Spacer(Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("https://github.com/FerB22/sem-four/releases/latest")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Descargar instalador APK")
                        }

                        Spacer(Modifier.height(8.dp))

                        OutlinedButton(
                            onClick = {
                                val intent = android.content.Intent(
                                    android.content.Intent.ACTION_VIEW,
                                    android.net.Uri.parse("https://github.com/FerB22/sem-four")
                                )
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Ver código fuente en GitHub")
                        }
                    }
                }
            }
        }
    }
}

