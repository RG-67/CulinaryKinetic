package com.culinarykinetic.app.ui.screens.help

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*
import androidx.compose.material.icons.filled.ChevronRight

private data class HelpTopic(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val topics = listOf(
    HelpTopic("Order Issues", Icons.Filled.Receipt),
    HelpTopic("Payment Issues", Icons.Filled.Payments),
    HelpTopic("Missing Item", Icons.Filled.RemoveShoppingCart),
    HelpTopic("Wrong Item Delivered", Icons.Filled.ErrorOutline),
    HelpTopic("Refund Status", Icons.Filled.CurrencyExchange),
    HelpTopic("Delivery Issue", Icons.Filled.DeliveryDining)
)

@Composable
fun HelpScreen(onBack: () -> Unit, onTopicClick: (String) -> Unit, onChatSupport: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Help & Support", onBack = onBack)
        LazyColumn(Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding)) {
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BrandCreamAlt)
                        .clickable { onChatSupport() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(42.dp).clip(CircleShape).background(BrandOrange), contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Chat with Support", style = MaterialTheme.typography.titleSmall)
                        Text("Usually replies within 2 minutes", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SubtleGray)
                }
                Spacer(Modifier.height(20.dp))
                Text("Common Issues", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(10.dp))
            }
            items(topics) { topic ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardWhite)
                        .clickable { onTopicClick(topic.title) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(topic.icon, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(topic.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SubtleGray)
                }
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun HelpIssueScreen(topic: String, onBack: () -> Unit, onSubmit: () -> Unit) {
    var message by remember { androidx.compose.runtime.mutableStateOf("") }
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = topic, onBack = onBack)
        Column(Modifier.weight(1f).padding(Dimens.ScreenPadding)) {
            Text("Tell us what happened", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth().height(160.dp),
                placeholder = { Text("Describe the issue in detail...") },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            Spacer(Modifier.height(14.dp))
            Text(
                "Our support team typically responds within 24 hours. For urgent order issues, use Chat Support instead.",
                style = MaterialTheme.typography.bodySmall, color = SubtleGray
            )
        }
        Surface(shadowElevation = 12.dp) {
            Column(Modifier.padding(Dimens.ScreenPadding)) {
                PrimaryButton(text = "Submit Request", onClick = onSubmit)
            }
        }
    }
}
