package com.opbengalas.birthdayapp.screens.BirthdayScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.ui.theme.Red
import java.time.format.DateTimeFormatter

@Composable
fun ContactScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    birthdayViewModel: BirthdayViewModel,
) {
    val birthdays by birthdayViewModel.sortedContacts.collectAsState()
    val name by birthdayViewModel.name.collectAsState()
    val date by birthdayViewModel.date.collectAsState()
    val description by birthdayViewModel.description.collectAsState()
    val isDialogOpen = remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (birthdays.isEmpty()) {
                Text(
                    text = "No contacts available. Add a contact using the + button.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(birthdays.size) { index ->
                        val birthday = birthdays[index]
                        ContactCard(
                            name = birthday.name,
                            date = birthday.birthdayDate.format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            ),
                            description = birthday.description,
                            modifier = Modifier.clickable {
                                navController.navigate("contact_detail/${birthday.id}")
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { isDialogOpen.value = true },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Contact",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        AddContactDialog(
            isOpen = isDialogOpen,
            name = name,
            date = date,
            description = description,
            onNameChange = { birthdayViewModel.addContactName(it) },
            onDateChange = { birthdayViewModel.addContactDate(it) },
            onDescriptionChange = { birthdayViewModel.addContactDescription(it) },
            onAddContact = { birthdayViewModel.addBirthdayFromInput() }
        )
    }
}

@Composable
fun ContactCard(
    name: String,
    date: String,
    description : String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "$name",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Date: $date",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Description: $description",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

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
                        value = description,
                        onValueChange = onDescriptionChange,
                        label = { Text("Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 5,
                        singleLine = false
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



