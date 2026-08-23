package com.culinarykinetic.app.ui.screens.tracking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.OrderStatus
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.theme.*

@Composable
fun OrderTrackingScreen(
    orderId: String,
    viewModel: AppViewModel,
    onClose: () -> Unit
) {
    val order = viewModel.getOrderById(orderId) ?: run {
        onClose()
        return
    }

    val etaText = when (order.status) {
        OrderStatus.PLACED -> "Est. 35 mins"
        OrderStatus.CONFIRMED -> "Est. 30 mins"
        OrderStatus.PREPARING -> "Est. 25 mins"
        OrderStatus.READY -> "Est. 20 mins"
        OrderStatus.PICKED_UP -> "Est. 18 mins"
        OrderStatus.ON_THE_WAY -> "Arriving in 15 mins"
        OrderStatus.DELIVERED -> "Delivered"
        OrderStatus.CANCELLED -> "Cancelled"
    }

    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(0.42f)
            ) {
                SimulatedMap(status = order.status)
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = "Close", tint = InkBlack)
                }
                Text(
                    "Track Order",
                    style = MaterialTheme.typography.titleLarge,
                    color = BrandOrangeDark,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)
                )
                Row(
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 60.dp, start = 12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.AccessTime, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(etaText, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(0.58f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(BrandCream)
                    .padding(24.dp)
            ) {
                Box(
                    Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(2.dp))
                        .background(DividerGray)
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    if (order.status == OrderStatus.DELIVERED) "Delivered!" else etaText,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    orderStatusMessage(order.status),
                    style = MaterialTheme.typography.bodyMedium,
                    color = SubtleGray
                )
                Spacer(Modifier.height(20.dp))
                TrackingTimeline(status = order.status)
                Spacer(Modifier.height(20.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(46.dp).clip(CircleShape).background(ChipGray), contentAlignment = Alignment.Center) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = SubtleGray)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(order.deliveryPartnerName, style = MaterialTheme.typography.titleSmall)
                        Text(order.deliveryPartnerVehicle, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = StarGold, modifier = Modifier.size(12.dp))
                            Text(" ${order.deliveryPartnerRating}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    IconButton(onClick = {}, modifier = Modifier.clip(CircleShape).background(ChipGray)) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "Chat", tint = CharcoalText)
                    }
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = {}, modifier = Modifier.clip(CircleShape).background(BrandOrange)) {
                        Icon(Icons.Filled.Call, contentDescription = "Call", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(12.dp))
                if (order.status != OrderStatus.DELIVERED) {
                    Text(
                        "Demo: advance status \u2192",
                        color = BrandOrange,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.clickable { viewModel.advanceCurrentOrderManually() }
                    )
                }
            }
        }
    }
}

private fun orderStatusMessage(status: OrderStatus): String = when (status) {
    OrderStatus.PLACED -> "Your order has been placed."
    OrderStatus.CONFIRMED -> "The restaurant confirmed your order."
    OrderStatus.PREPARING -> "Your order is on the way."
    OrderStatus.READY -> "Your order is ready for pickup."
    OrderStatus.PICKED_UP -> "Your delivery partner picked up the order."
    OrderStatus.ON_THE_WAY -> "Your order is on the way."
    OrderStatus.DELIVERED -> "Enjoy your meal!"
    OrderStatus.CANCELLED -> "This order was cancelled."
}

@Composable
private fun TrackingTimeline(status: OrderStatus) {
    val steps = listOf(
        Triple("Placed", Icons.Filled.Check, OrderStatus.PLACED),
        Triple("Preparing", Icons.Filled.Restaurant, OrderStatus.PREPARING),
        Triple("On the way", Icons.Filled.DeliveryDining, OrderStatus.ON_THE_WAY)
    )
    val activeIndex = when (status) {
        OrderStatus.PLACED, OrderStatus.CONFIRMED -> 0
        OrderStatus.PREPARING, OrderStatus.READY -> 1
        OrderStatus.PICKED_UP, OrderStatus.ON_THE_WAY -> 2
        OrderStatus.DELIVERED -> 3
        OrderStatus.CANCELLED -> -1
    }
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        steps.forEachIndexed { index, (label, icon, _) ->
            val done = index < activeIndex
            val active = index == activeIndex
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(
                    Modifier
                        .size(if (active) 44.dp else 38.dp)
                        .clip(CircleShape)
                        .background(if (done || active) BrandRed else Color.White)
                        .then(if (!done && !active) Modifier else Modifier),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (done) Icons.Filled.Check else icon,
                        contentDescription = label,
                        tint = if (done || active) Color.White else SubtleGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(label, style = MaterialTheme.typography.labelSmall, color = if (active) BrandRed else SubtleGray, fontWeight = if (active) FontWeight.Bold else FontWeight.Normal)
            }
            if (index != steps.size - 1) {
                Box(
                    Modifier
                        .weight(0.6f)
                        .height(2.dp)
                        .background(if (index < activeIndex) BrandRed else DividerGray)
                )
            }
        }
    }
}

@Composable
private fun SimulatedMap(status: OrderStatus) {
    Box(
        Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFFDCE9E3))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val roadColor = Color.White
            // Horizontal + vertical faux streets
            for (i in 1..4) {
                drawLine(
                    color = roadColor,
                    start = Offset(0f, size.height * i / 5),
                    end = Offset(size.width, size.height * i / 5),
                    strokeWidth = 10f
                )
            }
            for (i in 1..3) {
                drawLine(
                    color = roadColor,
                    start = Offset(size.width * i / 4, 0f),
                    end = Offset(size.width * i / 4, size.height),
                    strokeWidth = 10f
                )
            }
            // Route path
            drawLine(
                color = BrandRed,
                start = Offset(size.width * 0.2f, size.height * 0.75f),
                end = Offset(size.width * 0.7f, size.height * 0.3f),
                strokeWidth = 6f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 12f), 0f)
            )
        }
        // Restaurant marker
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .padding(start = 40.dp, bottom = 60.dp)
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Storefront, contentDescription = null, tint = BrandOrangeDark, modifier = Modifier.size(16.dp))
        }
        // Delivery marker (position shifts with status)
        val progress = when (status) {
            OrderStatus.PLACED, OrderStatus.CONFIRMED -> 0.15f
            OrderStatus.PREPARING -> 0.35f
            OrderStatus.READY, OrderStatus.PICKED_UP -> 0.6f
            OrderStatus.ON_THE_WAY -> 0.85f
            else -> 1f
        }
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(end = 60.dp, top = 40.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(BrandRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.DeliveryDining, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        // Destination marker
        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(SuccessGreen)
        )
    }
}
