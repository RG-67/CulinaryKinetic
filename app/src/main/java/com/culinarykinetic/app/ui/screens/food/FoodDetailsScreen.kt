package com.culinarykinetic.app.ui.screens.food

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.AddOn
import com.culinarykinetic.app.model.SizeOption
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.QuantitySelector
import com.culinarykinetic.app.ui.theme.*

@Composable
fun FoodDetailsScreen(
    restaurantId: String,
    foodId: String,
    viewModel: AppViewModel,
    onClose: () -> Unit,
    onGoToCart: () -> Unit
) {
    val restaurant = viewModel.restaurants.find { it.id == restaurantId } ?: return
    val food = restaurant.menu.find { it.id == foodId } ?: return

    var selectedSize by remember(foodId) { mutableStateOf(food.sizes.first()) }
    val selectedAddOns = remember(foodId) { mutableStateListOf<AddOn>() }
    var quantity by remember(foodId) { mutableStateOf(1) }
    val isFavorite = viewModel.favoriteFoodIds.contains(food.id)

    val unitPrice = food.price + selectedSize.extraPrice + selectedAddOns.sumOf { it.price }
    val totalPrice = unitPrice * quantity

    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.35f))) {
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(BrandCream)
        ) {
            Box(Modifier.weight(1f)) {
                Column(Modifier.verticalScroll(rememberScrollState())) {
                    Box {
                        NetworkImage(food.imageUrl, modifier = Modifier.fillMaxWidth().height(220.dp))
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        ) {
                            Icon(Icons.Filled.Close, contentDescription = "Close", tint = InkBlack)
                        }
                    }
                    Column(Modifier.padding(20.dp)) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(food.name, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
                            Text("\u20B9${food.price}", style = MaterialTheme.typography.headlineSmall, color = BrandOrange, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SuccessGreen)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                                    Text(" ${food.rating}", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text("  (${food.reviewCount} reviews)", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                        }
                        Spacer(Modifier.height(12.dp))
                        Text(food.description, style = MaterialTheme.typography.bodyMedium, color = CharcoalText)
                        Spacer(Modifier.height(20.dp))
                        Divider(color = DividerGray)
                        Spacer(Modifier.height(16.dp))

                        if (food.sizes.size > 1) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Choose Size", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "REQUIRED", style = MaterialTheme.typography.labelSmall, color = SubtleGray,
                                    modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(ChipGray).padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(Modifier.height(10.dp))
                            food.sizes.forEach { size ->
                                SizeOptionRow(size, selectedSize == size) { selectedSize = size }
                                Spacer(Modifier.height(8.dp))
                            }
                            Spacer(Modifier.height(8.dp))
                        }

                        if (food.addOns.isNotEmpty()) {
                            Text("Add-ons", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(10.dp))
                            food.addOns.forEach { addOn ->
                                val checked = selectedAddOns.contains(addOn)
                                AddOnRow(addOn, checked) {
                                    if (checked) selectedAddOns.remove(addOn) else selectedAddOns.add(addOn)
                                }
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(BrandCream)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuantitySelector(
                    quantity = quantity,
                    onIncrement = { quantity++ },
                    onDecrement = { if (quantity > 1) quantity-- }
                )
                PrimaryButton(
                    text = "Add to Cart \u2022 \u20B9$totalPrice",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.addToCart(restaurant, food, selectedSize, selectedAddOns.toList(), quantity)
                        onGoToCart()
                    }
                )
            }
        }
    }
}

@Composable
private fun SizeOptionRow(size: SizeOption, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) BrandCreamAlt else CardWhite)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) BrandOrange else DividerGray,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = BrandOrange))
        Spacer(Modifier.width(4.dp))
        Column(Modifier.weight(1f)) {
            Text(size.label, style = MaterialTheme.typography.titleSmall)
        }
        Text(size.note, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
        if (size.extraPrice > 0) {
            Text("  + \u20B9${size.extraPrice}", style = MaterialTheme.typography.labelLarge, color = BrandOrange)
        }
    }
}

@Composable
private fun AddOnRow(addOn: AddOn, checked: Boolean, onToggle: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(CardWhite)
            .clickable { onToggle() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() }, colors = CheckboxDefaults.colors(checkedColor = BrandOrange))
        Text(addOn.name, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text("+ \u20B9${addOn.price}", style = MaterialTheme.typography.labelLarge, color = BrandOrange)
    }
}

