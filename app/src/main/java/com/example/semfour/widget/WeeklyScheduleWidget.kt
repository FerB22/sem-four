@file:Suppress("RestrictedApi")

package com.example.semfour.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.launch
import com.example.semfour.MainActivity
import com.example.semfour.ui.viewmodel.DashboardViewModel

import java.util.Calendar

data class WidgetScheduleDay(
    val dayNum: Int,
    val dayName: String,
    val classes: List<WidgetClassItem>
)

data class WidgetClassItem(
    val time: String,
    val subjectCode: String,
    val subjectName: String,
    val room: String,
    val professor: String,
    val colorHex: String
)

private enum class ClassLiveStatus {
    PASSED,
    IN_PROGRESS,
    NEXT,
    PENDING,
    OTHER_DAY
}

private data class ClassStatusInfo(
    val status: ClassLiveStatus,
    val timeInfo: String,
    val isPastDay: Boolean
)

/**
 * Widget 3: Horario Semanal Completo para la pantalla de inicio (Light & Clean Theme).
 * Muestra todos los bloques de clases de Lunes a Jueves con salas, profesores y estado en vivo.
 */
class WeeklyScheduleWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val scheduleDays = defaultSchedule()

        provideContent {
            GlanceTheme {
                val cal = Calendar.getInstance()
                val currentDay = DashboardViewModel.getDayOfWeekIndex()
                val currentMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

                WeeklyScheduleContent(
                    scheduleDays = scheduleDays,
                    currentDay = currentDay,
                    currentMinutes = currentMinutes
                )
            }
        }
    }

    @Composable
    private fun WeeklyScheduleContent(
        scheduleDays: List<WidgetScheduleDay>,
        currentDay: Int,
        currentMinutes: Int
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .cornerRadius(18.dp)
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.TopStart
        ) {
            Column(modifier = GlanceModifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🗓️ HORARIO SEMANAL",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF0F172A)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "Duoc UC • 4.º Sem",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF64748B)),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                // Lista scrolleable con todos los días de la semana
                LazyColumn(
                    modifier = GlanceModifier.fillMaxSize()
                ) {
                    items(scheduleDays) { day ->
                        DaySection(
                            day = day,
                            currentDay = currentDay,
                            currentMinutes = currentMinutes
                        )
                        Spacer(modifier = GlanceModifier.height(8.dp))
                    }
                }
            }
        }
    }

    @Composable
    private fun DaySection(
        day: WidgetScheduleDay,
        currentDay: Int,
        currentMinutes: Int
    ) {
        val isToday = day.dayNum == currentDay
        val isPastDay = day.dayNum < currentDay
        val headerBg = if (isToday) Color(0xFF0F172A) else Color(0xFFF1F5F9)
        val headerTextColor = if (isToday) Color(0xFFFFFFFF) else Color(0xFF475569)

        var foundNext = false

        Column(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAFC))
                .cornerRadius(12.dp)
                .padding(bottom = 6.dp)
        ) {
            // Day Header Pill
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(headerBg)
                    .cornerRadius(10.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day.dayName.uppercase(),
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(headerTextColor),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                if (isToday) {
                    Spacer(modifier = GlanceModifier.width(6.dp))
                    Text(
                        text = "• HOY",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF10B981)),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(4.dp))

            // Classes list
            day.classes.forEach { item ->
                val statusInfo = if (isToday) {
                    val interval = parseTimeInterval(item.time)
                    if (interval != null) {
                        val (startMin, endMin) = interval
                        when {
                            currentMinutes > endMin -> {
                                ClassStatusInfo(ClassLiveStatus.PASSED, "✓ Finalizada", false)
                            }
                            currentMinutes in startMin..endMin -> {
                                val remMin = maxOf(1, endMin - currentMinutes)
                                ClassStatusInfo(ClassLiveStatus.IN_PROGRESS, "⏱️ Termina en ${remMin}m", false)
                            }
                            currentMinutes < startMin -> {
                                if (!foundNext) {
                                    foundNext = true
                                    val diff = maxOf(1, startMin - currentMinutes)
                                    val info = if (diff >= 60) {
                                        val h = diff / 60
                                        val m = diff % 60
                                        if (m > 0) "⏳ En ${h}h ${m}m" else "⏳ En ${h}h"
                                    } else {
                                        "⏳ En ${diff}m"
                                    }
                                    ClassStatusInfo(ClassLiveStatus.NEXT, info, false)
                                } else {
                                    ClassStatusInfo(ClassLiveStatus.PENDING, "", false)
                                }
                            }
                            else -> ClassStatusInfo(ClassLiveStatus.PENDING, "", false)
                        }
                    } else {
                        ClassStatusInfo(ClassLiveStatus.PENDING, "", false)
                    }
                } else {
                    ClassStatusInfo(ClassLiveStatus.OTHER_DAY, "", isPastDay)
                }

                ClassItemRow(item = item, statusInfo = statusInfo)
            }
        }
    }

    @Composable
    private fun ClassItemRow(item: WidgetClassItem, statusInfo: ClassStatusInfo) {
        val isPassed = statusInfo.status == ClassLiveStatus.PASSED || statusInfo.isPastDay
        val isInProgress = statusInfo.status == ClassLiveStatus.IN_PROGRESS
        val isNext = statusInfo.status == ClassLiveStatus.NEXT

        val accentColor = if (isPassed) Color(0xFFCBD5E1) else parseColor(item.colorHex)
        val titleColor = if (isPassed) Color(0xFF94A3B8) else Color(0xFF0F172A)
        val timeColor = if (isPassed) Color(0xFF94A3B8) else if (isInProgress) Color(0xFF059669) else Color(0xFF0284C7)
        val roomColor = if (isPassed) Color(0xFFCBD5E1) else Color(0xFF64748B)

        val rowBackground = when {
            isInProgress -> Color(0xFFF0FDF4)
            isNext -> Color(0xFFFFFFFF)
            else -> Color.Transparent
        }

        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(rowBackground)
                .cornerRadius(8.dp)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra de color de asignatura
                Box(
                    modifier = GlanceModifier
                        .width(4.dp)
                        .height(30.dp)
                        .background(accentColor)
                        .cornerRadius(2.dp)
                ) {}

                Spacer(modifier = GlanceModifier.width(8.dp))

                // Info de la clase
                Column(modifier = GlanceModifier.defaultWeight()) {
                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.subjectName,
                            maxLines = 1,
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(titleColor),
                                fontSize = 11.sp,
                                fontWeight = if (isInProgress) FontWeight.Bold else FontWeight.Medium
                            )
                        )

                        if (statusInfo.timeInfo.isNotBlank()) {
                            Spacer(modifier = GlanceModifier.defaultWeight())
                            Text(
                                text = statusInfo.timeInfo,
                                style = TextStyle(
                                    color = androidx.glance.unit.ColorProvider(
                                        when {
                                            isInProgress -> Color(0xFF16A34A)
                                            isNext -> Color(0xFFD97706)
                                            isPassed -> Color(0xFF94A3B8)
                                            else -> Color(0xFF64748B)
                                        }
                                    ),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row(
                        modifier = GlanceModifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.time,
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(timeColor),
                                fontSize = 10.sp,
                                fontWeight = if (isInProgress) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                        Spacer(modifier = GlanceModifier.width(6.dp))
                        Text(
                            text = "• ${item.room}",
                            maxLines = 1,
                            style = TextStyle(
                                color = androidx.glance.unit.ColorProvider(roomColor),
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }
        }
    }

    private fun parseTimeInterval(timeStr: String): Pair<Int, Int>? {
        return try {
            val parts = timeStr.split("-")
            if (parts.size != 2) return null
            val startParts = parts[0].trim().split(":")
            val endParts = parts[1].trim().split(":")
            if (startParts.size != 2 || endParts.size != 2) return null
            val startMin = startParts[0].toInt() * 60 + startParts[1].toInt()
            val endMin = endParts[0].toInt() * 60 + endParts[1].toInt()
            Pair(startMin, endMin)
        } catch (_: Exception) {
            null
        }
    }

    private fun parseColor(hex: String): Color {
        return try {
            Color(android.graphics.Color.parseColor(hex))
        } catch (_: Exception) {
            Color(0xFF10B981)
        }
    }

    companion object {
        fun defaultSchedule(): List<WidgetScheduleDay> {
            return listOf(
                WidgetScheduleDay(
                    dayNum = 1,
                    dayName = "Lunes",
                    classes = listOf(
                        WidgetClassItem("10:01 - 12:10", "DSY1102", "Desarrollo Orientado a Objetos", "Sala 1208 Lab PC", "David Andrés Azúa", "#F97316"),
                        WidgetClassItem("13:41 - 16:40", "BDY1103", "Taller de Base de Datos", "Sala 1207 Lab PC", "Gilda Orellana Guzmán", "#EF4444")
                    )
                ),
                WidgetScheduleDay(
                    dayNum = 2,
                    dayName = "Martes",
                    classes = listOf(
                        WidgetClassItem("12:11 - 13:40", "EAY4730", "Ética para el Trabajo", "Sala 1305 Proyectos", "Nicolás Matías Fuentes", "#4F46E5"),
                        WidgetClassItem("13:41 - 15:50", "DSY1104", "Desarrollo Fullstack II", "Sala 1208 Lab PC", "David Andrés Azúa", "#0284C7"),
                        WidgetClassItem("16:01 - 17:20", "MAT4141", "Estadística Descriptiva", "Sala 1207 Lab PC", "Francisco Saavedra", "#9333EA")
                    )
                ),
                WidgetScheduleDay(
                    dayNum = 3,
                    dayName = "Miércoles",
                    classes = listOf(
                        WidgetClassItem("10:01 - 11:20", "DSY1102", "Desarrollo Orientado a Objetos", "Sala 1208 Lab PC", "David Andrés Azúa", "#F97316"),
                        WidgetClassItem("13:41 - 15:50", "DSY1104", "Desarrollo Fullstack II", "Sala 1208 Lab PC", "David Andrés Azúa", "#0284C7")
                    )
                ),
                WidgetScheduleDay(
                    dayNum = 4,
                    dayName = "Jueves",
                    classes = listOf(
                        WidgetClassItem("13:41 - 15:10", "MAT4141", "Estadística Descriptiva", "Sala 1207 Lab PC", "Francisco Saavedra", "#9333EA"),
                        WidgetClassItem("15:11 - 18:50", "DSY1105", "Desarrollo de Apps Móviles", "Sala 1207 Lab PC", "Gilda Orellana Guzmán", "#10B981")
                    )
                )
            )
        }
    }
}

class WeeklyScheduleWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeeklyScheduleWidget()

    override fun onReceive(context: Context, intent: android.content.Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == android.content.Intent.ACTION_DATE_CHANGED ||
            action == android.content.Intent.ACTION_TIME_CHANGED ||
            action == android.content.Intent.ACTION_TIME_SET ||
            action == android.content.Intent.ACTION_TIMEZONE_CHANGED ||
            action == android.content.Intent.ACTION_USER_PRESENT ||
            action == android.content.Intent.ACTION_SCREEN_ON ||
            action == android.content.Intent.ACTION_BOOT_COMPLETED) {
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                WeeklyScheduleWidget().updateAll(context)
            }
        }
    }
}
