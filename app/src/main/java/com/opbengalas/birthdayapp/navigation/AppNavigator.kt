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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.opbengalas.birthdayapp.screens.BirthdayScreen.ContactScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.screens.BirthdayScreen.components.ContactDetailScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.components.EditContactScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.CalendarScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.components.NotificationSettingsScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.components.BirthdayActionsScreen
import com.opbengalas.birthdayapp.screens.MessageScreen.MessageGeneratorScreen
import com.opbengalas.birthdayapp.screens.StoreScreen.StoreScreen
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.VideoFeedScreen
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.VideoFeedViewModel
import java.time.LocalDate


@Composable
fun AppNavigator(
    navController: NavHostController,
    birthdayViewModel: BirthdayViewModel,
    videoFeedViewModel: VideoFeedViewModel
) {

    NavHost(navController = navController, startDestination = "contact_screen") {

        //CONTACTS SCREEN
        composable("contact_screen") {
            ContactScreen(navController = navController, birthdayViewModel = birthdayViewModel)
        }

        composable(
            route = "contact_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            ContactDetailScreen(
                navController = navController,
                birthdayViewModel = birthdayViewModel,
                id = id
            )
        }

        composable(
            route = "edit_contact/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            EditContactScreen(
                navController = navController,
                birthdayViewModel = birthdayViewModel,
                id = id
            )
        }

        //CALENDAR SCREEN
        composable("calendar") {
            CalendarScreen(navController = navController, birthdayViewModel = birthdayViewModel)
        }

        composable(
            route = "birthdayActionScreen/{selectedDate}",
            arguments = listOf(navArgument("selectedDate") { type = NavType.StringType })
        ) { backStackEntry ->
            val dateString =
                backStackEntry.arguments?.getString("selectedDate") ?: return@composable
            val selectedDate = LocalDate.parse(dateString)

            BirthdayActionsScreen(
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
            val contact =
                birthdayViewModel.listContact.collectAsState().value.find { it.id == contactId }

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

        //STORE SCREEN
        composable("storeScreen") {
            StoreScreen()
        }

        //VIDEOFEED SCREEN
        composable("videoFeedScreen") {
            VideoFeedScreen(navController = navController, videoFeedViewModel = videoFeedViewModel)
        }

        //MESSAGE SCREEN
        composable("messageScreen") {
            MessageGeneratorScreen()
        }

    }
}




