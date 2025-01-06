package com.opbengalas.birthdayapp.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayScreen
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.screens.BirthdayScreen.componentsBScreen.ContactDetailScreen
import com.opbengalas.birthdayapp.screens.CalendarScreen.CalendarScreen
import com.opbengalas.birthdayapp.screens.MessageScreen.MessageGeneratorScreen

@Composable
fun AppNavigator(
    birthdayViewModel: BirthdayViewModel,
    searchQuery: String,
    selectedTab: Int,
    onDestinationChanged: (String) -> Unit
) {
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            onDestinationChanged(destination.route ?: "birthday")
        }
    }

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> navController.navigate("birthday") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            1 -> navController.navigate("calendar") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            2 -> navController.navigate("messageGenerator") {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "birthday"
    ) {
        composable("birthday") {
            BirthdayScreen(
                birthdayViewModel = birthdayViewModel,
                navController = navController,
                searchQuery = searchQuery
            )
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
                name = name,
                date = date,
                description = description,
                onEdit = { /* Implementar edición */ },
                onDelete = { /* Implementar eliminación */ }
            )
        }
    }
}




