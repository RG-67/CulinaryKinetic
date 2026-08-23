package com.culinarykinetic.app.ui.screens.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.AppNotification
import com.culinarykinetic.app.model.NotificationType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.EmptyState
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun NotificationsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(
            title = "Notifications",
            onBack = onBack,
            actions = {
                if (viewModel.notifications.any { !it.isRead }) {
                    Text(
                        "Mark all read",
                        color = BrandOrange,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.clickable { viewModel.markAllNotificationsRead() }.padding(end = 8.dp)
                    )
                }
            }
        )
        if (viewModel.notifications.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.NotificationsNone,
                title = "No notifications",
                message = "You're all caught up! We'll let you know when something new happens."
            )
        } else {
            LazyColumn(
                Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(Modifier.height(6.dp)) }
                items(viewModel.notifications, key = { it.id }) { notification ->
                    NotificationRow(notification) { viewModel.markNotificationRead(notification.id) }
                }
                item { Spacer(Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
private fun NotificationRow(notification: AppNotification, onClick: () -> Unit) {
    val (icon, accent) = when (notification.type) {
        NotificationType.ORDER -> Icons.Filled.Receipt to BrandOrange
        NotificationType.PAYMENT -> Icons.Filled.Payments to SuccessGreen
        NotificationType.OFFER -> Icons.Filled.LocalOffer to BrandRed
        NotificationType.PROMO -> Icons.Filled.Campaign to WarningAmber
        NotificationType.DELIVERY -> Icons.Filled.DeliveryDining to BrandOrangeDark
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (notification.isRead) CardWhite else BrandCreamAlt)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(accent.copy(alpha = 0.12f)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(notification.title, style = MaterialTheme.typography.titleSmall, fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal)
            Spacer(Modifier.height(2.dp))
            Text(notification.message, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
            Spacer(Modifier.height(4.dp))
            Text(notification.time, style = MaterialTheme.typography.labelSmall, color = SubtleGray)
        }
        if (!notification.isRead) {
            Box(Modifier.padding(top = 4.dp).size(8.dp).clip(CircleShape).background(BrandRed))
        }
    }
}
