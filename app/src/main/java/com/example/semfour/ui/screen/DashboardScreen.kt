package com.example.semfour.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.ScheduleEntity
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.domain.algorithm.PrioritizedTopic
import com.example.semfour.ui.viewmodel.DashboardViewModel
import com.example.semfour.ui.viewmodel.SessionType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onStartSession: (topicId: String, sessionType: String) -> Unit,
    onOpenSubject: (subjectId: String) -> Unit
) {
    val topPriority by viewModel.topPriorityTopic.collectAsStateWithLifecycle()
    val allTopics by viewModel.prioritizedTopics.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val todayMinutes by viewModel.todayMinutes.collectAsStateWithLifecycle()
    val evaluations by viewModel.upcomingEvaluations.collectAsStateWithLifecycle()
    val dueCount by viewModel.dueTodayCount.collectAsStateWithLifecycle()
    val todaySchedule by viewModel.todaySchedule.collectAsStateWithLifecycle()

    val selectedPlanWeek by viewModel.selectedPlanWeek.collectAsStateWithLifecycle()
    val selectedPlanDay by viewModel.selectedPlanDay.collectAsStateWithLifecycle()
    val planTasksForDay by viewModel.planTasksForSelectedDay.collectAsStateWithLifecycle()
    val planTasksForWeek by viewModel.planTasksForSelectedWeek.collectAsStateWithLifecycle()
    val allPlanTasks by viewModel.allPlanTasks.collectAsStateWithLifecycle()

    val subjectMap = remember(subjects) { subjects.associateBy { it.id } }

    var evaluationToEdit by remember { mutableStateOf<EvaluationEntity?>(null) }
    var showEvaluationDialog by remember { mutableStateOf(false) }
    var showSchedulePlanDialog by remember { mutableStateOf(false) }
    var showClassesBottomSheet by remember { mutableStateOf(false) }
    var showAllEvaluations by remember { mutableStateOf(false) }
    var showFullTopicQueue by remember { mutableStateOf(false) }

    if (showClassesBottomSheet) {
        TodayClassesBottomSheet(
            classes = todaySchedule,
            subjectMap = subjectMap,
            onSubjectClick = onOpenSubject,
            onDismiss = { showClassesBottomSheet = false }
        )
    }

    if (showSchedulePlanDialog) {
        SchedulePlanDialog(
            allTasks = allPlanTasks,
            subjects = subjects,
            initialWeek = selectedPlanWeek,
            onToggleTask = { taskId, isCompleted -> viewModel.togglePlanTask(taskId, isCompleted) },
            onDismiss = { showSchedulePlanDialog = false }
        )
    }

    if (showEvaluationDialog) {
        EvaluationFormDialog(
            evaluationToEdit = evaluationToEdit,
            subjects = subjects,
            onSave = { eval -> viewModel.saveEvaluation(eval) },
            onDelete = { eval -> viewModel.deleteEvaluation(eval) },
            onDismiss = {
                showEvaluationDialog = false
                evaluationToEdit = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // ── 1. Header con acceso superior a Clases y Micro-widget ───────────
        item(contentType = "header") {
            DashboardHeader(
                todayMinutes = todayMinutes,
                dueCount = dueCount,
                todaySchedule = todaySchedule,
                subjectMap = subjectMap,
                onOpenClasses = { showClassesBottomSheet = true }
            )
        }

        // ── 2. Tarjeta Prioritaria (Recomendación SM-2 N.º 1) ─────────────────
        item(contentType = "priority_card") {
            topPriority?.let { priority ->
                val subject = subjectMap[priority.topic.subjectId]
                PriorityTopicCard(
                    prioritizedTopic = priority,
                    subject = subject,
                    onStartMicro = {
                        onStartSession(priority.topic.id, SessionType.MICRO.name)
                    },
                    onStartPomodoro = {
                        onStartSession(priority.topic.id, SessionType.POMODORO.name)
                    }
                )
            }
        }

        // ── 3. Cronograma Operativo de Estudio (Semana y Día) ─────────────────
        item(contentType = "daily_plan") {
            DailyStudyPlanSection(
                selectedWeek = selectedPlanWeek,
                selectedDay = selectedPlanDay,
                tasks = planTasksForDay,
                weekTasks = planTasksForWeek,
                subjectMap = subjectMap,
                onSelectWeek = { viewModel.selectPlanWeek(it) },
                onSelectDay = { viewModel.selectPlanDay(it) },
                onToggleTask = { taskId, isCompleted -> viewModel.togglePlanTask(taskId, isCompleted) },
                onOpenFullSchedule = { showSchedulePlanDialog = true }
            )
        }

        // ── 4. Chips de Asignaturas ───────────────────────────────────────────
        item(contentType = "subject_chips") {
            SubjectChipsRow(subjects = subjects, onSubjectClick = onOpenSubject)
        }

        // ── 5. Próximas evaluaciones (Límite temporal + Colapsable) ───────────
        val confirmedEvals = remember(evaluations) {
            evaluations.filter { it.fechaEval > 0L }.sortedBy { it.fechaEval }
        }
        val undeterminedEvals = remember(evaluations) {
            evaluations.filter { it.fechaEval == 0L }
        }
        val sortedEvaluations = remember(confirmedEvals, undeterminedEvals) {
            confirmedEvals + undeterminedEvals
        }
        val displayedEvaluations = remember(sortedEvaluations, showAllEvaluations, confirmedEvals) {
            if (showAllEvaluations) sortedEvaluations
            else (if (confirmedEvals.isNotEmpty()) confirmedEvals.take(2) else sortedEvaluations.take(2))
        }

        item(contentType = "evaluations_header") {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "📅 Próximas evaluaciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (confirmedEvals.isNotEmpty()) {
                        Spacer(Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${confirmedEvals.size} confirmadas",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                FilledTonalButton(
                    onClick = {
                        evaluationToEdit = null
                        showEvaluationDialog = true
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Añadir", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (displayedEvaluations.isNotEmpty()) {
            items(displayedEvaluations, key = { it.id }, contentType = { "evaluation_card" }) { eval ->
                EvaluationCard(
                    evaluation = eval,
                    subjectMap = subjectMap,
                    onEditClick = {
                        evaluationToEdit = eval
                        showEvaluationDialog = true
                    }
                )
            }
            if (sortedEvaluations.size > 2) {
                item(contentType = "evaluations_expand_btn") {
                    TextButton(
                        onClick = { showAllEvaluations = !showAllEvaluations },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            text = if (showAllEvaluations) "Mostrar menos ▴" else "Ver más evaluaciones (+${sortedEvaluations.size - displayedEvaluations.size}) ▾",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        } else {
            item(contentType = "evaluations_empty") {
                Text(
                    "No hay evaluaciones registradas. Toca «Añadir» para registrar una.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }
        }

        // ── 6. Cola de repaso (Top 3 temas + Expansión) ──────────────────────
        val queueTopics = remember(allTopics) { allTopics.drop(1) }
        val displayedQueue = remember(queueTopics, showFullTopicQueue) {
            if (showFullTopicQueue) queueTopics else queueTopics.take(3)
        }

        if (queueTopics.isNotEmpty()) {
            item(contentType = "queue_header") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "📚 Cola de repaso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${queueTopics.size} temas en total",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            items(displayedQueue, key = { it.topic.id }, contentType = { "queue_item" }) { priority ->
                val subject = subjectMap[priority.topic.subjectId]
                TopicQueueItem(
                    prioritizedTopic = priority,
                    subject = subject,
                    onStart = { onStartSession(priority.topic.id, SessionType.MICRO.name) }
                )
            }

            if (queueTopics.size > 3) {
                item(contentType = "queue_expand_btn") {
                    TextButton(
                        onClick = { showFullTopicQueue = !showFullTopicQueue },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (showFullTopicQueue) "⚡ Mostrar solo prioritarios ▴" else "⚡ +${queueTopics.size - 3} temas pendientes en cola ▾",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodayClassesBottomSheet(
    classes: List<ScheduleEntity>,
    subjectMap: Map<String, SubjectEntity>,
    onSubjectClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🕒 Horario de Hoy",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                    Text(
                        text = "${classes.size} clases programadas para hoy",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                classes.forEach { scheduleItem ->
                    val subject = subjectMap[scheduleItem.subjectId]
                    val color = subject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary

                    Card(
                        onClick = {
                            subject?.let { onSubjectClick(it.id) }
                            onDismiss()
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(44.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(color)
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${scheduleItem.startTime} - ${scheduleItem.endTime}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = color
                                    )
                                    if (subject?.codigo?.isNotBlank() == true) {
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "• ${subject.codigo}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF64748B)
                                        )
                                    }
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = subject?.nombre ?: "Asignatura",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = "📍 ${scheduleItem.room} • 👨‍🏫 ${scheduleItem.professor}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF475569)
                                )
                            }
                            Icon(
                                Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
    todayMinutes: Int,
    dueCount: Int,
    todaySchedule: List<ScheduleEntity>,
    subjectMap: Map<String, SubjectEntity>,
    onOpenClasses: () -> Unit
) {
    val cal = Calendar.getInstance()
    val hora = cal.get(Calendar.HOUR_OF_DAY)
    val saludo = when {
        hora < 12 -> "Buenos días 🌅"
        hora < 18 -> "Buenas tardes ☀️"
        else -> "Buenas noches 🌙"
    }

    val nextClass = remember(todaySchedule) { todaySchedule.firstOrNull() }
    val nextClassSubject = nextClass?.let { subjectMap[it.subjectId]?.nombre } ?: ""

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = saludo,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "4.º Semestre",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (todaySchedule.isNotEmpty()) {
                    Surface(
                        onClick = onOpenClasses,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = "${todaySchedule.size} clases",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                // Stat chip minutos hoy
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "$todayMinutes min hoy",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Micro-banner si hay clases hoy (delgado e interactivo)
        if (nextClass != null) {
            Spacer(Modifier.height(10.dp))
            Surface(
                onClick = onOpenClasses,
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Próx: ${nextClass.startTime} $nextClassSubject",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "• ${nextClass.room}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF64748B),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Ver horario",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PriorityTopicCard(
    prioritizedTopic: PrioritizedTopic,
    subject: SubjectEntity?,
    onStartMicro: () -> Unit,
    onStartPomodoro: () -> Unit
) {
    val subjectColor = subject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary
    val scorePercent = (prioritizedTopic.score * 100).roundToInt()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Fila superior: Chip Prioridad + Nivel Confianza
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = subjectColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(subjectColor)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "PRIORIDAD N.º 1 • $scorePercent %",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = subjectColor
                        )
                    }
                }

                // Estrellas de confianza
                ConfidenceStars(nivel = prioritizedTopic.topic.nivelConfianza)
            }

            Spacer(Modifier.height(12.dp))

            // Nombre de la asignatura
            Text(
                text = subject?.nombre ?: "Asignatura",
                style = MaterialTheme.typography.labelMedium,
                color = subjectColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(4.dp))

            // Nombre del tema
            Text(
                text = prioritizedTopic.topic.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Indicador de estado SM-2
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                val (statusText, statusColor) = when {
                    prioritizedTopic.estaVencido -> "Repaso vencido" to MaterialTheme.colorScheme.error
                    prioritizedTopic.esNuevo -> "Tema nuevo" to MaterialTheme.colorScheme.primary
                    else -> "Próximo repaso al día" to MaterialTheme.colorScheme.onSurfaceVariant
                }
                Text(
                    text = "● $statusText",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onStartMicro,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Bolt, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Micro (5 min)", fontSize = 13.sp)
                }

                Button(
                    onClick = onStartPomodoro,
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = subjectColor)
                ) {
                    Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Pomodoro", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun ConfidenceStars(nivel: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= nivel) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (i <= nivel) Color(0xFFFFD54F) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun SubjectChipsRow(
    subjects: List<SubjectEntity>,
    onSubjectClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(subjects, key = { it.id }) { subject ->
            val color = parseHexColor(subject.color)
            FilterChip(
                selected = false,
                onClick = { onSubjectClick(subject.id) },
                label = { Text(subject.codigo, fontWeight = FontWeight.SemiBold) },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
private fun EvaluationCard(
    evaluation: EvaluationEntity,
    subjectMap: Map<String, SubjectEntity>,
    onEditClick: () -> Unit
) {
    val subject = subjectMap[evaluation.subjectId]
    val color = subject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary

    val esIndeterminada = evaluation.fechaEval <= 0L
    val diasRestantes = if (!esIndeterminada) {
        ((evaluation.fechaEval - System.currentTimeMillis()) / (86_400_000L)).toInt()
    } else 0

    val diasTexto = when {
        esIndeterminada -> "Por definir"
        diasRestantes < 0 -> "Venció"
        diasRestantes == 0 -> "¡Hoy!"
        diasRestantes == 1 -> "Mañana"
        else -> "En $diasRestantes días"
    }

    val badgeBgColor = when {
        esIndeterminada -> MaterialTheme.colorScheme.surfaceVariant
        diasRestantes <= 2 -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }

    val badgeTextColor = when {
        esIndeterminada -> MaterialTheme.colorScheme.onSurfaceVariant
        diasRestantes <= 2 -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (esIndeterminada) 0.45f else 0.7f)
        ),
        onClick = onEditClick
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(44.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (esIndeterminada) color.copy(alpha = 0.4f) else color)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subject?.nombre ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (esIndeterminada) MaterialTheme.colorScheme.onSurfaceVariant else color,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = evaluation.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                color = badgeBgColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = diasTexto,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = badgeTextColor
                )
            }
            Spacer(Modifier.width(4.dp))
            IconButton(
                onClick = onEditClick,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar evaluación",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun TopicQueueItem(
    prioritizedTopic: PrioritizedTopic,
    subject: SubjectEntity?,
    onStart: () -> Unit
) {
    val color = subject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary

    ListItem(
        headlineContent = {
            Text(prioritizedTopic.topic.nombre, maxLines = 1, overflow = TextOverflow.Ellipsis)
        },
        supportingContent = {
            Text(
                subject?.nombre ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "${(prioritizedTopic.score * 100).roundToInt()}",
                    style = MaterialTheme.typography.labelMedium,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        trailingContent = {
            IconButton(onClick = onStart) {
                Icon(Icons.Default.PlayArrow, "Estudiar", tint = color)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onStart() }
    )
}

// ── Utilidades ────────────────────────────────────────────────────────────────

private val colorCache = java.util.concurrent.ConcurrentHashMap<String, Color>()

fun parseHexColor(hex: String): Color {
    if (hex.isBlank()) return Color.Gray
    return colorCache.getOrPut(hex) {
        try {
            Color(hex.toColorInt())
        } catch (_: Exception) {
            try {
                Color(android.graphics.Color.parseColor(hex))
            } catch (_: Exception) {
                Color.Gray
            }
        }
    }
}

fun confidenceColor(nivel: Int): Color = when (nivel) {
    1 -> Color(0xFFFF6B6B)
    2 -> Color(0xFFFFA726)
    3 -> Color(0xFFFFD54F)
    4 -> Color(0xFF81C784)
    5 -> Color(0xFF4CAF50)
    else -> Color.Gray
}

@Composable
private fun DailyStudyPlanSection(
    selectedWeek: Int,
    selectedDay: Int,
    tasks: List<com.example.semfour.data.local.entity.DailyPlanTaskEntity>,
    weekTasks: List<com.example.semfour.data.local.entity.DailyPlanTaskEntity>,
    subjectMap: Map<String, SubjectEntity>,
    onSelectWeek: (Int) -> Unit,
    onSelectDay: (Int) -> Unit,
    onToggleTask: (String, Boolean) -> Unit,
    onOpenFullSchedule: () -> Unit
) {
    val weekCompleted = remember(weekTasks) { weekTasks.count { it.isCompleted } }
    val weekTotal = remember(weekTasks) { weekTasks.size.coerceAtLeast(1) }
    val weekPercent = remember(weekCompleted, weekTotal) {
        (weekCompleted.toFloat() / weekTotal.toFloat() * 100).toInt()
    }

    val days = remember {
        listOf(
            1 to "Lun",
            2 to "Mar",
            3 to "Mié",
            4 to "Jue",
            5 to "Vie"
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cabecera con selector de semana y botón para abrir visor completo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onSelectWeek(selectedWeek - 1) },
                        enabled = selectedWeek > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Semana anterior", modifier = Modifier.size(20.dp))
                    }

                    Text(
                        text = "Semana $selectedWeek de 16",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    IconButton(
                        onClick = { onSelectWeek(selectedWeek + 1) },
                        enabled = selectedWeek < 16,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.ChevronRight, contentDescription = "Semana siguiente", modifier = Modifier.size(20.dp))
                    }
                }

                TextButton(
                    onClick = onOpenFullSchedule,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.CalendarViewMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Ver todo", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            // Barra de progreso de la semana
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progreso semanal: $weekCompleted/$weekTotal tareas",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "$weekPercent%",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            LinearProgressIndicator(
                progress = { weekCompleted.toFloat() / weekTotal.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color(0xFFF1F5F9)
            )

            Spacer(Modifier.height(12.dp))

            // Selector de días Lunes a Viernes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { (dayNum, dayLabel) ->
                    val isSelected = selectedDay == dayNum
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectDay(dayNum) },
                        label = { Text(dayLabel, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF0F172A),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Lista de tareas del día seleccionado
            if (tasks.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tasks.forEach { task ->
                        key(task.id) {
                            PlanTaskItem(
                                task = task,
                                subject = subjectMap[task.subjectId],
                                onToggle = { isChecked -> onToggleTask(task.id, isChecked) }
                            )
                        }
                    }
                }
            } else {
                Text(
                    "No hay tareas asignadas para este día.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}
