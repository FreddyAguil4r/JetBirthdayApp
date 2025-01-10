package com.opbengalas.birthdayapp.screens.CalendarScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.opbengalas.birthdayapp.models.Contact


@Composable
fun NotificationSettingsScreen(
    contact: Contact,
    onSave: (Contact) -> Unit,
    onCancel: () -> Unit
) {
    var tone by remember { mutableStateOf(contact.notificationTone ?: "") }
    var duration by remember { mutableStateOf(contact.notificationDuration.toString()) }
    var repeat by remember { mutableStateOf(contact.notificationRepeat) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Configurar notificaciones para ${contact.name}", style = MaterialTheme.typography.bodySmall)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = tone,
            onValueChange = { tone = it },
            label = { Text("Ruta del tono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duración (en segundos)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = repeat,
                onCheckedChange = { repeat = it }
            )
            Text(text = "Repetir notificación")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {
                onSave(
                    contact.copy(
                        notificationTone = tone,
                        notificationDuration = duration.toIntOrNull() ?: 0,
                        notificationRepeat = repeat
                    )
                )
            }) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}
