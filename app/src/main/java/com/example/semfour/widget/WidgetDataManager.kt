package com.example.semfour.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import com.example.semfour.domain.algorithm.PrioritizedTopic
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

/**
 * Gestor para actualizar los datos en los widgets de la pantalla de inicio (Glance).
 */
@Singleton
class WidgetDataManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun updatePriorityWidget(
        prioritizedTopic: PrioritizedTopic?,
        subjectName: String,
        nextClass: String = "",
        nextClassRoom: String = ""
    ) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(StudyPriorityWidget::class.java)

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { prefs ->
                if (prioritizedTopic != null) {
                    prefs[StudyPriorityWidget.KEY_TOPIC_NAME] = prioritizedTopic.topic.nombre
                    prefs[StudyPriorityWidget.KEY_SUBJECT_NAME] = subjectName
                    prefs[StudyPriorityWidget.KEY_PRIORITY_SCORE] = (prioritizedTopic.score * 100).roundToInt().coerceIn(0, 100)
                    prefs[StudyPriorityWidget.KEY_CONFIDENCE] = prioritizedTopic.topic.nivelConfianza
                }
                prefs[StudyPriorityWidget.KEY_NEXT_CLASS] = nextClass
                prefs[StudyPriorityWidget.KEY_NEXT_CLASS_ROOM] = nextClassRoom
            }
            StudyPriorityWidget().update(context, glanceId)
        }
    }

    suspend fun updateStreakWidget(
        streakDays: Int,
        todayMinutes: Int,
        weeklyMinutes: Int
    ) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(StudyStreakWidget::class.java)

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { prefs ->
                prefs[StudyStreakWidget.KEY_STREAK_DAYS] = streakDays
                prefs[StudyStreakWidget.KEY_TODAY_MINUTES] = todayMinutes
                prefs[StudyStreakWidget.KEY_WEEKLY_MINUTES] = weeklyMinutes
            }
            StudyStreakWidget().update(context, glanceId)
        }
    }

    suspend fun updateWeeklyScheduleWidget() {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(WeeklyScheduleWidget::class.java)
        glanceIds.forEach { glanceId ->
            WeeklyScheduleWidget().update(context, glanceId)
        }
    }

    suspend fun updateDailyPlanWidget(
        week: Int,
        dayOfWeek: Int,
        tasks: List<com.example.semfour.data.local.entity.DailyPlanTaskEntity>,
        subjects: List<com.example.semfour.data.local.entity.SubjectEntity>
    ) {
        val manager = GlanceAppWidgetManager(context)
        val glanceIds = manager.getGlanceIds(DailyStudyPlanWidget::class.java)
        if (glanceIds.isEmpty()) return

        val subjectMap = subjects.associateBy { it.id }

        glanceIds.forEach { glanceId ->
            updateAppWidgetState(context, glanceId) { prefs ->
                prefs[DailyStudyPlanWidget.KEY_WEEK_NUMBER] = week
                prefs[DailyStudyPlanWidget.KEY_DAY_OF_WEEK] = dayOfWeek
                prefs[DailyStudyPlanWidget.KEY_DAY_NAME] = DailyStudyPlanWidget.getDayName(dayOfWeek)
                prefs[DailyStudyPlanWidget.KEY_TASK_COUNT] = tasks.size

                tasks.forEachIndexed { index, task ->
                    val subject = subjectMap[task.subjectId]
                    val code = subject?.codigo ?: if (task.subjectId == "sub_consolidacion") "CIERRE" else ""
                    val name = subject?.nombre ?: if (task.subjectId == "sub_consolidacion") "Consolidación" else "Asignatura"
                    val color = subject?.color ?: if (task.subjectId == "sub_consolidacion") "#8B5CF6" else "#3B82F6"

                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_id")] = task.id
                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_code")] = code
                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_name")] = name
                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_color")] = color
                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_type")] = task.taskType
                    prefs[androidx.datastore.preferences.core.stringPreferencesKey("task_${index}_file")] = task.notebookFile
                    prefs[androidx.datastore.preferences.core.booleanPreferencesKey("task_${index}_done")] = task.isCompleted
                }
            }
            DailyStudyPlanWidget().update(context, glanceId)
        }
    }
}
