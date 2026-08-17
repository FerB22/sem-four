package com.example.semfour.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Colores Vivos de Asignaturas ─────────────────────────────────────────────
val ColorMoviles = Color(0xFF10B981)      // Android Emerald Green
val ColorPOO = Color(0xFFF97316)          // Java Vibrant Orange
val ColorFullstack = Color(0xFF0284C7)    // Web Sky Blue
val ColorBD = Color(0xFFEF4444)           // Database Red
val ColorEstadistica = Color(0xFF9333EA)  // Statistics Purple
val ColorEtica = Color(0xFF4F46E5)        // Ethics Indigo

// ── Paleta Blanco Puro & Negro Nítido (Clean White & Deep Black) ─────────────
private val PureWhiteScheme = lightColorScheme(
    primary = Color(0xFF0F172A),               // Negro carbón profundo
    onPrimary = Color(0xFFFFFFFF),             // Blanco sobre negro
    primaryContainer = Color(0xFFF1F5F9),      // Contenedores gris perla suave
    onPrimaryContainer = Color(0xFF0F172A),    // Texto negro
    secondary = Color(0xFF1E293B),             // Gris oscuro elegante
    onSecondary = Color(0xFFFFFFFF),           // Blanco
    secondaryContainer = Color(0xFFF8FAFC),    // Blanco grisáceo suave
    onSecondaryContainer = Color(0xFF0F172A),  // Texto negro
    tertiary = Color(0xFF334155),              // Detalle slate
    onTertiary = Color(0xFFFFFFFF),
    background = Color(0xFFFFFFFF),            // Fondo 100% BLANCO PURO
    onBackground = Color(0xFF0F172A),          // Palabras y títulos en NEGRO
    surface = Color(0xFFFFFFFF),               // Superficie 100% BLANCA
    onSurface = Color(0xFF0F172A),             // Texto y detalles en NEGRO
    surfaceVariant = Color(0xFFF8FAFC),        // Fondo de tarjetas gris neutro claro
    onSurfaceVariant = Color(0xFF475569),      // Subtítulos en gris pizarra
    surfaceTint = Color.Transparent,           // CERO TINTE ROSADO/LAVANDA DE MATERIAL 3
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFFFFFFFF),
    surfaceContainerHigh = Color(0xFFF8FAFC),
    surfaceContainerHighest = Color(0xFFF1F5F9),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFF1F5F9),
    outline = Color(0xFFE2E8F0),               // Bordes sutiles ultra limpios
    outlineVariant = Color(0xFFF1F5F9),        // Separadores claros
    error = Color(0xFFDC2626),                 // Rojo de error
    onError = Color(0xFFFFFFFF)
)

/**
 * Tema SemFour: Diseño minimalista en Blanco y Negro,
 * manteniendo los colores vibrantes característicos para cada asignatura.
 */
@Composable
fun SemFourTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = PureWhiteScheme,
        content = content
    )
}
