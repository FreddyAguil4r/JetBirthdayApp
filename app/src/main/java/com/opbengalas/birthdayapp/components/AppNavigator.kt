package com.opbengalas.birthdayapp.components


import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayScreen
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
            route = "contact_detail/{name}/{date}/{description}",
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val description = backStackEntry.arguments?.getString("description") ?: ""
            ContactDetailScreen(
                name = Uri.decode(name),
                date = Uri.decode(date),
                description = Uri.decode(description),
                onEdit = { /* Acción de edición */ },
                onDelete = { /* Acción de eliminación */ }
            )
        }
    }
}




