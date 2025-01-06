package com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import com.opbengalas.birthdayapp.ui.theme.Red

@Composable
fun AddContactDialog(
    isOpen: MutableState<Boolean>,
    name: String,
    date: String,
    description: String,
    onNameChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddContact: () -> Unit
) {
    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = { isOpen.value = false },
            title = { Text("Add Contact") },
            text = {
                Column {
                    TextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = date,
                        onValueChange = onDateChange,
                        label = { Text("Date (dd/MM/yyyy)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = description, // Muestra la descripción
                        onValueChange = onDescriptionChange, // Callback para actualizar
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp), // Asegura un cuadro de texto más grande
                        maxLines = 5, // Limita el número de líneas visibles
                        singleLine = false // Permite líneas múltiples
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddContact()
                        isOpen.value = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Red)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { isOpen.value = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Gray)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}