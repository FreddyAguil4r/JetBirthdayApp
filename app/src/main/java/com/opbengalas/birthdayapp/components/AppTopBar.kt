package com.opbengalas.birthdayapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.R
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import com.opbengalas.birthdayapp.ui.theme.Red
import com.opbengalas.birthdayapp.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavController, birthdayViewModel: BirthdayViewModel) {

    val searchQuery = remember { mutableStateOf("") }

    TopAppBar(
        title = {
            SearchBar(
                query = searchQuery.value,
                onQueryChange = { searchQuery.value = it },
                onSearch = { },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search contacts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {}
        },
        actions = {
            IconButton(onClick = { birthdayViewModel.toggleSortOrder() }) {
                Icon(
                    painter = painterResource(id = R.drawable.sort),
                    contentDescription = "Sort Options"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Red,
            titleContentColor = White
        )
    )
}

