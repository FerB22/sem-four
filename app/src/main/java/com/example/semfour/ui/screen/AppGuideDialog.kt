package com.example.semfour.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Diálogo instructivo interactivo de bienvenida y preguntas frecuentes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppGuideDialog(
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("🚀 Funciones", "📚 Materias", "🧠 Estudio SM-2", "❓ Preguntas")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                "Instructivo de SemFour",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Guía completa y preguntas frecuentes",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                // Selector de pestañas
                PrimaryScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Contenido por pestaña
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                ) {
                    when (selectedTab) {
                        0 -> FunctionsGuideContent()
                        1 -> SubjectsGuideContent()
                        2 -> StudyMethodGuideContent()
                        3 -> FaqGuideContent()
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("¡Entendido, a estudiar!", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ── PESTAÑA 1: FUNCIONES DE LA APP ────────────────────────────────────────────

@Composable
private fun FunctionsGuideContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "¿Qué puedes hacer dentro de SemFour?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "SemFour es tu centro integral de estudio optimizado con algoritmos de repetición espaciada y horario en tiempo real.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            GuideFeatureCard(
                icon = Icons.Default.Home,
                iconColor = Color(0xFF3DDC84),
                title = "1. Pestaña «Hoy» (Dashboard)",
                description = "Muestra tu tema de mayor prioridad calculado por el algoritmo SM-2, tus clases de hoy, próximas evaluaciones y una cola rápida para estudiar en cualquier momento libre."
            )
        }

        item {
            GuideFeatureCard(
                icon = Icons.Default.CalendarMonth,
                iconColor = Color(0xFF61DAFB),
                title = "2. Pestaña «Horario»",
                description = "Visualiza tu horario semanal con salas de computación (LAB PC), profesores asignados y filtros rápidos por día de lunes a viernes."
            )
        }

        item {
            GuideFeatureCard(
                icon = Icons.Default.School,
                iconColor = Color(0xFFF89820),
                title = "3. Pestaña «Materias»",
                description = "Explora el catálogo de asignaturas del semestre, califica tu nivel de dominio (1 a 5) en cada tema y añade apuntes o enlaces de estudio."
            )
        }

        item {
            GuideFeatureCard(
                icon = Icons.Default.LocalFireDepartment,
                iconColor = Color(0xFFFF6B6B),
                title = "4. Pestaña «Hábitos»",
                description = "Lleva el registro de tu racha diaria de estudio, mapa de calor de 90 días estilo GitHub y estadísticas de minutos estudiados durante la semana."
            )
        }

        item {
            GuideFeatureCard(
                icon = Icons.Default.Settings,
                iconColor = Color(0xFF8E24AA),
                title = "5. Pestaña «Ajustes»",
                description = "Activa o desactiva las Experiencias de Aprendizaje 2 y 3, añade Inglés Intermedio 1, respalda tus datos con Google Drive y descarga actualizaciones del APK."
            )
        }
    }
}

// ── PESTAÑA 2: ASIGNATURAS DEL 4.º SEMESTRE ───────────────────────────────────

