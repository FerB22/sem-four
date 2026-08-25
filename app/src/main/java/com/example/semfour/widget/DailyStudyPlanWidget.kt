@file:Suppress("RestrictedApi")

package com.example.semfour.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.datastore.preferences.core.*
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
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.launch
import com.example.semfour.MainActivity
import com.example.semfour.data.local.StudyPlanCatalog
import com.example.semfour.ui.viewmodel.DashboardViewModel

private fun glanceColor(color: Color): ColorProvider = ColorProvider(color)

data class WidgetPlanTask(
    val id: String,
    val subjectCode: String,
    val subjectName: String,
    val colorHex: String,
    val taskType: String,
    val notebookFile: String,
    val isCompleted: Boolean
)

/**
 * Widget 4: Cuadernos y Plan de Estudio Diario (Semana y Día actual).
 * Muestra qué archivo .ipynb y qué materia corresponde estudiar hoy.
 */
class DailyStudyPlanWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = currentState<Preferences>()
                val week = prefs[KEY_WEEK_NUMBER] ?: 1
                val liveDayOfWeek = DashboardViewModel.getDayOfWeekIndex().coerceIn(1, 5)
                val savedDay = prefs[KEY_DAY_OF_WEEK]
                val dayOfWeek = if (savedDay != null && savedDay == liveDayOfWeek) savedDay else liveDayOfWeek
                val dayName = getDayName(dayOfWeek)

                // Cargar tareas desde prefs o catálogo por defecto si el día coincide
                val taskCount = prefs[KEY_TASK_COUNT] ?: -1
                val tasks = if (savedDay == liveDayOfWeek && taskCount >= 0) {
                    (0 until taskCount).map { i ->
                        WidgetPlanTask(
                            id = prefs[stringPreferencesKey("task_${i}_id")] ?: "",
                            subjectCode = prefs[stringPreferencesKey("task_${i}_code")] ?: "",
                            subjectName = prefs[stringPreferencesKey("task_${i}_name")] ?: "Materia",
                            colorHex = prefs[stringPreferencesKey("task_${i}_color")] ?: "#3B82F6",
                            taskType = prefs[stringPreferencesKey("task_${i}_type")] ?: "Estudio",
                            notebookFile = prefs[stringPreferencesKey("task_${i}_file")] ?: "Cuaderno.ipynb",
                            isCompleted = prefs[booleanPreferencesKey("task_${i}_done")] ?: false
                        )
                    }
                } else {
                    getDefaultTasksForDay(week, liveDayOfWeek)
                }

                DailyStudyPlanContent(
                    week = week,
                    dayName = dayName,
                    tasks = tasks
                )
            }
        }
    }

    @Composable
    private fun DailyStudyPlanContent(
        week: Int,
        dayName: String,
        tasks: List<WidgetPlanTask>
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .cornerRadius(16.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp)
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
                        text = "🎯 REPASO DE HOY",
                        style = TextStyle(
                            color = glanceColor(Color(0xFF0F172A)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    // Pill de Semana y Día
                    Box(
                        modifier = GlanceModifier
                            .background(Color(0xFF0F172A))
                            .cornerRadius(6.dp)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Semana $week • $dayName",
                            style = TextStyle(
                                color = glanceColor(Color(0xFFFFFFFF)),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(4.dp))

                // Lista de Cuadernos .ipynb a estudiar hoy (Column directa para máxima compatibilidad con todos los launchers)
                if (tasks.isNotEmpty()) {
                    Column(
                        modifier = GlanceModifier.fillMaxWidth()
                    ) {
                        tasks.take(2).forEachIndexed { index, task ->
                            if (index > 0) {
                                Spacer(modifier = GlanceModifier.height(3.dp))
                            }
                            TaskRowItem(task)
                        }
                    }
                } else {
                    Box(
                        modifier = GlanceModifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎉 ¡Sin cuadernos hoy! Día libre.",
                            style = TextStyle(
                                color = glanceColor(Color(0xFF64748B)),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TaskRowItem(task: WidgetPlanTask) {
        val subjectColor = try {
            Color(task.colorHex.toColorInt())
        } catch (_: Exception) {
            Color(0xFF3B82F6)
        }

        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .background(if (task.isCompleted) Color(0xFFF1F5F9) else Color(0xFFF8FAFC))
                .cornerRadius(8.dp)
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra de color de la materia
                Box(
                    modifier = GlanceModifier
                        .width(3.dp)
                        .height(28.dp)
                        .cornerRadius(2.dp)
                        .background(subjectColor)
                ) {}

                Spacer(modifier = GlanceModifier.width(8.dp))

                Column(modifier = GlanceModifier.defaultWeight()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (task.subjectCode.isNotBlank()) {
                            Text(
                                text = task.subjectCode,
                                style = TextStyle(
                                    color = glanceColor(subjectColor),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = GlanceModifier.width(4.dp))
                        }
                        Text(
                            text = task.subjectName,
                            style = TextStyle(
                                color = glanceColor(
                                    if (task.isCompleted) Color(0xFF64748B) else Color(0xFF0F172A)
                                ),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = GlanceModifier.height(2.dp))

                    Text(
                        text = if (task.notebookFile.endsWith(".ipynb")) "🎯 ${task.notebookFile.removeSuffix(".ipynb").replace("_", " ")}" else "🎯 ${task.notebookFile}",
                        style = TextStyle(
                            color = glanceColor(
                                if (task.isCompleted) Color(0xFF94A3B8) else Color(0xFF475569)
                            ),
                            fontSize = 9.sp
                        ),
                        maxLines = 1
                    )
                }

                // Checkbox / Estado
                if (task.isCompleted) {
                    Text(
                        text = "✓",
                        style = TextStyle(
                            color = glanceColor(Color(0xFF22C55E)),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

    companion object {
        val KEY_WEEK_NUMBER = intPreferencesKey("plan_week_number")
        val KEY_DAY_OF_WEEK = intPreferencesKey("plan_day_of_week")
        val KEY_DAY_NAME = stringPreferencesKey("plan_day_name")
        val KEY_TASK_COUNT = intPreferencesKey("plan_task_count")

        fun getDayName(day: Int): String = when (day) {
            1 -> "LUNES"
            2 -> "MARTES"
            3 -> "MIÉRCOLES"
            4 -> "JUEVES"
            5 -> "VIERNES"
            6 -> "SÁBADO"
            7 -> "DOMINGO"
            else -> "LUNES"
        }

        private fun getDefaultTasksForDay(week: Int, day: Int): List<WidgetPlanTask> {
            val all = StudyPlanCatalog.generateAllTasks()
            val dayTasks = all.filter { it.weekNumber == week && it.dayOfWeek == day }
            return dayTasks.map { task ->
                val (code, name, color) = when (task.subjectId) {
                    "sub_poo" -> Triple("DSY1102", "Desarrollo OO (Java)", "#F89820")
                    "sub_bd" -> Triple("BDY1103", "Taller BD (PL/SQL)", "#E53935")
                    "sub_fullstack" -> Triple("DSY1104", "Fullstack II (React)", "#61DAFB")
                    "sub_estadistica" -> Triple("MAT4141", "Estadística (Pandas)", "#43A047")
                    "sub_movil" -> Triple("DSY1105", "Móviles (Kotlin/Compose)", "#00C853")
                    "sub_etica" -> Triple("EAY4730", "Ética para el Trabajo", "#3949AB")
                    else -> Triple("CIERRE", "Consolidación", "#8B5CF6")
                }
                WidgetPlanTask(
                    id = task.id,
                    subjectCode = code,
                    subjectName = name,
                    colorHex = color,
                    taskType = task.taskType,
                    notebookFile = task.notebookFile,
                    isCompleted = task.isCompleted
                )
            }
        }
    }
}

class DailyStudyPlanWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = DailyStudyPlanWidget()

    override fun onReceive(context: Context, intent: android.content.Intent) {
        super.onReceive(context, intent)
        val action = intent.action
        if (action == android.content.Intent.ACTION_DATE_CHANGED ||
            action == android.content.Intent.ACTION_TIME_CHANGED ||
            action == android.content.Intent.ACTION_TIME_TICK ||
            action == android.content.Intent.ACTION_TIMEZONE_CHANGED ||
            action == android.content.Intent.ACTION_USER_PRESENT ||
            action == android.content.Intent.ACTION_SCREEN_ON ||
            action == android.content.Intent.ACTION_BOOT_COMPLETED) {
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                DailyStudyPlanWidget().updateAll(context)
            }
        }
    }
}
