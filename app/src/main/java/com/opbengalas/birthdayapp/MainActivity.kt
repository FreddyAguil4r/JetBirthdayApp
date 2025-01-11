package com.opbengalas.birthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.opbengalas.birthdayapp.navigation.AppNavigator
import com.opbengalas.birthdayapp.ui.theme.BirthdayAppTheme
import androidx.navigation.compose.rememberNavController
import com.opbengalas.birthdayapp.components.AppBottomNavigationBar
import com.opbengalas.birthdayapp.components.AppTopBar
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
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

                LaunchedEffect(Unit) {
                    birthdayViewModel.notifyContacts.collect { contact ->
                        notificationService.showDefaultNotification(contact)
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { AppTopBar(navController, birthdayViewModel) },
                    bottomBar = { AppBottomNavigationBar(navController) },
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigator(navController, birthdayViewModel)
                        }
                    }
                )
            }
        }
    }
}