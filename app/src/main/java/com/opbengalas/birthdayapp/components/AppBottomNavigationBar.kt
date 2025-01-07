package com.opbengalas.birthdayapp.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    BottomNavigationBar(
        selectedTab = when (currentDestination?.route) {
            "birthday" -> 0
            "calendar" -> 1
            "messageGenerator" -> 2
            else -> 0
        },
        onTabSelected = { index ->
            when (index) {
                0 -> navController.navigate("birthday") { launchSingleTop = true }
                1 -> navController.navigate("calendar") { launchSingleTop = true }
                2 -> navController.navigate("messageGenerator") { launchSingleTop = true }
            }
        }
    )
}
