package com.example.semfour.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import com.example.semfour.data.local.entity.ScheduleEntity
import com.example.semfour.data.local.entity.SubjectEntity
import com.example.semfour.ui.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onSubjectClick: (String) -> Unit = {}
) {
    val allSchedule by viewModel.allSchedule.collectAsStateWithLifecycle()
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()
    val currentDay = viewModel.currentDayOfWeek // 1=Lunes, ..., 5=Viernes

    var selectedDay by remember { mutableIntStateOf(if (currentDay in 1..5) currentDay else 1) }

    val days = remember {
        listOf(
            1 to "Lunes",
            2 to "Martes",
            3 to "Miércoles",
            4 to "Jueves",
            5 to "Viernes"
        )
    }

    val daySchedule = remember(allSchedule, selectedDay) {
        allSchedule.filter { it.dayOfWeek == selectedDay }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Horario de clases", fontWeight = FontWeight.Bold)
                        Text(
                            "Analista Programador • 4.º semestre",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Selector de Días ──────────────────────────────────────────────
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(days, key = { it.first }) { (dayNum, dayName) ->
                    val isSelected = selectedDay == dayNum
                    val isToday = currentDay == dayNum

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedDay = dayNum },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = dayName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                                if (isToday) {
                                    Spacer(Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // ── Contenido de las clases del día ───────────────────────────────
            if (daySchedule.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Weekend,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Sin clases programadas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "¡Día libre de horario presencial! Buen momento para avanzar repasos pendientes.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val cal = java.util.Calendar.getInstance()
                    val currentMinutes = cal.get(java.util.Calendar.HOUR_OF_DAY) * 60 + cal.get(java.util.Calendar.MINUTE)
                    val isToday = currentDay == selectedDay
                    var foundNext = false

                    items(daySchedule, key = { it.id }) { scheduleItem ->
                        val subject = subjects.find { it.id == scheduleItem.subjectId }
                        val subjectColor = subject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary

                        val startMin = parseScheduleTimeToMinutes(scheduleItem.startTime)
                        val endMin = parseScheduleTimeToMinutes(scheduleItem.endTime)

                        val isPassed = isToday && startMin != null && endMin != null && currentMinutes > endMin
                        val isInProgress = isToday && startMin != null && endMin != null && currentMinutes in startMin..endMin
                        var isNext = false
                        var badgeText = ""
                        var badgeColor = Color(0xFF64748B)

                        if (isToday) {
                            when {
                                isPassed -> {
                                    badgeText = "✓ Finalizada"
                                    badgeColor = Color(0xFF94A3B8)
                                }
                                isInProgress -> {
                                    val remaining = maxOf(1, endMin!! - currentMinutes)
                                    badgeText = "⏱️ Termina en ${remaining}m"
                                    badgeColor = Color(0xFF16A34A)
                                }
                                startMin != null && currentMinutes < startMin -> {
                                    if (!foundNext) {
                                        foundNext = true
                                        isNext = true
                                        val diff = maxOf(1, startMin - currentMinutes)
                                        badgeText = if (diff >= 60) {
                                            val h = diff / 60
                                            val m = diff % 60
                                            if (m > 0) "⏳ En ${h}h ${m}m" else "⏳ En ${h}h"
                                        } else {
                                            "⏳ En ${diff}m"
                                        }
                                        badgeColor = Color(0xFFD97706)
                                    }
                                }
                            }
                        }

                        ScheduleCard(
                            schedule = scheduleItem,
                            subject = subject,
                            subjectColor = subjectColor,
                            isPassed = isPassed,
                            isInProgress = isInProgress,
                            isNext = isNext,
                            badgeText = badgeText,
                            badgeColor = badgeColor,
                            onClick = {
                                if (subject != null) onSubjectClick(subject.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduleCard(
    schedule: ScheduleEntity,
    subject: SubjectEntity?,
    subjectColor: Color,
    isPassed: Boolean = false,
    isInProgress: Boolean = false,
    isNext: Boolean = false,
    badgeText: String = "",
    badgeColor: Color = Color(0xFF64748B),
    onClick: () -> Unit
) {
    val barColor = if (isPassed) Color(0xFFCBD5E1) else subjectColor
    val titleColor = if (isPassed) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurface
    val timeTextColor = if (isPassed) Color(0xFF94A3B8) else if (isInProgress) Color(0xFF16A34A) else subjectColor
    val detailsTextColor = if (isPassed) Color(0xFF94A3B8) else MaterialTheme.colorScheme.onSurfaceVariant

    val cardBg = when {
        isInProgress -> Color(0xFFF0FDF4)
        isNext -> Color(0xFFFFFBEB)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val cardBorder = when {
        isInProgress -> androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC))
        isNext -> androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
        else -> null
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = cardBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isInProgress) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Barra de color lateral
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(68.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(barColor)
            )

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Horario + Código + Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = timeTextColor
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "${schedule.startTime} - ${schedule.endTime}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = timeTextColor
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (badgeText.isNotBlank()) {
                            Surface(
                                color = when {
                                    isInProgress -> Color(0xFFDCFCE7)
                                    isNext -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFF1F5F9)
                                },
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = badgeText,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = badgeColor
                                )
                            }
                            Spacer(Modifier.width(6.dp))
                        }

                        Surface(
                            color = if (isPassed) Color(0xFFE2E8F0) else subjectColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = subject?.codigo ?: "",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isPassed) Color(0xFF94A3B8) else subjectColor
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                // Nombre de la asignatura
                Text(
                    text = subject?.nombre ?: "Asignatura",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isInProgress) FontWeight.ExtraBold else FontWeight.Bold,
                    color = titleColor,
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                // Sala y Profesor
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MeetingRoom,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = detailsTextColor
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = schedule.room,
                        style = MaterialTheme.typography.bodySmall,
                        color = detailsTextColor,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = detailsTextColor
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = schedule.professor,
                        style = MaterialTheme.typography.bodySmall,
                        color = detailsTextColor,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}
