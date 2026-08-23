package com.culinarykinetic.app.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.*

private data class Banner(
    val imageUrl: String,
    val tag: String,
    val title: String,
    val subtitle: String
)

private val banners = listOf(
    Banner(
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=900&q=80",
        "LIMITED TIME", "50% OFF", "on your first order"
    ),
    Banner(
        "https://images.unsplash.com/photo-1594007654729-407eedc4be65?w=900&q=80",
        "TONIGHT ONLY", "Free Delivery", "on orders above \u20B9299"
    ),
    Banner(
        "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=900&q=80",
        "NEW", "Try Biryani Week", "curated biryani specials"
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onSearchClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    onRestaurantClick: (String) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { banners.size })
    var fastDeliveryOnly by remember { mutableStateOf(false) }
    var topRatedOnly by remember { mutableStateOf(false) }

    val filteredRestaurants = remember(fastDeliveryOnly, topRatedOnly) {
        viewModel.restaurants.filter { r ->
            (!fastDeliveryOnly || r.deliveryTimeMinutes.contains("15") || r.deliveryTimeMinutes.contains(
                "20"
            )) &&
                    (!topRatedOnly || r.rating >= 4.5)
        }
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumnLikeScroll {
            // Top bar: location + notifications
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.ScreenPadding, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = BrandOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(2.dp))
                            Text(
                                "Current Location",
                                style = MaterialTheme.typography.labelSmall,
                                color = SubtleGray
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                viewModel.currentLocationLabel,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Filled.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Box {
                        IconButton(
                            onClick = onNotificationsClick,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(ChipGray)
                        ) {
                            Icon(
                                Icons.Filled.NotificationsNone,
                                contentDescription = "Notifications",
                                tint = CharcoalText
                            )
                        }
                        if (viewModel.unreadNotificationCount > 0) {
                            Box(
                                Modifier
                                    .align(Alignment.TopEnd)
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(BrandRed)
                            )
                        }
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    Text(
                        "Good evening, ${viewModel.currentUser.name.substringBefore(" ")}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        "What are you craving today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SubtleGray
                    )
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Row(
                    Modifier
                        .padding(horizontal = Dimens.ScreenPadding)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.White)
                        .border(1.dp, DividerGray, RoundedCornerShape(100.dp))
                        .clickableRow(onSearchClick)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, tint = SubtleGray)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Restaurant name, cuisine, or a dish...",
                        color = SubtleGray,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Filled.Mic, contentDescription = null, tint = BrandOrange)
                }
            }
            item { Spacer(Modifier.height(18.dp)) }
            item {
                Box(
                    Modifier
                        .padding(horizontal = Dimens.ScreenPadding)
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                        val b = banners[page]
                        Box(
                            Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        ) {
                            NetworkImage(b.imageUrl, modifier = Modifier.fillMaxSize())
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.28f))
                            )
                            Column(
                                Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    b.tag,
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(BrandRed)
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    b.title,
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    b.subtitle,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    Row(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        banners.indices.forEach { i ->
                            Box(
                                Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (i == pagerState.currentPage) Color.White else Color.White.copy(
                                            alpha = 0.4f
                                        )
                                    )
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(22.dp)) }
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionHeader("Explore Categories")
                    Spacer(Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                        items(viewModel.categories) { cat ->
                            CategoryChip(cat.name, cat.imageUrl) { onCategoryClick(cat.name) }
                        }
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickableRow { onSearchClick() }) {
                                Box(
                                    Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(BrandCreamAlt),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = BrandOrange
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text("More", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(18.dp)) }
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = Dimens.ScreenPadding),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item { FilterChip("Filters", false, Icons.Filled.Tune, {}) }
                    item {
                        FilterChip(
                            "Fast Delivery",
                            fastDeliveryOnly,
                            Icons.Filled.Bolt
                        ) { fastDeliveryOnly = !fastDeliveryOnly }
                    }
                    item { FilterChip("Rating 4.5+", topRatedOnly, null, {}) }
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionHeader("Recommended for you")
                }
            }
            item { Spacer(Modifier.height(12.dp)) }
            items(filteredRestaurants) { restaurant ->
                Box(Modifier.padding(horizontal = Dimens.ScreenPadding, vertical = 8.dp)) {
                    RestaurantCard(
                        restaurant = restaurant,
                        isFavorite = viewModel.favoriteRestaurantIds.contains(restaurant.id),
                        onClick = { onRestaurantClick(restaurant.id) },
                        onFavoriteToggle = { viewModel.toggleFavoriteRestaurant(restaurant.id) }
                    )
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
            item {
                Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
                    SectionHeader("Popular Dishes near you")
                    Spacer(Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(viewModel.restaurants.flatMap { it.menu }.distinctBy { it.id }
                            .take(8)) { food ->
                            Column(
                                Modifier
                                    .width(140.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color.White)
                                    .clickableRow {
                                        val r =
                                            viewModel.restaurants.find { it.menu.any { m -> m.id == food.id } }
                                        if (r != null) onRestaurantClick(r.id)
                                    }
                            ) {
                                NetworkImage(
                                    food.imageUrl,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(100.dp)
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                )
                                Column(Modifier.padding(10.dp)) {
                                    Text(
                                        food.name,
                                        style = MaterialTheme.typography.labelLarge,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Text(
                                        "\u20B9${food.price}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = BrandOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
}

@Composable
private fun LazyColumnLikeScroll(content: androidx.compose.foundation.lazy.LazyListScope.() -> Unit) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        content = content
    )
}

private fun Modifier.clickableRow(onClick: () -> Unit): Modifier = this.clickable(onClick = onClick)
