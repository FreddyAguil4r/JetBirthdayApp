package com.opbengalas.birthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.opbengalas.birthdayapp.components.AppNavigator
import com.opbengalas.birthdayapp.ui.theme.BirthdayAppTheme
import androidx.navigation.compose.rememberNavController
import com.opbengalas.birthdayapp.components.AppBottomNavigationBar
import com.opbengalas.birthdayapp.components.AppTopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BirthdayAppTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {AppTopBar(navController)},
                    bottomBar = { AppBottomNavigationBar(navController) },
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigator(navController)
                        }
                    }
                )
            }
        }
    }
}


