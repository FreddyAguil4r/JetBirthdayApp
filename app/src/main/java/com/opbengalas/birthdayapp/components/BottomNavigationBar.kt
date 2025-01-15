package com.opbengalas.birthdayapp.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.opbengalas.birthdayapp.R

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.contact_image),
                    contentDescription = "Contacts"
                )
            },
            label = { Text("Contacts") }
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.calendar),
                    contentDescription = "Calendar"
                )
            },
            label = { Text("Calendar") }
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.store_image),
                    contentDescription = "Store"
                )
            },
            label = { Text("Store") }
        )
        NavigationBarItem(
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            icon = {
                Icon(
                    painterResource(id = R.drawable.video_image),
                    contentDescription = "Video"
                )
            },
            label = { Text("Joys") }
        )
    }
}