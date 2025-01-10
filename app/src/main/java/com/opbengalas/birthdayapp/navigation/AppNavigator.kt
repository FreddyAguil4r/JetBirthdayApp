package com.opbengalas.birthdayapp.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.ContactDetailScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.EditContactScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.CalendarScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.components.NotificationSettingsScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.components.PersonalContactNotifierScreen
import com.opbengalas.birthdayapp.screens.MessageScreen.MessageGeneratorScreen
import java.time.LocalDate


@Composable
fun AppNavigator(navController: NavHostController, birthdayViewModel: BirthdayViewModel) {

    NavHost(navController = navController, startDestination = "birthday") {

        //CONTACTS SCREEN
        composable("birthday") {
            BirthdayScreen(navController = navController, birthdayViewModel = birthdayViewModel)
        }

        composable(
            route = "contact_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val contacts by birthdayViewModel.listContact.collectAsState()

            if (contacts.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                return@composable
            }

            val contact = contacts.find { it.id == id }
            if (contact != null) {
                ContactDetailScreen(contact = contact, navController = navController)
            } else {

                Text(
                    text = "Contact Detail not found with ID: $id",
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
                Log.e("AppNavigator", "Contact Detail not found with ID: $id")
            }
        }

        composable(
            route = "edit_contact/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val contacts by birthdayViewModel.listContact.collectAsState()

            if (contacts.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                return@composable
            }

            val contact = contacts.find { it.id == id }
            if (contact != null) {
                EditContactScreen(contact = contact, navController = navController)
            } else {
                Log.e("AppNavigator", "Edit Contant not found with ID: $id")
            }
        }

        //CALENDAR SCREEN
        composable("calendar") {
            CalendarScreen(navController = navController, birthdayViewModel = birthdayViewModel)
        }

        composable(
            route = "personalContactNotifier/{selectedDate}",
            arguments = listOf(navArgument("selectedDate") { type = NavType.StringType })
        ) { backStackEntry ->
            val dateString = backStackEntry.arguments?.getString("selectedDate") ?: return@composable
            val selectedDate = LocalDate.parse(dateString)

            PersonalContactNotifierScreen(
                selectedDate = selectedDate,
                birthdayViewModel = birthdayViewModel,
                navController = navController
            )
        }

        composable(
            route = "notification_settings/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.IntType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getInt("contactId") ?: return@composable
            val contact = birthdayViewModel.listContact.collectAsState().value.find { it.id == contactId }

            if (contact != null) {
                NotificationSettingsScreen(
                    contact = contact,
                    onSave = { updatedContact ->
                        birthdayViewModel.updateContact(updatedContact)
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }

        //MESSAGE GENERATOR SCREEN
        composable("messageGenerator") {
            MessageGeneratorScreen()
        }

    }
}




