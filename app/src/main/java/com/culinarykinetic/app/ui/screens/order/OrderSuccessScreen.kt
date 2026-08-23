package com.culinarykinetic.app.ui.screens.order

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.SecondaryButton
import com.culinarykinetic.app.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun OrderSuccessScreen(
    viewModel: AppViewModel,
    onTrackOrder: () -> Unit,
    onBackToHome: () -> Unit,
    onViewOrder: () -> Unit
) {
    val order = viewModel.currentOrder
    val scale = remember { Animatable(0.5f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(BrandCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(110.dp)
                .scale(scale.value)
                .clip(CircleShape)
                .background(SuccessGreen.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(SuccessGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.size(38.dp)
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Order Confirmed!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Your meal from ${order?.restaurantName ?: "the restaurant"} is being prepared.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubtleGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(28.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(CardWhite)
                .clickable { onViewOrder() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(BrandCreamAlt),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.AccessTime, contentDescription = null, tint = BrandOrange)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    "EST. DELIVERY",
                    style = MaterialTheme.typography.labelSmall,
                    color = SubtleGray
                )
                Text(
                    "25 mins",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SubtleGray)
        }
        Spacer(Modifier.height(8.dp))
        order?.let {
            Text(
                "Order #${it.id}  \u2022  \u20B9${it.grandTotal}",
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray
            )
        }
        Spacer(Modifier.height(28.dp))
        PrimaryButton(text = "Track Order", trailingIcon = Icons.Filled.Map, onClick = onTrackOrder)
        Spacer(Modifier.height(12.dp))
        SecondaryButton(text = "Back to Home", onClick = onBackToHome)
    }
}
