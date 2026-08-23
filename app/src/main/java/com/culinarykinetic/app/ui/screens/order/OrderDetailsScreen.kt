package com.culinarykinetic.app.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.OrderStatus
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.*

@Composable
fun OrderDetailsScreen(
    orderId: String,
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onTrack: () -> Unit,
    onReorder: () -> Unit,
    onHelp: () -> Unit,
    onReview: () -> Unit
) {
    val order = viewModel.getOrderById(orderId) ?: run { onBack(); return }
    val isDelivered = order.status == OrderStatus.DELIVERED

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Order #${order.id}", onBack = onBack)
        LazyColumn(
            Modifier.weight(1f).padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NetworkImage(order.restaurantImageUrl, modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(order.restaurantName, style = MaterialTheme.typography.titleMedium)
                        Text(order.placedAt, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                    }
                    StatusPill(
                        text = order.status.label,
                        color = if (order.status == OrderStatus.CANCELLED) ErrorRed else if (isDelivered) SuccessGreen else BrandOrange,
                        background = if (order.status == OrderStatus.CANCELLED) ErrorRed.copy(alpha = 0.1f) else if (isDelivered) SuccessGreen.copy(alpha = 0.1f) else BrandCreamAlt
                    )
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp)
                ) {
                    Text("Items", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(10.dp))
                    order.items.forEach { line ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${line.quantity}x ${line.menuItem.name}", style = MaterialTheme.typography.bodyMedium)
                            Text("\u20B9${line.lineTotal}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp)
                ) {
                    Text("Bill Details", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(10.dp))
                    PriceRow("Item Total", "\u20B9${order.itemTotal}")
                    Spacer(Modifier.height(6.dp))
                    PriceRow("Delivery Fee", "\u20B9${order.deliveryFee}")
                    Spacer(Modifier.height(6.dp))
                    PriceRow("Taxes & Fees", "\u20B9${order.taxes + order.platformFee}")
                    if (order.discount > 0) {
                        Spacer(Modifier.height(6.dp))
                        PriceRow("Discount", "-\u20B9${order.discount}", valueColor = SuccessGreen)
                    }
                    Spacer(Modifier.height(10.dp))
                    Divider(color = DividerGray)
                    Spacer(Modifier.height(10.dp))
                    PriceRow("Total Paid", "\u20B9${order.grandTotal}", isBold = true, valueColor = BrandOrange)
                    Spacer(Modifier.height(10.dp))
                    Text("Paid via ${order.paymentMethod.title}", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp)
                ) {
                    Text("Delivered To", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(6.dp))
                    Text(order.address.label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(order.address.line1, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                    Text(order.address.line2, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (!isDelivered && order.status != OrderStatus.CANCELLED) {
                        Button(
                            onClick = onTrack,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                        ) { Text("Track Order") }
                    } else {
                        Button(
                            onClick = onReorder,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                        ) {
                            Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Reorder")
                        }
                    }
                    OutlinedButton(
                        onClick = onHelp,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Icon(Icons.Filled.HelpOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Get Help")
                    }
                }
            }
            if (isDelivered) {
                item {
                    OutlinedButton(
                        onClick = onReview,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(100.dp)
                    ) { Text("Rate this order") }
                }
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}
