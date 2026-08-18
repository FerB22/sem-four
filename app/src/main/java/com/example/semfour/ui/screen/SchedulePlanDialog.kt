package com.example.semfour.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.semfour.data.local.entity.DailyPlanTaskEntity
import com.example.semfour.data.local.entity.SubjectEntity

@Composable
fun SchedulePlanDialog(
    allTasks: List<DailyPlanTaskEntity>,
    subjects: List<SubjectEntity>,
    initialWeek: Int = 1,
    onToggleTask: (String, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedWeek by remember { mutableStateOf(initialWeek.coerceIn(1, 16)) }
    val subjectMap = remember(subjects) { subjects.associateBy { it.id } }

    val totalCompleted = remember(allTasks) { allTasks.count { it.isCompleted } }
    val totalCount = remember(allTasks) { allTasks.size.coerceAtLeast(1) }
    val globalProgress = totalCompleted.toFloat() / totalCount.toFloat()

    val weekTasks = remember(allTasks, selectedWeek) {
        allTasks.filter { it.weekNumber == selectedWeek }
    }
    val weekCompleted = remember(weekTasks) { weekTasks.count { it.isCompleted } }
    val weekTotal = remember(weekTasks) { weekTasks.size.coerceAtLeast(1) }
    val weekProgress = weekCompleted.toFloat() / weekTotal.toFloat()

    val days = listOf(
        1 to "Lunes",
        2 to "Martes",
        3 to "Miércoles",
        4 to "Jueves",
        5 to "Viernes"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // ── Cabecera ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📅 Cronograma de Estudio",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Asignación operativa de cuadernos .ipynb (16 semanas)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Barra de Progreso Global ──
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Progreso Semestral",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                "$totalCompleted / $totalCount tareas (${(globalProgress * 100).toInt()}%)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { globalProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color(0xFFE2E8F0)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                // ── Selector de Semanas 1 a 16 (Horizontal Scroll) ──
                Text(
                    "Selecciona la semana académica:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )
                Spacer(Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(16) { index ->
                        val weekNum = index + 1
                        val isSelected = selectedWeek == weekNum
                        val isWeekDone = allTasks.filter { it.weekNumber == weekNum }.let { list ->
                            list.isNotEmpty() && list.all { it.isCompleted }
                        }

                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedWeek = weekNum },
                            label = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        "Semana $weekNum",
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                    if (isWeekDone) {
                                        Spacer(Modifier.width(4.dp))
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFF22C55E),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF0F172A),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                // ── Resumen de la Semana Seleccionada ──
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "📌 Semana $selectedWeek ($weekCompleted/$weekTotal completadas)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // ── Lista de Días y Tareas de la Semana ──
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    days.forEach { (dayNum, dayName) ->
                        val tasksForDay = weekTasks.filter { it.dayOfWeek == dayNum }
                        if (tasksForDay.isNotEmpty()) {
                            item(key = "day_header_$dayNum") {
                                DayPlanSection(
                                    dayName = dayName,
                                    tasks = tasksForDay,
                                    subjectMap = subjectMap,
                                    onToggleTask = onToggleTask
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayPlanSection(
    dayName: String,
    tasks: List<DailyPlanTaskEntity>,
    subjectMap: Map<String, SubjectEntity>,
    onToggleTask: (String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = dayName.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tasks.forEach { task ->
                    PlanTaskItem(
                        task = task,
                        subject = subjectMap[task.subjectId],
                        onToggle = { isChecked -> onToggleTask(task.id, isChecked) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlanTaskItem(
    task: DailyPlanTaskEntity,
    subject: SubjectEntity?,
    onToggle: (Boolean) -> Unit
) {
    val subjectColor = subject?.let { parseHexColor(it.color) }
        ?: if (task.subjectId == "sub_consolidacion") Color(0xFF8B5CF6) else MaterialTheme.colorScheme.primary
    val subjectName = subject?.nombre ?: if (task.subjectId == "sub_consolidacion") "Consolidación y Cierre" else "Asignatura"
    val subjectCode = subject?.codigo ?: if (task.subjectId == "sub_consolidacion") "CIERRE" else ""

    Card(
        onClick = { onToggle(!task.isCompleted) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) Color(0xFFF1F5F9) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (task.isCompleted) Color(0xFFCBD5E1) else Color(0xFFE2E8F0)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF22C55E),
                    uncheckedColor = Color(0xFF94A3B8)
                )
            )

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (subjectCode.isNotBlank()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(subjectColor.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = subjectCode,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = subjectColor
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(
                        text = subjectName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (task.isCompleted) Color(0xFF64748B) else Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = task.taskType,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (task.isCompleted) FontWeight.Normal else FontWeight.Medium,
                    color = if (task.isCompleted) Color(0xFF94A3B8) else Color(0xFF334155),
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                if (task.notebookFile.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = if (task.isCompleted) Color(0xFF94A3B8) else Color(0xFF64748B),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = task.notebookFile,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (task.isCompleted) Color(0xFF94A3B8) else Color(0xFF475569),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
