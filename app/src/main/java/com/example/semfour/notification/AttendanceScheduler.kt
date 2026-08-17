package com.example.semfour.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.semfour.data.local.dao.ScheduleDao
import com.example.semfour.data.local.dao.SubjectDao
import com.example.semfour.data.local.entity.ScheduleEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Administrador para programar y cancelar alarmas exactas de recordatorio
 * de asistencia (30 minutos después de iniciar cada clase).
 */
@Singleton
class AttendanceScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scheduleDao: ScheduleDao,
    private val subjectDao: SubjectDao
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isAttendanceRemindersEnabled(): Boolean {
        return prefs.getBoolean(KEY_ATTENDANCE_ENABLED, true)
    }

    fun setAttendanceRemindersEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_ATTENDANCE_ENABLED, enabled).apply()
    }

    /**
     * Programa o reprograma todas las alarmas de asistencia para las clases en la base de datos.
     */
    suspend fun scheduleAllFromDatabase() {
        if (!isAttendanceRemindersEnabled()) {
            cancelAllFromDatabase()
            return
        }

        val schedules = scheduleDao.getAllSchedule().firstOrNull() ?: emptyList()
        val subjects = subjectDao.getAllSubjects().firstOrNull() ?: emptyList()
        val subjectMap = subjects.associateBy { it.id }

        for (scheduleItem in schedules) {
            val subjectName = subjectMap[scheduleItem.subjectId]?.nombre ?: "Clase"
            scheduleAttendanceAlarm(scheduleItem, subjectName)
        }
    }

    /**
     * Cancela todas las alarmas programadas.
     */
    suspend fun cancelAllFromDatabase() {
        val schedules = scheduleDao.getAllSchedule().firstOrNull() ?: emptyList()
        for (scheduleItem in schedules) {
            cancelAttendanceAlarm(scheduleItem)
        }
    }

    /**
     * Programa la alarma exacta para una clase específica exactamente a los 30 min de haber iniciado.
     */
    fun scheduleAttendanceAlarm(scheduleItem: ScheduleEntity, subjectName: String) {
        if (!isAttendanceRemindersEnabled()) return

        val triggerTimeMs = calculateNextTriggerTime(scheduleItem.dayOfWeek, scheduleItem.startTime)

        val intent = Intent(context, AttendanceNotificationReceiver::class.java).apply {
            putExtra(EXTRA_SCHEDULE_ID, scheduleItem.id)
            putExtra(EXTRA_SUBJECT_ID, scheduleItem.subjectId)
            putExtra(EXTRA_SUBJECT_NAME, subjectName)
            putExtra(EXTRA_ROOM, scheduleItem.room)
            putExtra(EXTRA_START_TIME, scheduleItem.startTime)
            putExtra(EXTRA_DAY_OF_WEEK, scheduleItem.dayOfWeek)
        }

        val requestCode = scheduleItem.id.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerTimeMs,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // En Android 12+ si no cuenta con permiso de alarma exacta, se usa set
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMs,
                pendingIntent
            )
        }
    }

    /**
     * Cancela la alarma de una clase.
     */
    fun cancelAttendanceAlarm(scheduleItem: ScheduleEntity) {
        val intent = Intent(context, AttendanceNotificationReceiver::class.java)
        val requestCode = scheduleItem.id.hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    companion object {
        const val PREFS_NAME = "semfour_notification_prefs"
        const val KEY_ATTENDANCE_ENABLED = "attendance_reminders_enabled"

        const val EXTRA_SCHEDULE_ID = "extra_schedule_id"
        const val EXTRA_SUBJECT_ID = "extra_subject_id"
        const val EXTRA_SUBJECT_NAME = "extra_subject_name"
        const val EXTRA_ROOM = "extra_room"
        const val EXTRA_START_TIME = "extra_start_time"
        const val EXTRA_DAY_OF_WEEK = "extra_day_of_week"

        /**
         * Calcula el epoch millisecond del próximo día y hora (+30 minutos)
         * para la clase indicada.
         */
        fun calculateNextTriggerTime(dayOfWeek: Int, startTime: String): Long {
            val parts = startTime.split(":")
            val startHour = parts.getOrNull(0)?.toIntOrNull() ?: 10
            val startMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0

            // Sumar 30 minutos al inicio de la clase
            var targetHour = startHour
            var targetMinute = startMinute + 30
            if (targetMinute >= 60) {
                targetHour += targetMinute / 60
                targetMinute %= 60
            }

            val calendarDay = when (dayOfWeek) {
                1 -> Calendar.MONDAY
                2 -> Calendar.TUESDAY
                3 -> Calendar.WEDNESDAY
                4 -> Calendar.THURSDAY
                5 -> Calendar.FRIDAY
                6 -> Calendar.SATURDAY
                7 -> Calendar.SUNDAY
                else -> Calendar.MONDAY
            }

            val now = Calendar.getInstance()
            val target = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, calendarDay)
                set(Calendar.HOUR_OF_DAY, targetHour)
                set(Calendar.MINUTE, targetMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Si el momento calculado ya pasó esta semana, programar para la próxima semana (+7 días)
            if (target.timeInMillis <= now.timeInMillis) {
                target.add(Calendar.WEEK_OF_YEAR, 1)
            }

            return target.timeInMillis
        }
    }
}
