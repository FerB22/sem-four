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
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.semfour.MainActivity

/**
 * Widget 1: Recomendación de Estudio con Algoritmo SM-2 y Próxima Clase (Light & Clean Theme).
 */
class StudyPriorityWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = currentState<Preferences>()
                val topicName = prefs[KEY_TOPIC_NAME] ?: "1.1 Ecosistema móvil: Kotlin y Compose"
                val subjectName = prefs[KEY_SUBJECT_NAME] ?: "Desarrollo Móvil"
                val priorityScore = prefs[KEY_PRIORITY_SCORE] ?: 95
                val confidence = prefs[KEY_CONFIDENCE] ?: 3
                val nextClass = prefs[KEY_NEXT_CLASS] ?: ""
                val nextClassRoom = prefs[KEY_NEXT_CLASS_ROOM] ?: ""

                PriorityWidgetContent(
                    topicName = topicName,
                    subjectName = subjectName,
                    priorityScore = priorityScore,
                    confidence = confidence,
                    nextClass = nextClass,
                    nextClassRoom = nextClassRoom
                )
            }
        }
    }

    @Composable
    private fun PriorityWidgetContent(
        topicName: String,
        subjectName: String,
        priorityScore: Int,
        confidence: Int,
        nextClass: String,
        nextClassRoom: String
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .cornerRadius(18.dp)
                .padding(14.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🧠 RECOMENDACIÓN SM-2 ($priorityScore %)",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF0F172A)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = GlanceModifier.defaultWeight())
                    Text(
                        text = "⚡ $confidence/5 ⭐",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFFF97316)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Subject Pill
                Box(
                    modifier = GlanceModifier
                        .background(Color(0xFFF1F5F9))
                        .cornerRadius(6.dp)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = subjectName,
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF0284C7)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = GlanceModifier.height(6.dp))

                // Topic Title
                Text(
                    text = topicName,
                    maxLines = 2,
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(Color(0xFF0F172A)),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = GlanceModifier.defaultWeight())

                // Bottom: Action Button
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A))
                        .cornerRadius(8.dp)
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ Toca para estudiar 5 min",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFFFFFFFF)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

    companion object {
        val KEY_TOPIC_NAME = stringPreferencesKey("topic_name")
        val KEY_SUBJECT_NAME = stringPreferencesKey("subject_name")
        val KEY_PRIORITY_SCORE = intPreferencesKey("priority_score")
        val KEY_CONFIDENCE = intPreferencesKey("confidence")
        val KEY_NEXT_CLASS = stringPreferencesKey("next_class")
        val KEY_NEXT_CLASS_ROOM = stringPreferencesKey("next_class_room")
    }
}

class StudyPriorityWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StudyPriorityWidget()
}
