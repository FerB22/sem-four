package com.example.semfour.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.semfour.data.local.entity.QuizQuestionEntity
import java.util.UUID

@Composable
fun AddEditQuestionDialog(
    topicId: String,
    existingQuestion: QuizQuestionEntity? = null,
    onDismiss: () -> Unit,
    onSave: (QuizQuestionEntity) -> Unit
) {
    var theoryContext by remember { mutableStateOf(existingQuestion?.theoryContext ?: "") }
    var questionText by remember { mutableStateOf(existingQuestion?.question ?: "") }
    var optionA by remember { mutableStateOf(existingQuestion?.optionA ?: "") }
    var optionB by remember { mutableStateOf(existingQuestion?.optionB ?: "") }
    var optionC by remember { mutableStateOf(existingQuestion?.optionC ?: "") }
    var optionD by remember { mutableStateOf(existingQuestion?.optionD ?: "") }
    var correctIndex by remember { mutableIntStateOf(existingQuestion?.correctOptionIndex ?: 0) }
    var explanation by remember { mutableStateOf(existingQuestion?.explanation ?: "") }

    var showError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = if (existingQuestion != null) "✏️ Editar Pregunta" else "➕ Nueva Pregunta de Repaso",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Text(
                    text = "Añade una micro-lección y una pregunta de opción múltiple para repasar este tema.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )

                Spacer(Modifier.height(16.dp))

                // Micro-lección / Concepto clave (Opcional)
                OutlinedTextField(
                    value = theoryContext,
                    onValueChange = { theoryContext = it },
                    label = { Text("📖 Concepto Clave a Enseñar (Opcional)") },
                    placeholder = { Text("Ej: En Kotlin, 'val' define una variable inmutable que no puede reasignarse.") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(12.dp))

                // Enunciado
                OutlinedTextField(
                    value = questionText,
                    onValueChange = { questionText = it; showError = false },
                    label = { Text("Enunciado de la pregunta *") },
                    placeholder = { Text("Ej: ¿Cuál es la función del operador '?.' en Kotlin?") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Opciones (Marca la correcta con el botón circular):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF334155)
                )

                Spacer(Modifier.height(8.dp))

                // Opción A
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = correctIndex == 0,
                        onClick = { correctIndex = 0 }
                    )
                    OutlinedTextField(
                        value = optionA,
                        onValueChange = { optionA = it; showError = false },
                        label = { Text("Opción A *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Opción B
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = correctIndex == 1,
                        onClick = { correctIndex = 1 }
                    )
                    OutlinedTextField(
                        value = optionB,
                        onValueChange = { optionB = it; showError = false },
                        label = { Text("Opción B *") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Opción C
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = correctIndex == 2,
                        onClick = { correctIndex = 2 }
                    )
                    OutlinedTextField(
                        value = optionC,
                        onValueChange = { optionC = it },
                        label = { Text("Opción C (Opcional)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Opción D
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = correctIndex == 3,
                        onClick = { correctIndex = 3 }
                    )
                    OutlinedTextField(
                        value = optionD,
                        onValueChange = { optionD = it },
                        label = { Text("Opción D (Opcional)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                // Explicación
                OutlinedTextField(
                    value = explanation,
                    onValueChange = { explanation = it },
                    label = { Text("Explicación didáctica (Opcional)") },
                    placeholder = { Text("Explica por qué esa es la respuesta correcta...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp)
                )

                if (showError) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Por favor completa el enunciado y al menos las opciones A y B.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (questionText.isBlank() || optionA.isBlank() || optionB.isBlank()) {
                                showError = true
                            } else {
                                val questionToSave = (existingQuestion ?: QuizQuestionEntity(
                                    id = "custom_q_" + UUID.randomUUID().toString().take(8),
                                    topicId = topicId,
                                    theoryContext = theoryContext.trim(),
                                    question = questionText.trim(),
                                    optionA = optionA.trim(),
                                    optionB = optionB.trim(),
                                    optionC = optionC.trim(),
                                    optionD = optionD.trim(),
                                    correctOptionIndex = correctIndex,
                                    explanation = explanation.trim(),
                                    isCustom = true
                                )).copy(
                                    theoryContext = theoryContext.trim(),
                                    question = questionText.trim(),
                                    optionA = optionA.trim(),
                                    optionB = optionB.trim(),
                                    optionC = optionC.trim(),
                                    optionD = optionD.trim(),
                                    correctOptionIndex = correctIndex,
                                    explanation = explanation.trim(),
                                    isCustom = true
                                )
                                onSave(questionToSave)
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A))
                    ) {
                        Text("Guardar Pregunta")
                    }
                }
            }
        }
    }
}
