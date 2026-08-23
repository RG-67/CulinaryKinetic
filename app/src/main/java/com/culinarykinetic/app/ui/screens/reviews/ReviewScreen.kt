package com.culinarykinetic.app.ui.screens.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.FilterChip
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable

private val tags = listOf("Great taste", "On time", "Well packed", "Value for money", "Hygienic", "Fresh food")

@Composable
fun ReviewScreen(
    orderId: String,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val order = viewModel.getOrderById(orderId)
    var restaurantRating by remember { mutableStateOf(0) }
    var foodRating by remember { mutableStateOf(0) }
    var deliveryRating by remember { mutableStateOf(0) }
    val selectedTags = remember { mutableStateListOf<String>() }
    var reviewText by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Rate Your Order", onBack = onBack)
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = Dimens.ScreenPadding)
                .verticalScrollFix()
        ) {
            Text(order?.restaurantName ?: "Your order", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(20.dp))
            RatingSection("Restaurant Rating", restaurantRating) { restaurantRating = it }
            Spacer(Modifier.height(18.dp))
            RatingSection("Food Rating", foodRating) { foodRating = it }
            Spacer(Modifier.height(18.dp))
            RatingSection("Delivery Rating", deliveryRating) { deliveryRating = it }
            Spacer(Modifier.height(20.dp))
            Text("What did you like?", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tags) { tag ->
                    val selected = selectedTags.contains(tag)
                    FilterChip(tag, selected) {
                        if (selected) selectedTags.remove(tag) else selectedTags.add(tag)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("Write a review (optional)", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                placeholder = { Text("Share more details about your experience...") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            Spacer(Modifier.height(14.dp))
            Row(
                Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(ChipGray)
                    .clickable { }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.AddAPhoto, contentDescription = null, tint = SubtleGray)
                Spacer(Modifier.width(8.dp))
                Text("Add Photos", color = SubtleGray, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(100.dp))
        }
        Surface(shadowElevation = 12.dp, color = BrandCream) {
            Column(Modifier.padding(Dimens.ScreenPadding)) {
                PrimaryButton(
                    text = "Submit Review",
                    enabled = restaurantRating > 0 || foodRating > 0 || deliveryRating > 0,
                    onClick = onSubmitted
                )
            }
        }
    }
}

@Composable
private fun RatingSection(title: String, rating: Int, onChange: (Int) -> Unit) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row {
            (1..5).forEach { star ->
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "$star star",
                    tint = if (star <= rating) StarGold else DividerGray,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .clickable { onChange(star) }
                        .padding(4.dp)
                )
            }
        }
    }
}

/*@Composable
private fun Modifier.verticalScrollFix(): Modifier =
    this.then(androidx.compose.foundation.verticalScroll(androidx.compose.foundation.rememberScrollState()))*/
@Composable
private fun Modifier.verticalScrollFix(): Modifier =
    this.verticalScroll(rememberScrollState())
