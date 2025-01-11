package com.opbengalas.birthdayapp.screens.BirthdayScreen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ContactDetailScreen(
    navController: NavHostController,
    birthdayViewModel: BirthdayViewModel,
    id: Int
) {
    val contacts by birthdayViewModel.listContact.collectAsState()

    if (contacts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val contact = contacts.find { it.id == id }
    if (contact != null) {
        DetailContent(
            contact = contact,
            navController = navController,
            birthdayViewModel = birthdayViewModel
        )
    } else {
        Text(
            text = "Contact Detail not found with ID: $id",
            modifier = Modifier.fillMaxSize(),
            color = Color.Red,
            textAlign = TextAlign.Center
        )
        Log.e("ContactDetailScreen", "Contact Detail not found with ID: $id")
    }
}

@Composable
private fun DetailContent(
    contact: Contact,
    navController: NavHostController,
    birthdayViewModel: BirthdayViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = contact.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Date:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = contact.birthdayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
        }
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Description:",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = contact.description,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { navController.navigate("edit_contact/${contact.id}") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
        ) {
            Text("Editar", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
        Button(
            onClick = {
                if (contact.id != 0) {
                    birthdayViewModel.deleteContact(contact)
                    navController.popBackStack()
                } else {
                    Log.e("DeleteError", "Contact ID is invalid")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Eliminate", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
    }
}

@Composable
fun EditContactScreen(
    navController: NavHostController,
    birthdayViewModel: BirthdayViewModel,
    id: Int
) {
    val contact = birthdayViewModel.listContact.value.find { it.id == id } ?: return

    val (name, setName) = remember { mutableStateOf(contact.name) }
    val (date, setDate) = remember {
        mutableStateOf(
            contact.birthdayDate.format(
                DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy"
                )
            )
        )
    }
    val (description, setDescription) = remember { mutableStateOf(contact.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = setName,
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = date,
            onValueChange = setDate,
            label = { Text("Birthday Date") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = setDescription,
            label = { Text("Description") }
        )
        Button(
            onClick = {
                val updatedContact = contact.copy(
                    name = name,
                    birthdayDate = LocalDate
                        .parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    description = description
                )
                birthdayViewModel.updateContact(updatedContact)
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar", color = Color.White)
        }
    }
}



