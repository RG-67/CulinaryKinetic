package com.culinarykinetic.app.ui.screens.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun OffersScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Offers for you", onBack = onBack)
        LazyColumn(
            Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(6.dp)) }
            item {
                Text("Coupons", style = MaterialTheme.typography.titleLarge)
            }
            items(viewModel.coupons) { coupon ->
                OfferBanner(
                    icon = Icons.Filled.LocalOffer,
                    title = coupon.code,
                    subtitle = "${coupon.title} \u2022 Min order \u20B9${coupon.minOrder}",
                    accent = BrandOrange
                )
            }
            item { Spacer(Modifier.height(6.dp)) }
            item { Text("Bank Offers", style = MaterialTheme.typography.titleLarge) }
            item {
                OfferBanner(
                    icon = Icons.Filled.AccountBalance,
                    title = "10% instant discount",
                    subtitle = "On HDFC Bank Credit & Debit cards, up to \u20B9150",
                    accent = SuccessGreen
                )
            }
            item {
                OfferBanner(
                    icon = Icons.Filled.AccountBalance,
                    title = "5% cashback",
                    subtitle = "On ICICI Bank UPI payments above \u20B9299",
                    accent = SuccessGreen
                )
            }
            item { Text("Restaurant Offers", style = MaterialTheme.typography.titleLarge) }
            item {
                OfferBanner(
                    icon = Icons.Filled.Storefront,
                    title = "Buy 1 Get 1",
                    subtitle = "On starters at Burger District, today only",
                    accent = BrandRed
                )
            }
            item {
                OfferBanner(
                    icon = Icons.Filled.DeliveryDining,
                    title = "Free delivery weekend",
                    subtitle = "No delivery fee on all orders above \u20B9199",
                    accent = BrandOrangeDark
                )
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun OfferBanner(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, accent: androidx.compose.ui.graphics.Color) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = accent)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
        }
    }
}
