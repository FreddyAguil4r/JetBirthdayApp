package com.opbengalas.birthdayapp.screens.CalendarScreen


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.opbengalas.birthdayapp.R
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.models.Gift
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
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
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
                navController.navigate("birthdayActionScreen/${day}")
            }
        )

        Spacer(modifier = modifier.height(8.dp))
        ClosestBirthdaysSection(contacts = contacts)

        Spacer(modifier = modifier.height(8.dp))
        SuggestedGiftsSection()
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

    val chunkedDays = days.chunked(7)

    Column(modifier = Modifier.fillMaxWidth()) {
        chunkedDays.forEach { weekDays ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                weekDays.forEach { day ->
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
    }
}

fun generateCalendarDays(firstDay: LocalDate, lastDay: LocalDate): List<LocalDate> {
    val firstDayOfWeek = firstDay.minusDays(firstDay.dayOfWeek.value.toLong() - 1)
    val lastDayOfWeek = lastDay.plusDays(7 - lastDay.dayOfWeek.value.toLong())

    return (0 until ceil(
        ChronoUnit.DAYS.between(firstDayOfWeek, lastDayOfWeek).toDouble()
    ).toInt()).map {
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

        Column( // Reemplazamos LazyColumn por Column
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            closestBirthdayContacts.forEach{ contact -> // Reemplazamos el items por un forEach
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

@Composable
fun SuggestedGiftsSection() {
    val gifts = listOf(
        Gift(
            "Watch",
            "https://m.media-amazon.com/images/I/41cHQmiqawL._AC_.jpg",
            "Elegant wristwatch"
        ),
        Gift(
            "Perfume",
            "https://belcorpperu.vtexassets.com/arquivos/ids/309040-1600-auto?v=638545962284170000&width=1600&height=auto&aspect=true",
            "Luxury fragrance"
        ),
        Gift(
            "Book",
            "https://dictionary.cambridge.org/es/images/thumb/book_noun_001_01679.jpg?version=6.0.43",
            "Inspirational read"
        ),
        Gift(
            "Headphones",
            "https://imagedelivery.net/4fYuQyy-r8_rpBpcY7lH_A/falabellaPE/137414219_01/w=1500,h=1500,fit=pad",
            "High-quality sound"
        ),
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Suggested Gifts",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(gifts) { gift ->
                GiftCard(gift = gift)
            }
        }
    }
}

@Composable
fun GiftCard(gift: Gift) {
    var isLoading by remember { mutableStateOf(true) }

    Card(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp)
            .clickable { /* Future action: Handle gift click */ },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gift.imageUrl)
                        .crossfade(true)
                        .build(),
                    onLoading = { isLoading = true },
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false },
                    error = painterResource(R.drawable.error_image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 2.dp
                    )
                }
            }

            Text(
                text = gift.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = gift.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

