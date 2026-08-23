package com.culinarykinetic.app.ui.screens.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.Order
import com.culinarykinetic.app.model.OrderStatus
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.EmptyState
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun OrdersScreen(
    viewModel: AppViewModel,
    onBack: (() -> Unit)?,
    onOrderDetails: (String) -> Unit,
    onTrackOrder: (String) -> Unit,
    onReorder: (String) -> Unit
) {
    var tab by remember { mutableStateOf(0) }
    val active = viewModel.orderHistory.filter { it.status != OrderStatus.DELIVERED && it.status != OrderStatus.CANCELLED }
    val past = viewModel.orderHistory.filter { it.status == OrderStatus.DELIVERED || it.status == OrderStatus.CANCELLED }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Orders", onBack = onBack)
        TabRow(
            selectedTabIndex = tab,
            containerColor = BrandCream,
            contentColor = BrandOrange,
            indicator = { positions ->
                TabRowDefaults.Indicator(Modifier.tabIndicatorOffset(positions[tab]), color = BrandOrange)
            }
        ) {
            Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Active", fontWeight = FontWeight.Bold) })
            Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Past", fontWeight = FontWeight.Bold) })
        }
        val list = if (tab == 0) active else past
        if (list.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Receipt,
                title = if (tab == 0) "No active orders" else "No past orders yet",
                message = if (tab == 0) "Orders you place will show up here in real time." else "Your delivered and cancelled orders will appear here."
            )
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(Modifier.height(6.dp)) }
                items(list, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        onClick = { onOrderDetails(order.id) },
                        onTrack = { onTrackOrder(order.id) },
                        onReorder = { onReorder(order.id) }
                    )
                }
                item { Spacer(Modifier.height(90.dp)) }
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit, onTrack: () -> Unit, onReorder: () -> Unit) {
    val isActive = order.status != OrderStatus.DELIVERED && order.status != OrderStatus.CANCELLED
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            NetworkImage(order.restaurantImageUrl, modifier = Modifier.size(56.dp).clip(RoundedCornerShape(12.dp)))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(order.restaurantName, style = MaterialTheme.typography.titleSmall)
                    Text("\u20B9${order.grandTotal}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (order.status == OrderStatus.CANCELLED) Icons.Filled.CheckCircle else Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = if (order.status == OrderStatus.CANCELLED) ErrorRed else if (order.status == OrderStatus.DELIVERED) SuccessGreen else BrandOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        " ${order.status.label} \u2022 ${order.placedAt.substringBefore(",")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubtleGray
                    )
                }
                Text(
                    order.items.joinToString(", ") { "${it.menuItem.name} x${it.quantity}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = SubtleGray,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CharcoalText)
            ) {
                Icon(Icons.Filled.Receipt, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("View Details")
            }
            if (isActive) {
                Button(
                    onClick = onTrack,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                ) { Text("Track") }
            } else {
                Button(
                    onClick = onReorder,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(100.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange)
                ) {
                    Icon(Icons.Filled.Replay, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                    Spacer(Modifier.width(6.dp))
                    Text("Reorder", color = Color.White)
                }
            }
        }
    }
}
