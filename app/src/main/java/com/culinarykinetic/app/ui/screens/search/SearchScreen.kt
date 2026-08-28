package com.culinarykinetic.app.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.mock.MockData
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.*

@Composable
fun SearchScreen(
    viewModel: AppViewModel,
    onBack: (() -> Unit)?,
    onRestaurantClick: (String) -> Unit,
    onFoodClick: (restaurantId: String, foodId: String) -> Unit
) {
    var query by remember { mutableStateOf(viewModel.searchQuery) }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val (matchedRestaurants, matchedFoods) = remember(query) { MockData.searchAll(query) }

    Column(Modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; viewModel.searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("Search for dishes, restaurants or cuisines") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        tint = BrandOrange
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(100.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.commitSearch(query)
                    focusManager.clearFocus()
                }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = BrandOrange.copy(alpha = 0.5f)
                )
            )
        }

        if (query.isBlank()) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.ScreenPadding)
            ) {
                item {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Recent Searches", style = MaterialTheme.typography.titleLarge)
                        if (viewModel.recentSearches.isNotEmpty()) {
                            Text(
                                "Clear",
                                color = BrandOrange,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.clickable { viewModel.clearRecentSearches() }
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(viewModel.recentSearches) { term ->
                            Row(
                                Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(ChipGray)
                                    .clickable { query = term }
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Filled.History,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = SubtleGray
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(term, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    Text("Popular Cuisines", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(MockData.popularCuisines) { cuisine ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { query = cuisine.name }) {
                                Box(
                                    Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(ChipGray)
                                ) {
                                    NetworkImage(
                                        cuisine.imageUrl, modifier = Modifier.clip(CircleShape)
                                    )
                                }
                                Spacer(Modifier.height(6.dp))
                                Text(cuisine.name, style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.LocalFireDepartment,
                            contentDescription = null,
                            tint = BrandOrange,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Trending Near You", style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(Modifier.height(12.dp))
                }
                items(viewModel.restaurants.take(4)) { restaurant ->
                    Box(Modifier.padding(bottom = 12.dp)) {
                        RestaurantListCard(
                            restaurant = restaurant,
                            isFavorite = viewModel.favoriteRestaurantIds.contains(restaurant.id),
                            onClick = { onRestaurantClick(restaurant.id) },
                            onFavoriteToggle = { viewModel.toggleFavoriteRestaurant(restaurant.id) }
                        )
                    }
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        } else {
            if (matchedRestaurants.isEmpty() && matchedFoods.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.Search,
                    title = "No results found",
                    message = "We couldn't find anything matching \"$query\". Try a different search term."
                )
            } else {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = Dimens.ScreenPadding)
                ) {
                    if (matchedRestaurants.isNotEmpty()) {
                        item {
                            Text("Restaurants", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(10.dp))
                        }
                        items(matchedRestaurants) { restaurant ->
                            Box(Modifier.padding(bottom = 12.dp)) {
                                RestaurantListCard(
                                    restaurant = restaurant,
                                    isFavorite = viewModel.favoriteRestaurantIds.contains(restaurant.id),
                                    onClick = {
                                        viewModel.commitSearch(query)
                                        onRestaurantClick(restaurant.id)
                                    },
                                    onFavoriteToggle = {
                                        viewModel.toggleFavoriteRestaurant(
                                            restaurant.id
                                        )
                                    }
                                )
                            }
                        }
                    }
                    if (matchedFoods.isNotEmpty()) {
                        item {
                            Spacer(Modifier.height(8.dp))
                            Text("Dishes", style = MaterialTheme.typography.titleLarge)
                            Spacer(Modifier.height(10.dp))
                        }
                        items(matchedFoods) { food ->
                            val restaurant =
                                viewModel.restaurants.find { it.menu.any { m -> m.id == food.id } }
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(CardWhite)
                                    .clickable {
                                        viewModel.commitSearch(query)
                                        if (restaurant != null) onFoodClick(restaurant.id, food.id)
                                    }
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NetworkImage(
                                    food.imageUrl,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(food.name, style = MaterialTheme.typography.titleSmall)
                                    Text(
                                        restaurant?.name ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SubtleGray
                                    )
                                    Text(
                                        "\u20B9${food.price}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = BrandOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}
