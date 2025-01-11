package com.opbengalas.birthdayapp.screens.CalendarScreen


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.R
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.screens.BirthdayScreen.BirthdayViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.ceil


@Composable
fun CalendarScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    birthdayViewModel: BirthdayViewModel,
) {
    val contacts by birthdayViewModel.listContact.collectAsState()
    val currentMonth = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CalendarHeader(
            currentMonth = currentMonth.value,
            onPreviousMonth = {
                currentMonth.value = currentMonth.value.minusMonths(1)
            },
            onNextMonth = { currentMonth.value = currentMonth.value.plusMonths(1) }
        )

        DaysOfWeekHeader()

        CalendarGrid(
            currentMonth = currentMonth.value,
            contacts = contacts,
            onDayClick = { day ->
                navController.navigate("personalContactNotifier/${day}")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ClosestBirthdaysSection(contacts = contacts)
    }
}

@Composable
fun CalendarHeader(
    currentMonth: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "Previous Month"
            )
        }

        Text(
            text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = "Next Month"
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        for (day in daysOfWeek) {
            Text(
                text = day,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CalendarGrid(
    currentMonth: LocalDate,
    contacts: List<Contact>,
    onDayClick: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.withDayOfMonth(1)
    val lastDayOfMonth = currentMonth.withDayOfMonth(currentMonth.lengthOfMonth())
    val days = generateCalendarDays(firstDayOfMonth, lastDayOfMonth)

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days) { day ->
            val isInCurrentMonth = day.month == currentMonth.month
            val isFirstDayOfMonth = day == firstDayOfMonth
            val isLastDayOfMonth = day == lastDayOfMonth

            val textColor = when {
                isFirstDayOfMonth || isLastDayOfMonth -> MaterialTheme.colorScheme.onBackground
                isInCurrentMonth -> MaterialTheme.colorScheme.onBackground
                else -> Color.Gray
            }

            val fontWeight = when {
                isFirstDayOfMonth || isLastDayOfMonth -> FontWeight.Bold
                else -> FontWeight.Normal
            }

            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onDayClick(day) },
                color = if (contacts.any { contact ->
                        contact.birthdayDate.month == day.month && contact.birthdayDate.dayOfMonth == day.dayOfMonth
                    }) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = MaterialTheme.shapes.small
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = day.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = fontWeight
                        ),
                        textAlign = TextAlign.Center,
                        color = textColor
                    )
                }
            }
        }
    }
}

fun generateCalendarDays(firstDay: LocalDate, lastDay: LocalDate): List<LocalDate> {
    val firstDayOfWeek = firstDay.minusDays(firstDay.dayOfWeek.value.toLong() - 1)
    val lastDayOfWeek = lastDay.plusDays(7 - lastDay.dayOfWeek.value.toLong())

    return (0 until ceil(ChronoUnit.DAYS.between(firstDayOfWeek, lastDayOfWeek).toDouble()).toInt()).map {
        firstDayOfWeek.plusDays(it.toLong())
    }
}

@Composable
fun ClosestBirthdaysSection(contacts: List<Contact>) {
    val today = LocalDate.now()

    val sortedContacts = contacts.sortedBy {
        val birthdayThisYear = it.birthdayDate.withYear(today.year)
        if (birthdayThisYear.isBefore(today)) birthdayThisYear.plusYears(1) else birthdayThisYear
    }

    val closestBirthdayContacts = sortedContacts.filter {
        val closestBirthday = sortedContacts.firstOrNull()?.birthdayDate?.withYear(today.year)
        it.birthdayDate.withYear(today.year) == closestBirthday
    }

    if (closestBirthdayContacts.isNotEmpty()) {
        Text(
            text = "Closest Birthdays 🎉",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(closestBirthdayContacts) { contact ->
                BirthdayCard(contact = contact)
            }
        }
    }
}

@Composable
fun BirthdayCard(contact: Contact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_cake),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "🎂 ${contact.birthdayDate.format(DateTimeFormatter.ofPattern("dd MMMM"))}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}