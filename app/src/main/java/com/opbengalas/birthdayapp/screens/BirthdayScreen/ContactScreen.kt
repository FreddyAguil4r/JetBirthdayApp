package com.opbengalas.birthdayapp.screens.BirthdayScreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
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
    val phoneNumber by birthdayViewModel.phoneNumber.collectAsState()
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
                            date = birthday.birthdayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            description = birthday.description,
                            imageResource = birthday.profileImage,
                            phoneNumber = birthday.phoneNumber,
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
            phoneNumber = phoneNumber,
            onNameChange = { birthdayViewModel.addContactName(it) },
            onDateChange = { birthdayViewModel.addContactDate(it) },
            onDescriptionChange = { birthdayViewModel.addContactDescription(it) },
            onPhoneNumberChange = { birthdayViewModel.addContactPhoneNumber(it) },
            onAddContact = { birthdayViewModel.addBirthdayFromInput() }
        )
    }
}

@Composable
fun ContactCard(
    name: String,
    date: String,
    description: String,
    imageResource: String,
    phoneNumber: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = painterResource(
                        id = LocalContext.current.resources.getIdentifier(
                            imageResource,
                            "drawable",
                            LocalContext.current.packageName
                        )
                    ),
                    contentDescription = "Contact Image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                    Text(
                        text = date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CallButton(phoneNumber = phoneNumber) // Botón para llamada
                    IconButton(onClick = { /* Lógica para enviar mensaje */ }) {
                        Icon(
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = "Message",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(start = 80.dp) // Alinea debajo de la imagen
                )
            }
        }
    }
}


@Composable
fun AddContactDialog(
    isOpen: MutableState<Boolean>,
    name: String,
    date: String,
    description: String,
    phoneNumber: String,
    onNameChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAddContact: () -> Unit
) {
    val isDatePickerOpen = remember { mutableStateOf(false) }

    if (isOpen.value) {
        AlertDialog(
            onDismissRequest = { isOpen.value = false },
            title = { Text("Add Contact", style = MaterialTheme.typography.titleLarge) },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Name", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Selected Date: $date",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { isDatePickerOpen.value = true }) {
                            Text("Pick Date")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = {
                            Text(
                                "Phone Number",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = onDescriptionChange,
                        label = { Text("Description", style = MaterialTheme.typography.bodyLarge) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 6,
                        singleLine = false
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAddContact()
                        isOpen.value = false
                    },
                ) {
                    Text("Add", style = MaterialTheme.typography.titleSmall)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isOpen.value = false },
                ) {
                    Text("Cancel", style = MaterialTheme.typography.titleSmall)
                }
            }
        )
    }

    if (isDatePickerOpen.value) {
        DatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val selectedDate =
                        java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                            .format(java.util.Date(it))
                    onDateChange(selectedDate)
                }
                isDatePickerOpen.value = false
            },
            onDismiss = { isDatePickerOpen.value = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun CallButton(phoneNumber: String) {
    val context = LocalContext.current
    val callPermissionState = remember { mutableStateOf(false) }

    fun checkAndRequestCallPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            callPermissionState.value = true
        }
    }

    LaunchedEffect(callPermissionState.value) {
        if (callPermissionState.value) {
            val formattedPhoneNumber = if (phoneNumber.startsWith("+51")) {
                phoneNumber
            } else {
                "+51$phoneNumber" // Asegurar que el prefijo de Perú esté presente
            }

            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$formattedPhoneNumber")
            }
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Call permission not granted", Toast.LENGTH_SHORT).show()
            }
            callPermissionState.value = false // Reset state after attempting a call
        }
    }

    IconButton(
        onClick = { checkAndRequestCallPermission() }
    ) {
        Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "Call",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}


