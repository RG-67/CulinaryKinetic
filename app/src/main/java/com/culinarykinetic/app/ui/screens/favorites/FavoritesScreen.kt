package com.culinarykinetic.app.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.*

@Composable
fun FavoritesScreen(
    viewModel: AppViewModel,
    onBack: (() -> Unit)?,
    onRestaurantClick: (String) -> Unit,
    onFoodClick: (restaurantId: String, foodId: String) -> Unit,
    onBrowse: () -> Unit
) {
    var tab by remember { mutableStateOf(0) }
    val favRestaurants = viewModel.restaurants.filter { viewModel.favoriteRestaurantIds.contains(it.id) }
    val favFoods = viewModel.restaurants.flatMap { r -> r.menu.filter { viewModel.favoriteFoodIds.contains(it.id) }.map { r to it } }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Favorites", onBack = onBack)
        TabRow(
            selectedTabIndex = tab,
            containerColor = BrandCream,
            contentColor = BrandOrange,
            indicator = { positions -> TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(positions[tab]), color = BrandOrange) }
        ) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Restaurants", fontWeight = FontWeight.Bold) })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Dishes", fontWeight = FontWeight.Bold) })
        }
        if (tab == 0) {
            if (favRestaurants.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FavoriteBorder,
                    title = "No favorite restaurants yet",
                    message = "Tap the heart icon on any restaurant to save it here.",
                    actionText = "Browse Restaurants",
                    onAction = onBrowse
                )
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { Spacer(Modifier.height(6.dp)) }
                    items(favRestaurants, key = { it.id }) { restaurant ->
                        RestaurantListCard(
                            restaurant = restaurant,
                            isFavorite = true,
                            onClick = { onRestaurantClick(restaurant.id) },
                            onFavoriteToggle = { viewModel.toggleFavoriteRestaurant(restaurant.id) }
                        )
                    }
                    item { Spacer(Modifier.height(90.dp)) }
                }
            }
        } else {
            if (favFoods.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.FavoriteBorder,
                    title = "No favorite dishes yet",
                    message = "Tap the heart icon on any dish to save it here.",
                    actionText = "Browse Restaurants",
                    onAction = onBrowse
                )
            } else {
                LazyColumn(Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    item { Spacer(Modifier.height(6.dp)) }
                    items(favFoods, key = { it.second.id }) { (restaurant, food) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(CardWhite)
                                .clickable { onFoodClick(restaurant.id, food.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            NetworkImage(food.imageUrl, modifier = Modifier.size(60.dp).clip(RoundedCornerShape(10.dp)))
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(food.name, style = MaterialTheme.typography.titleSmall)
                                Text(restaurant.name, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                                Text("\u20B9${food.price}", style = MaterialTheme.typography.labelLarge, color = BrandOrange, fontWeight = FontWeight.Bold)
                            }
                            FavoriteToggleButton(
                                isFavorite = true,
                                onToggle = { viewModel.toggleFavoriteFood(food.id) },
                                background = ChipGray
                            )
                            Spacer(Modifier.width(6.dp))
                            IconButton(
                                onClick = { viewModel.addToCart(restaurant, food, food.sizes.first(), emptyList(), 1) },
                                modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(BrandCreamAlt)
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Add to cart", tint = BrandOrange)
                            }
                        }
                    }
                    item { Spacer(Modifier.height(90.dp)) }
                }
            }
        }
    }
}
