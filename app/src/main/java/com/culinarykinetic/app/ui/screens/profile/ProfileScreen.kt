package com.culinarykinetic.app.ui.screens.profile

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onEditProfile: () -> Unit,
    onOrders: () -> Unit,
    onFavorites: () -> Unit,
    onAddresses: () -> Unit,
    onPayment: () -> Unit,
    onOffers: () -> Unit,
    onNotifications: () -> Unit,
    onHelp: () -> Unit,
    onSettings: () -> Unit,
    onLogOut: () -> Unit
) {
    val user = viewModel.currentUser
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(horizontal = Dimens.ScreenPadding)
    ) {
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                NetworkImage(
                    user.avatarUrl, modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(user.name, style = MaterialTheme.typography.headlineSmall)
                    Text(
                        user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = SubtleGray
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Edit Profile",
                        color = BrandOrange,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(ChipGray)
                            .clickable { onEditProfile() }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        item { Spacer(Modifier.height(20.dp)) }
        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(BrandOrange)
                    .clickable { }
                    .padding(18.dp)
            ) {
                Spacer(Modifier.padding(top = 10.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text(
                            "Culinary Rewards",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            user.tier,
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${user.loyaltyPoints}",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "POINTS",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                LinearProgressIndicator(
                    progress = 0.82f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${user.pointsToNextTier} pts to Platinum",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(Modifier.padding(bottom = 10.dp))
            }
        }
        item { Spacer(Modifier.height(18.dp)) }
        item {
            ProfileRow(icon = Icons.Filled.Receipt, title = "My Orders", onClick = onOrders)
        }
        item { Spacer(Modifier.height(14.dp)) }
        item {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.height(220.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                userScrollEnabled = false
            ) {
                items(
                    listOf(
                        Triple("Favorites", Icons.Filled.FavoriteBorder, onFavorites),
                        Triple("Addresses", Icons.Filled.LocationOn, onAddresses),
                        Triple("Payment", Icons.Filled.CreditCard, onPayment),
                        Triple("Offers", Icons.Filled.LocalOffer, onOffers)
                    )
                ) { (title, icon, action) ->
                    ProfileGridItem(title, icon, action)
                }
            }
        }
        item { Spacer(Modifier.height(14.dp)) }
        item {
            ProfileRow(
                icon = Icons.Filled.NotificationsNone,
                title = "Notifications",
                onClick = onNotifications
            )
        }
        item { Spacer(Modifier.height(10.dp)) }
        item { ProfileRow(icon = Icons.Filled.HelpOutline, title = "Support", onClick = onHelp) }
        item { Spacer(Modifier.height(10.dp)) }
        item { ProfileRow(icon = Icons.Filled.Settings, title = "Settings", onClick = onSettings) }
        item { Spacer(Modifier.height(24.dp)) }
        item {
            Text(
                "Log Out",
                color = ErrorRed,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogOut() }
                    .padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
        item { Spacer(Modifier.height(50.dp)) }
    }
}

@Composable
private fun ProfileRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
            .border(width = 1.dp, color = ChipGray, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BrandOrange)
        }
        Spacer(Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SubtleGray)
    }
}

@Composable
private fun ProfileGridItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = ChipGray, shape = RoundedCornerShape(16.dp))
            .background(CardWhite)
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BrandOrange)
        }
        Spacer(Modifier.height(10.dp))
        Text(title, style = MaterialTheme.typography.titleSmall)
    }
}
