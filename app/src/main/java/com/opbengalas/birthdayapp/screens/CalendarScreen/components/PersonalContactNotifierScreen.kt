package com.opbengalas.birthdayapp.screens.CalendarScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PersonalContactNotifierScreen(
    selectedDate: LocalDate,
    birthdayViewModel: BirthdayViewModel,
    navController: NavHostController
) {
    val contacts by birthdayViewModel.listContact.collectAsState()
    val contactsForDay = contacts.filter {
        it.birthdayDate.month == selectedDate.month && it.birthdayDate.dayOfMonth == selectedDate.dayOfMonth
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (contactsForDay.isEmpty()) {
            Text(
                text = "No contacts have birthdays on this day.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contactsForDay) { contact ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Birthday: ${
                                    contact.birthdayDate.format(
                                        DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy"
                                        )
                                    )
                                }",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = contact.description,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Button(
                                onClick = {
                                    navController.navigate("notification_settings/${contact.id}")
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(text = "Add Notification")
                            }
                            Button(
                                onClick = {
                                    // Navigate to a screen to add personalized notification
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text(text = "Add Gift Card")
                            }
                        }
                    }
                }
            }
        }
    }
}
