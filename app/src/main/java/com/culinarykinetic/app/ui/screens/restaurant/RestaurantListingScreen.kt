package com.culinarykinetic.app.ui.screens.restaurant

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.Restaurant
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.Dimens

@Composable
fun RestaurantListingScreen(
    categoryName: String,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onRestaurantClick: (String) -> Unit
) {
    var minRating by remember { mutableStateOf(false) }
    var sortByDelivery by remember { mutableStateOf(false) }
    var sortByPriceLowHigh by remember { mutableStateOf(false) }

    val base = remember(categoryName) {
        viewModel.restaurants.filter { r ->
            r.cuisines.any { it.contains(categoryName, ignoreCase = true) } ||
                    r.name.contains(categoryName, ignoreCase = true) ||
                    categoryName.equals("More", ignoreCase = true)
        }.ifEmpty { viewModel.restaurants }
    }

    val list = remember(base, minRating, sortByDelivery, sortByPriceLowHigh) {
        var result = base
        if (minRating) result = result.filter { it.rating >= 4.0 }
        result = when {
            sortByDelivery -> result.sortedBy { it.deliveryTimeMinutes }
            sortByPriceLowHigh -> result.sortedBy { it.priceForTwo }
            else -> result
        }
        result
    }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = categoryName, onBack = onBack)
        LazyRow(
            modifier = Modifier.padding(horizontal = Dimens.ScreenPadding, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { FilterChip("4.0+", minRating, Icons.Filled.Star, { minRating = !minRating }) }
            item { FilterChip("Delivery Time", sortByDelivery, null) {} }
            item { FilterChip("Price: Low to High", sortByPriceLowHigh, null, {}) }
            item { FilterChip("More filters", false, Icons.Filled.Tune) {} }
        }
        if (list.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Star,
                title = "No restaurants found",
                message = "Try adjusting your filters or check back later."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(list) { restaurant: Restaurant ->
                    RestaurantListCard(
                        restaurant = restaurant,
                        isFavorite = viewModel.favoriteRestaurantIds.contains(restaurant.id),
                        onClick = { onRestaurantClick(restaurant.id) },
                        onFavoriteToggle = { viewModel.toggleFavoriteRestaurant(restaurant.id) }
                    )
                }
                item { Spacer(Modifier.height(24.dp)) }
            }
        }
    }
}