@Composable
private fun SubjectsGuideContent() {
    val subjectsInfo = listOf(
        SubjectGuideItem("Desarrollo Orientado a Objetos", "DSY1102", "#F89820", "POO en Java, interfaz gráfica JavaFX, patrón arquitectónico MVC, persistencia en JSON y conectividad con base de datos relacional JDBC."),
        SubjectGuideItem("Taller de Base de Datos", "BDY1103", "#E53935", "PL/SQL avanzado (procedimientos, paquetes, disparadores y cursores), construcción de aplicaciones con Oracle APEX y modelado documental NoSQL con MongoDB."),
        SubjectGuideItem("Desarrollo de Aplicaciones Móviles", "DSY1105", "#3DDC84", "Desarrollo moderno en Android con Kotlin, interfaz reactiva con Jetpack Compose, arquitectura MVVM, persistencia local con Room y consumo de APIs REST."),
        SubjectGuideItem("Desarrollo Fullstack II", "DSY1104", "#61DAFB", "Frontend con React, Vite y Bootstrap, pruebas unitarias automatizadas con Jasmine y Karma, arquitectura de microservicios backend y despliegue en nube AWS."),
        SubjectGuideItem("Estadística Descriptiva", "MAT4141", "#8E24AA", "Análisis y procesamiento de datos con Python y Pandas, medidas de tendencia central y dispersión, regresión lineal y distribuciones de probabilidad normal."),
        SubjectGuideItem("Ética para el Trabajo", "EAY4730", "#3949AB", "Reflexión ética en el entorno profesional y tecnológico, análisis de dilemas morales organizacionales, deontología informática y privacidad de datos."),
        SubjectGuideItem("Inglés Intermedio 1 (Opcional)", "INI4111", "#00897B", "Vocabulario tecnológico en inglés, redacción de correos técnicos y reportes, lectura de documentación de APIs, entrevistas laborales y reuniones ágiles Scrum.")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Asignaturas y Experiencias de Aprendizaje",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "La malla del 4.º semestre está dividida en Experiencias de Aprendizaje (EA1, EA2 y EA3). Puedes activar o desactivar las experiencias según tu avance en el semestre desde Ajustes.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(subjectsInfo) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(54.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(parseHexColor(item.color))
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(item.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(6.dp))
                            Text(item.code, style = MaterialTheme.typography.labelSmall, color = parseHexColor(item.color), fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(item.desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

private data class SubjectGuideItem(val name: String, val code: String, val color: String, val desc: String)

// ── PESTAÑA 3: MÉTODO DE ESTUDIO SM-2 ─────────────────────────────────────────

@Composable
private fun StudyMethodGuideContent() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                "¿Cómo optimiza tu estudio SemFour?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "La app utiliza el algoritmo científico SuperMemo-2 (SM-2) para programar repasos exactamente antes de que olvides un concepto.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fórmula del Score de Prioridad:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("• 50 % Repetición Espaciada: Temas con fecha de repaso vencida suben a la cima.", style = MaterialTheme.typography.bodySmall)
                    Text("• 30 % Confianza Inversa: Temas donde tienes nivel 1 o 2 se priorizan.", style = MaterialTheme.typography.bodySmall)
                    Text("• 20 % Urgencia de Evaluación: Materias con evaluaciones cercanas obtienen mayor peso.", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item {
            Text("Tipos de Sesión de Estudio:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("⚡ Micro (5 min)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(4.dp))
                        Text("Ideal para repasar conceptos rápidos entre clases o en el transporte.", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("🍅 Pomodoro (25 min)", fontWeight = FontWeight.Bold, color = Color(0xFFFF6B6B))
                        Spacer(Modifier.height(4.dp))
                        Text("Bloque de concentración profunda para resolver ejercicios y código.", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(Modifier.padding(14.dp)) {
                    Text("Al terminar una sesión:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Califica tu nivel de retención del 1 al 5. El algoritmo recalculará el intervalo de días óptimo para tu próximo repaso automático.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ── PESTAÑA 4: PREGUNTAS FRECUENTES (FAQ) ─────────────────────────────────────

@Composable
private fun FaqGuideContent() {
    val faqs = listOf(
        FaqItem(
            "¿Por qué algunas evaluaciones dicen «Por definir» en gris?",
            "Corresponden a evaluaciones oficiales de tu plan de estudios cuya fecha exacta aún no ha sido calendarizada por el profesor. Puedes tocar cualquier tarjeta de evaluación para abrir el calendario y asignarle fecha en cuanto te la informen."
        ),
        FaqItem(
            "¿Cómo activo o desactivo las Experiencias 2 y 3 o Inglés?",
            "Ve a la pestaña Ajustes (el último icono de la barra inferior). En la sección «Temario y asignaturas», activa o desactiva los interruptores. La base de datos añadirá o removerá los temas y evaluaciones automáticamente."
        ),
        FaqItem(
            "¿Cómo añado los widgets de la app a mi pantalla de inicio?",
            "1. Ve a la pantalla de inicio de tu teléfono y mantén presionado un espacio vacío.\n2. Toca «Widgets».\n3. Busca «SemFour» y elige entre: Tema prioritario, Racha de fuego u Horario semanal.\n4. Arrástralo a donde prefieras."
        ),
        FaqItem(
            "¿Puedo usar la aplicación sin conexión a internet?",
            "Sí, 100 %. SemFour está diseñada con arquitectura Offline-First. Toda tu información de estudio, evaluaciones y horarios reside en la base de datos local de tu teléfono."
        ),
        FaqItem(
            "¿Cómo funciona la sincronización con Google Drive?",
            "En Ajustes, inicia sesión con tu cuenta de Google. Podrás sincronizar manualmente o dejar que la app guarde una copia de seguridad cifrada en tu Google Drive personal para no perder tus datos al cambiar de teléfono."
        )
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                "Preguntas frecuentes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "Toca cada pregunta para desplegar la respuesta detallada.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        items(faqs) { faq ->
            FaqAccordionCard(item = faq)
        }
    }
}

private data class FaqItem(val question: String, val answer: String)

@Composable
private fun FaqAccordionCard(item: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.question,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.answer,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight * 1.25f
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideFeatureCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
