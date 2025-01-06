package com.opbengalas.birthdayapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.opbengalas.birthdayapp.components.AppNavigator
import com.opbengalas.birthdayapp.components.BottomNavigationBar
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.ui.theme.BirthdayAppTheme
import com.opbengalas.birthdayapp.ui.theme.Red
import com.opbengalas.birthdayapp.ui.theme.White
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BirthdayAppTheme {
                val birthdayViewModel: BirthdayViewModel = hiltViewModel()
                val searchQuery = remember { mutableStateOf("") }
                val selectedTab = remember { mutableStateOf(0) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                SearchBar(
                                    query = searchQuery.value,
                                    onQueryChange = { searchQuery.value = it },
                                    onSearch = {},
                                    active = false,
                                    onActiveChange = {},
                                    placeholder = { Text("Search contacts") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {}
                            },
                            actions = {
                                IconButton(onClick = {
                                    birthdayViewModel.toggleSortOrder()
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.sort),
                                        contentDescription = "Sort Options"
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Red,
                                titleContentColor = White,
                            )
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            selectedTab = selectedTab.value,
                            onTabSelected = { index ->
                                selectedTab.value = index
                            }
                        )
                    },
                    content = { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigator(
                                birthdayViewModel = birthdayViewModel,
                                searchQuery = searchQuery.value,
                                selectedTab = selectedTab.value,
                                onDestinationChanged = { destination ->
                                    selectedTab.value = when (destination) {
                                        "birthday" -> 0
                                        "calendar" -> 1
                                        "messageGenerator" -> 2
                                        else -> 0
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
    }
}


