package com.example.semfour.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.semfour.data.local.entity.EvaluationEntity
import com.example.semfour.data.local.entity.SubjectEntity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Diálogo pop-up completo para crear o editar evaluaciones:
 * Permite seleccionar la asignatura, el nombre, la fecha (o marcar como por definir),
 * ponderación y tipo de evaluación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationFormDialog(
    evaluationToEdit: EvaluationEntity? = null,
    subjects: List<SubjectEntity>,
    onSave: (EvaluationEntity) -> Unit,
    onDelete: ((EvaluationEntity) -> Unit)? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isEditing = evaluationToEdit != null

    var selectedSubjectId by remember {
        mutableStateOf(
            evaluationToEdit?.subjectId ?: subjects.firstOrNull()?.id ?: "sub_poo"
        )
    }
    var evaluationName by remember {
        mutableStateOf(evaluationToEdit?.nombre ?: "")
    }
    var fechaEval by remember {
        mutableStateOf(evaluationToEdit?.fechaEval ?: 0L)
    }
    var ponderacionText by remember {
        mutableStateOf(
            if (evaluationToEdit != null) "${(evaluationToEdit.ponderacion * 100).toInt()}" else "25"
        )
    }
    var tipo by remember {
        mutableStateOf(evaluationToEdit?.tipo ?: "CERTAMEN")
    }

    var subjectDropdownExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val dateFormat = remember {
        SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "ES"))
    }

    val selectedSubject = subjects.find { it.id == selectedSubjectId }

    var showDatePickerDialog by remember { mutableStateOf(false) }

    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (fechaEval > 0L) fechaEval else System.currentTimeMillis()
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { utcMillis ->
                            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            cal.timeInMillis = utcMillis
                            val localCal = Calendar.getInstance()
                            localCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 10, 0, 0)
                            localCal.set(Calendar.MILLISECOND, 0)
                            fechaEval = localCal.timeInMillis
                        }
                        showDatePickerDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = {
                            fechaEval = 0L
                            showDatePickerDialog = false
                        }
                    ) {
                        Text("Sin fecha", color = MaterialTheme.colorScheme.error)
                    }
                    OutlinedButton(
                        onClick = { showDatePickerDialog = false },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Cancelar")
                    }
                }
            },
            shape = RoundedCornerShape(24.dp),
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF0F172A),
                    headlineContentColor = Color(0xFF0F172A),
                    weekdayContentColor = Color(0xFF64748B),
                    subheadContentColor = Color(0xFF0F172A),
                    yearContentColor = Color(0xFF0F172A),
                    currentYearContentColor = Color(0xFF0F172A),
                    selectedYearContentColor = Color.White,
                    selectedYearContainerColor = Color(0xFF0F172A),
                    dayContentColor = Color(0xFF0F172A),
                    selectedDayContentColor = Color.White,
                    selectedDayContainerColor = Color(0xFF0F172A),
                    todayContentColor = Color(0xFF0F172A),
                    todayDateBorderColor = Color(0xFF0F172A)
                )
            )
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isEditing) "Editar evaluación" else "Nueva evaluación",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 1. Selector de Asignatura
                Text(
                    text = "Asignatura",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = subjectDropdownExpanded,
                    onExpandedChange = { subjectDropdownExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedSubject?.nombre ?: "Seleccionar asignatura",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectDropdownExpanded) },
                        leadingIcon = {
                            val color = selectedSubject?.let { parseHexColor(it.color) } ?: MaterialTheme.colorScheme.primary
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(color)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = subjectDropdownExpanded,
                        onDismissRequest = { subjectDropdownExpanded = false },
                        containerColor = Color.White
                    ) {
                        subjects.forEach { subj ->
                            val subjColor = parseHexColor(subj.color)
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(subjColor)
                                        )
                                        Spacer(Modifier.width(10.dp))
                                        Column {
                                            Text(subj.nombre, fontWeight = FontWeight.SemiBold)
                                            Text(subj.codigo, style = MaterialTheme.typography.labelSmall, color = subjColor)
                                        }
                                    }
                                },
                                onClick = {
                                    selectedSubjectId = subj.id
                                    subjectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2. Nombre de la Evaluación
                Text(
                    text = "Nombre de la evaluación",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = evaluationName,
                    onValueChange = { evaluationName = it },
                    placeholder = { Text("Ej. Certamen 1, Entrega de proyecto...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                // 3. Fecha de la Evaluación
                Text(
                    text = "Fecha de evaluación",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(6.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePickerDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (fechaEval > 0L) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = if (fechaEval > 0L) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (fechaEval > 0L) dateFormat.format(Date(fechaEval)).replaceFirstChar { it.uppercase() }
                                else "Por definir (Sin fecha programada)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (fechaEval > 0L) FontWeight.Bold else FontWeight.Normal,
                                color = if (fechaEval > 0L) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Toca para cambiar la fecha",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (fechaEval > 0L) {
                            TextButton(onClick = { fechaEval = 0L }) {
                                Text("Quitar fecha", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 4. Ponderación (%) y Tipo
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ponderación (%)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = ponderacionText,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 3) {
                                    ponderacionText = input
                                }
                            },
                            suffix = { Text("%") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tipo",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            FilterChip(
                                selected = tipo == "CERTAMEN",
                                onClick = { tipo = "CERTAMEN" },
                                label = { Text("Certamen", style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(6.dp))
                            FilterChip(
                                selected = tipo == "ENTREGA",
                                onClick = { tipo = "ENTREGA" },
                                label = { Text("Entrega", style = MaterialTheme.typography.labelSmall) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Botones Guardar / Eliminar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (evaluationToEdit != null && onDelete != null) {
                        TextButton(
                            onClick = {
                                onDelete(evaluationToEdit)
                                onDismiss()
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Eliminar")
                        }
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                if (evaluationName.isBlank()) {
                                    errorMessage = "Por favor ingresa un nombre para la evaluación."
                                    return@Button
                                }
                                val pondInt = ponderacionText.toIntOrNull() ?: 25
                                val pondFloat = (pondInt / 100f).coerceIn(0.05f, 1.0f)
                                val evalId = evaluationToEdit?.id ?: "eval_custom_${System.currentTimeMillis()}"

                                val entity = EvaluationEntity(
                                    id = evalId,
                                    subjectId = selectedSubjectId,
                                    nombre = evaluationName.trim(),
                                    fechaEval = fechaEval,
                                    ponderacion = pondFloat,
                                    tipo = tipo,
                                    completada = evaluationToEdit?.completada ?: false
                                )
                                onSave(entity)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(if (isEditing) "Guardar cambios" else "Crear evaluación", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
