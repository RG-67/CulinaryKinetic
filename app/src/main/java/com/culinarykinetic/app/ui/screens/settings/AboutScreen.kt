package com.culinarykinetic.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "About", onBack = onBack)
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            Box(
                Modifier.size(80.dp).clip(RoundedCornerShape(20.dp)).background(BrandCreamAlt),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Restaurant, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(36.dp))
            }
            Spacer(Modifier.height(16.dp))
            Text("Culinary Kinetic", style = MaterialTheme.typography.headlineMedium)
            Text("Version 1.0.0 (Demo)", style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
            Spacer(Modifier.height(20.dp))
            Text(
                "Culinary Kinetic is a demo food-delivery experience showcasing a complete Jetpack Compose UI/UX journey \u2014 from discovery to delivery \u2014 built entirely with local mock data.",
                style = MaterialTheme.typography.bodyMedium,
                color = CharcoalText,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            Text(
                "Built with Kotlin, Jetpack Compose, Material 3, Navigation Compose and Coil.",
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray,
                textAlign = TextAlign.Center
            )
        }
    }
}
