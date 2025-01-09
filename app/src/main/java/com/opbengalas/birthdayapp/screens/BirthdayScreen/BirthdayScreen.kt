package com.opbengalas.birthdayapp.screens.BirthdayScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.AddContactDialog
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.ContactCard
import com.opbengalas.birthdayapp.ui.theme.Red
import java.time.format.DateTimeFormatter

@Composable
fun BirthdayScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    birthdayViewModel: BirthdayViewModel,
) {
    val birthdays by birthdayViewModel.sortedContacts.collectAsState()
    val name by birthdayViewModel.name.collectAsState()
    val date by birthdayViewModel.date.collectAsState()
    val description by birthdayViewModel.description.collectAsState()
    val isDialogOpen = remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
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
            containerColor = Red,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Contact",
                tint = Color.White
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
