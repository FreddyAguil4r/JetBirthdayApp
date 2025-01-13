package com.opbengalas.birthdayapp.screens.StoreScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.opbengalas.birthdayapp.models.Experience
import com.opbengalas.birthdayapp.models.Gift
import com.opbengalas.birthdayapp.models.PhysicalCard
import com.opbengalas.birthdayapp.models.VirtualCard

enum class StoreCategory {
    Gifts, PhysicalCards, VirtualCards, Experiences
}

@Composable
fun StoreScreen(modifier: Modifier = Modifier) {
    var selectedCategory by remember { mutableStateOf(StoreCategory.Gifts) }

    val items = when (selectedCategory) {
        StoreCategory.Gifts -> sampleGifts
        StoreCategory.PhysicalCards -> samplePhysicalCards
        StoreCategory.VirtualCards -> sampleVirtualCards
        StoreCategory.Experiences -> sampleExperiences
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StoreCategory.values().forEach { category ->
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selectedCategory == category) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(items) { item ->
                StoreItemCard(item = item)
            }
        }
    }
}

@Composable
fun StoreItemCard(item: Any) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { /* Handle click */ },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = when (item) {
                        is Gift -> item.imageUrl
                        is PhysicalCard -> item.imageUrl
                        is VirtualCard -> item.imageUrl
                        is Experience -> item.imageUrl
                        else -> ""
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Text(
                text = when (item) {
                    is Gift -> item.name
                    is PhysicalCard -> item.name
                    is VirtualCard -> item.name
                    is Experience -> item.name
                    else -> ""
                },
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = when (item) {
                    is Gift -> item.description
                    is PhysicalCard -> item.description
                    is VirtualCard -> item.description
                    is Experience -> item.description
                    else -> ""
                },
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Sample data
val sampleGifts = listOf(
    Gift("Watch", "https://m.media-amazon.com/images/I/41cHQmiqawL._AC_.jpg", "Elegant wristwatch"),
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
    )
)

val samplePhysicalCards = listOf(
    PhysicalCard(
        "Birthday Card",
        "https://lacasadelartesano.com.uy/images/thumbs/0013697_tarjeta-de-regalo-fisica-para-enviar.webp",
        "Happy Birthday"
    ),
    PhysicalCard(
        "Thank You Card",
        "https://danaturaleza.uy/wp-content/uploads/2023/12/portada-gift-card.webp",
        "Gratitude"
    )
)

val sampleVirtualCards = listOf(
    VirtualCard(
        "Digital Birthday Card",
        "https://www.btime.pe/wp-content/uploads/2022/12/100-2.png",
        "E-card with animations"
    ),
    VirtualCard(
        "Holiday E-Card",
        "https://suntimestore.com/cdn/shop/products/giftcarda100.png?v=1671132001",
        "Festive and fun"
    )
)

val sampleExperiences = listOf(
    Experience(
        "Skydiving",
        "https://www.detallesolemio.co/wp-content/uploads/2021/09/Desayunos-sorpresa-economico.jpeg",
        "Thrilling adventure"
    ),
    Experience(
        "Cooking Class",
        "https://www.bouquetdecoraciones.cl/wp-content/uploads/2022/04/regalo-sorpresa-1.jpg",
        "Learn to cook exotic dishes"
    ),
    Experience(
        "Wine Tasting",
        "https://www.bouquetdecoraciones.cl/wp-content/uploads/2023/09/Desayuno-sorpresa-Hombre-a-Domicilio.jpg",
        "Enjoy fine wines"
    )
)
