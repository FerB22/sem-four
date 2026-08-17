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
                    prefs[StudyPriorityWidget.KEY_PRIORITY_SCORE] = (prioritizedTopic.score * 100).roundToInt()
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
}
