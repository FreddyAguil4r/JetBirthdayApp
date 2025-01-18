package com.opbengalas.birthdayapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.opbengalas.birthdayapp.navigation.AppNavigator
import com.opbengalas.birthdayapp.ui.theme.BirthdayAppTheme
import androidx.navigation.compose.rememberNavController
import com.opbengalas.birthdayapp.components.AppBottomNavigationBar
import com.opbengalas.birthdayapp.components.AppTopBar
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.screens.BirthdayScreen.components.ContactDetailTopBar
import com.opbengalas.birthdayapp.screens.BirthdayScreen.components.ContactTopBar
import com.opbengalas.birthdayapp.screens.CalendarScreen.components.CalendarTopBar
import com.opbengalas.birthdayapp.screens.StoreScreen.components.StoreTopBar
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.VideoFeedScreen
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.VideoFeedViewModel
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.components.VideoFeedTopBar
import com.opbengalas.birthdayapp.service.NotificationService
import com.opbengalas.birthdayapp.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var notificationService: NotificationService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.requestNotificationPermission(this)

        setContent {
            BirthdayAppTheme {
                val navController = rememberNavController()
                val birthdayViewModel: BirthdayViewModel = hiltViewModel()
                val videoViewModel : VideoFeedViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    birthdayViewModel.notifyContacts.collect { contact ->
                        notificationService.showDefaultNotification(contact)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val currentEntry = navController.currentBackStackEntryAsState().value
                        val currentRoute = currentEntry?.destination?.route ?: ""
                        val id = currentEntry?.arguments?.getInt("id")

                        Log.d("NavController", "Current Route: $currentRoute")

                        when (currentRoute) {
                            "contact_screen" -> ContactTopBar(navController, birthdayViewModel)
                            "calendar" -> CalendarTopBar(navController, birthdayViewModel)
                            "storeScreen" -> StoreTopBar(navController, birthdayViewModel)
                            "videoFeedScreen" -> VideoFeedTopBar(navController, videoViewModel)
                            "contact_detail/{id}" -> ContactDetailTopBar(navController)
                            else -> AppTopBar(navController, birthdayViewModel)
                        }
                    },
                    bottomBar = { AppBottomNavigationBar(navController) },
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigator(navController, birthdayViewModel,videoViewModel)
                        }
                    }
                )
            }
        }
    }
}