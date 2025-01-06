package com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactDetailScreen(
    name: String,
    date: String,
    description: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val decodedName = Uri.decode(name)
    val decodedDate = Uri.decode(date)
    val decodedDescription = Uri.decode(description)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$decodedName",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Date:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = decodedDate,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Description:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = decodedDescription,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onEdit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Editar", style = MaterialTheme.typography.bodyLarge)
        }
        Button(
            onClick = onDelete,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Eliminar", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}

