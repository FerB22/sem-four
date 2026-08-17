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
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.semfour.MainActivity

/**
 * Widget 2: Contador de racha diaria y progreso de estudio (Light & Clean Theme).
 */
class StudyStreakWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<*> = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val prefs = currentState<Preferences>()
                val streakDays = prefs[KEY_STREAK_DAYS] ?: 1
                val todayMinutes = prefs[KEY_TODAY_MINUTES] ?: 0
                val weeklyMinutes = prefs[KEY_WEEKLY_MINUTES] ?: 0

                StreakWidgetContent(
                    streakDays = streakDays,
                    todayMinutes = todayMinutes,
                    weeklyMinutes = weeklyMinutes
                )
            }
        }
    }

    @Composable
    private fun StreakWidgetContent(
        streakDays: Int,
        todayMinutes: Int,
        weeklyMinutes: Int
    ) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
                .cornerRadius(18.dp)
                .padding(14.dp)
                .clickable(actionStartActivity<MainActivity>()),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔥",
                    style = TextStyle(fontSize = 24.sp)
                )

                Spacer(modifier = GlanceModifier.height(4.dp))

                Text(
                    text = "$streakDays días",
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(Color(0xFF0F172A)),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Racha de estudio",
                    style = TextStyle(
                        color = androidx.glance.unit.ColorProvider(Color(0xFF64748B)),
                        fontSize = 11.sp
                    )
                )

                Spacer(modifier = GlanceModifier.height(8.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Hoy: $todayMinutes min",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF0284C7)),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = " • Sem.: $weeklyMinutes min",
                        style = TextStyle(
                            color = androidx.glance.unit.ColorProvider(Color(0xFF475569)),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }

    companion object {
        val KEY_STREAK_DAYS = intPreferencesKey("streak_days")
        val KEY_TODAY_MINUTES = intPreferencesKey("today_minutes")
        val KEY_WEEKLY_MINUTES = intPreferencesKey("weekly_minutes")
    }
}

class StudyStreakWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = StudyStreakWidget()
}
