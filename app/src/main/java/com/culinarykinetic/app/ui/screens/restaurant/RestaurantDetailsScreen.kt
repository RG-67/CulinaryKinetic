package com.culinarykinetic.app.ui.screens.restaurant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.MenuItem
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.components.FilterChip
import com.culinarykinetic.app.ui.components.FavoriteToggleButton
import com.culinarykinetic.app.ui.theme.*

@Composable
fun RestaurantDetailsScreen(
    restaurantId: String,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onFoodClick: (String) -> Unit,
    onCartClick: () -> Unit
) {
    val restaurant = viewModel.restaurants.find { it.id == restaurantId } ?: return
    var selectedCategory by remember { mutableStateOf(restaurant.menuCategories.firstOrNull() ?: "Recommended") }
    val isFavorite = viewModel.favoriteRestaurantIds.contains(restaurant.id)

    Box(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Box {
                    NetworkImage(restaurant.coverImageUrl, modifier = Modifier.fillMaxWidth().height(220.dp))
                    Box(Modifier.fillMaxWidth().height(220.dp).background(Color.Black.copy(alpha = 0.15f)))
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .padding(12.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = InkBlack)
                    }
                    FavoriteToggleButton(
                        isFavorite = isFavorite,
                        onToggle = { viewModel.toggleFavoriteRestaurant(restaurant.id) },
                        modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                        background = Color.White
                    )
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .offset(y = (-24).dp)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(BrandCream)
                        .padding(20.dp)
                ) {
                    Text(restaurant.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SuccessGreen)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                Text(" ${restaurant.rating}", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                        Text(
                            "  (${restaurant.reviewCount}+ reviews)",
                            style = MaterialTheme.typography.bodySmall,
                            color = SubtleGray
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${restaurant.cuisines.joinToString(" \u2022 ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SubtleGray
                    )
                    Text(
                        "\u20B9${restaurant.priceForTwo} for two",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SubtleGray
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (restaurant.offerText != null) {
                            OfferPill(icon = Icons.Filled.LocalOffer, text = restaurant.offerText)
                        }
                        if (restaurant.freeDelivery) {
                            OfferPill(icon = Icons.Filled.DeliveryDining, text = "Free Delivery on all orders")
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(restaurant.menuCategories) { category ->
                            FilterChip(
                                category, selected = selectedCategory == category
                            ) { selectedCategory = category }
                        }
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    Text(selectedCategory, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))
                }
            }
            val filteredMenu = restaurant.menu.filter { it.category == selectedCategory }
            items(filteredMenu) { item ->
                MenuItemRow(
                    item = item,
                    isFavorite = viewModel.favoriteFoodIds.contains(item.id),
                    onFavoriteToggle = { viewModel.toggleFavoriteFood(item.id) },
                    onClick = { onFoodClick(item.id) },
                    onQuickAdd = {
                        viewModel.addToCart(restaurant, item, item.sizes.first(), emptyList(), 1)
                    }
                )
            }
            item { Spacer(Modifier.height(100.dp)) }
        }

        if (viewModel.cartItems.isNotEmpty() && viewModel.cartRestaurant?.id == restaurant.id) {
            val itemCount = viewModel.cartItems.sumOf { it.quantity }
            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .clip(RoundedCornerShape(100.dp))
                    .background(BrandOrange)
                    .clickable { onCartClick() }
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$itemCount item${if (itemCount > 1) "s" else ""} \u2022 \u20B9${viewModel.cartItemTotal}", color = Color.White, fontWeight = FontWeight.Bold)
                Text("View Cart \u2192", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun OfferPill(icon: ImageVector, text: String) {
    Row(
        Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(BrandCreamAlt)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = BrandOrangeDark, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, color = BrandOrangeDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MenuItemRow(
    item: MenuItem,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onClick: () -> Unit,
    onQuickAdd: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            Modifier
                .size(6.dp)
        )
        Box(Modifier.size(76.dp)) {
            NetworkImage(item.imageUrl, modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)))
            Box(
                Modifier
                    .align(Alignment.TopStart)
                    .padding(4.dp)
                    .size(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White)
                    .padding(1.dp)
                    .background(if (item.isVeg) SuccessGreen else BrandRed)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(Modifier.height(2.dp))
            Text(
                item.description,
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Text("\u20B9${item.price}", style = MaterialTheme.typography.titleSmall, color = BrandOrange, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(8.dp))
        IconButton(
            onClick = onQuickAdd,
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .then(Modifier)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = BrandOrange)
        }
    }
}
