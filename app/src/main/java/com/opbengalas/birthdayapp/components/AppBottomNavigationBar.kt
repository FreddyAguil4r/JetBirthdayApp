package com.opbengalas.birthdayapp.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    BottomNavigationBar(
        selectedTab = when (currentDestination?.route) {
            "contact_screen" -> 0
            "calendar" -> 1
            "storeScreen" -> 2
            "videoFeedScreen" -> 3
            else -> 0
        },
        onTabSelected = { index ->
            when (index) {
                0 -> navController.navigate("contact_screen") { launchSingleTop = true }
                1 -> navController.navigate("calendar") { launchSingleTop = true }
                2 -> navController.navigate("storeScreen") { launchSingleTop = true }
                3 -> navController.navigate("videoFeedScreen") { launchSingleTop = true }
            }
        }
    )
}
