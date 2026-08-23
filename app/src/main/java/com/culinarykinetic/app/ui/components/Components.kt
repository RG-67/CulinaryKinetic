package com.culinarykinetic.app.ui.components

import android.R.attr.onClick
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.culinarykinetic.app.model.Restaurant
import com.culinarykinetic.app.ui.theme.*

@Composable
fun NetworkImage(
    url: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(Dimens.ButtonCorner),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandOrange,
            contentColor = Color.White,
            disabledContainerColor = BrandOrange.copy(alpha = 0.4f)
        )
    ) {
        Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (trailingIcon != null) {
            Spacer(Modifier.width(8.dp))
            Icon(trailingIcon, contentDescription = null, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(Dimens.ButtonCorner),
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerGray),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CharcoalText)
    ) {
        Text(text, style = MaterialTheme.typography.titleSmall)
    }
}

@Composable
fun ScreenTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    titleColor: Color = BrandOrange
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = CharcoalText
                )
            }
        } else {
            Spacer(Modifier.width(48.dp))
        }
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall,
            color = titleColor,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(horizontalArrangement = Arrangement.End) { actions() }
    }
}

@Composable
fun RatingBadge(rating: Double, modifier: Modifier = Modifier, filled: Boolean = true) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (filled) InkBlack.copy(alpha = 0.75f) else Color.Transparent)
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.Star,
            contentDescription = null,
            tint = StarGold,
            modifier = Modifier.size(12.dp)
        )
        Spacer(Modifier.width(2.dp))
        Text(
            rating.toString(),
            color = if (filled) Color.White else CharcoalText,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun FavoriteToggleButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    background: Color = Color.White
) {
    val scale by animateFloatAsState(if (isFavorite) 1.15f else 1f, label = "fav_scale")
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(background.copy(alpha = 0.9f))
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) BrandRed else SubtleGray,
            modifier = Modifier
                .size(18.dp)
                .scale(scale)
        )
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CardCorner))
            .background(CardWhite)
            .clickable { onClick() }
    ) {
        Box {
            NetworkImage(
                restaurant.coverImageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = Dimens.CardCorner,
                            topEnd = Dimens.CardCorner
                        )
                    )
            )
            if (restaurant.offerText != null) {
                Text(
                    restaurant.offerText.let { if (it.startsWith("50%")) "50% OFF" else it.take(14) },
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(BrandRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            FavoriteToggleButton(
                isFavorite = isFavorite,
                onToggle = onFavoriteToggle,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RatingBadge(restaurant.rating)
                Text(
                    restaurant.deliveryTimeMinutes,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(InkBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
            if (restaurant.isPromoted) {
                Text(
                    "PROMOTED",
                    color = BrandOrangeDark,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(BrandCreamAlt)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        Column(Modifier.padding(12.dp)) {
            Text(
                restaurant.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                restaurant.cuisines.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun RestaurantListCard(
    restaurant: Restaurant,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Dimens.CardCorner))
            .background(CardWhite)
            .clickable { onClick() }
    ) {
        Box {
            NetworkImage(
                restaurant.coverImageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = Dimens.CardCorner,
                            topEnd = Dimens.CardCorner
                        )
                    )
            )
            if (restaurant.offerText != null) {
                Text(
                    if (restaurant.offerText.startsWith("50%")) "50% OFF" else if (restaurant.freeDelivery) "Free Delivery" else restaurant.offerText,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(BrandRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            FavoriteToggleButton(
                isFavorite = isFavorite,
                onToggle = onFavoriteToggle,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            )
            Row(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RatingBadge(restaurant.rating)
                Text(
                    restaurant.deliveryTimeMinutes,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(InkBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
        Column(Modifier.padding(14.dp)) {
            Text(restaurant.name, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(2.dp))
            Text(
                restaurant.cuisines.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = SubtleGray,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    " ${restaurant.distanceKm} km",
                    style = MaterialTheme.typography.bodySmall,
                    color = SubtleGray
                )
                Text(
                    "  \u2022  ${restaurant.priceLevel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SubtleGray
                )
            }
        }
    }
}

@Composable
fun CategoryChip(name: String, imageUrl: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            NetworkImage(imageUrl, modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape))
        }
        Spacer(Modifier.height(6.dp))
        Text(name, style = MaterialTheme.typography.labelMedium, color = CharcoalText)
    }
}

@Composable
fun FilterChip(label: String, selected: Boolean, icon: ImageVector? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(Dimens.ChipCorner))
            .background(if (selected) BrandOrange else ChipGray)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (selected) Color.White else CharcoalText,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(
            label,
            color = if (selected) Color.White else CharcoalText,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(ChipGray)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onDecrement, modifier = Modifier.size(28.dp)) {
            Icon(
                Icons.Filled.Remove,
                contentDescription = "Decrease",
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            quantity.toString(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        IconButton(onClick = onIncrement, modifier = Modifier.size(28.dp)) {
            Icon(Icons.Filled.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun PriceRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = CharcoalText
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = if (isBold) CharcoalText else SubtleGray
        )
        Text(
            value,
            style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun SectionHeader(title: String, actionText: String? = null, onAction: (() -> Unit)? = null) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        if (actionText != null) {
            Text(
                actionText,
                style = MaterialTheme.typography.labelLarge,
                color = BrandOrange,
                modifier = Modifier.clickable { onAction?.invoke() }
            )
        }
    }
}

@Composable
fun EmptyState(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = SubtleGray,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = SubtleGray,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
        if (actionText != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = actionText,
                onClick = onAction,
                modifier = Modifier.padding(horizontal = 40.dp)
            )
        }
    }
}

@Composable
fun StatusPill(text: String, color: Color, background: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(background)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier
            .size(6.dp)
            .clip(CircleShape)
            .background(color))
        Spacer(Modifier.width(6.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LoadingOverlay(message: String = "Loading...") {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrandCream),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = BrandOrange)
        Spacer(Modifier.height(16.dp))
        Text(message, color = SubtleGray, style = MaterialTheme.typography.bodyMedium)
    }
}
