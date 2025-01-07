package com.opbengalas.birthdayapp.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.ContactDetailScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.CalendarScreen
import com.opbengalas.birthdayapp.screens.MessageScreen.MessageGeneratorScreen


@Composable
fun AppNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "birthday") {
        composable("birthday") {
            BirthdayScreen(navController = navController)
        }
        composable("calendar") {
            CalendarScreen()
        }
        composable("messageGenerator") {
            MessageGeneratorScreen()
        }
        composable(
            route = "contact_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            val birthdayViewModel: BirthdayViewModel = hiltViewModel()
            val contacts by birthdayViewModel.listContact.collectAsState()

            val contact = contacts.find { it.id == id }
            if (contact != null) {
                ContactDetailScreen(contact = contact, navController = navController)
            } else {
                Log.e("AppNavigator", "Contact not found with ID: $id")
            }
        }
    }
}




