package com.culinarykinetic.app.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.material.icons.filled.ChevronRight

@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onAbout: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Settings", onBack = onBack)
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = Dimens.ScreenPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(6.dp))
            SettingsSectionLabel("Notifications")
            SettingsGroup {
                SettingsSwitchRow(
                    Icons.Filled.NotificationsActive,
                    "Push Notifications",
                    viewModel.pushNotificationsEnabled
                ) {
                    viewModel.pushNotificationsEnabled = it
                }
                SettingsSwitchRow(
                    Icons.Filled.Campaign,
                    "Promotional Notifications",
                    viewModel.promoNotificationsEnabled
                ) {
                    viewModel.promoNotificationsEnabled = it
                }
                SettingsSwitchRow(
                    Icons.Filled.Receipt,
                    "Order Update Alerts",
                    viewModel.orderUpdatesEnabled
                ) {
                    viewModel.orderUpdatesEnabled = it
                }
            }
            Spacer(Modifier.height(20.dp))
            SettingsSectionLabel("Appearance")
            SettingsGroup {
                SettingsSwitchRow(Icons.Filled.DarkMode, "Dark Mode", viewModel.darkModeEnabled) {
                    viewModel.darkModeEnabled = it
                }
                SettingsNavRow(Icons.Filled.Language, "Language", viewModel.selectedLanguage) { }
            }
            Spacer(Modifier.height(20.dp))
            SettingsSectionLabel("Account")
            SettingsGroup {
                SettingsNavRow(Icons.Filled.Lock, "Privacy & Security", "") { }
                SettingsNavRow(
                    Icons.Filled.LocationOn,
                    "Location Settings",
                    viewModel.currentLocationLabel
                ) { }
                SettingsNavRow(Icons.Filled.CreditCard, "Payment Settings", "") { }
            }
            Spacer(Modifier.height(20.dp))
            SettingsSectionLabel("About")
            SettingsGroup {
                SettingsNavRow(Icons.Filled.Info, "About Culinary Kinetic", "") { onAbout() }
                SettingsNavRow(Icons.Filled.Description, "Terms of Service", "") { }
                SettingsNavRow(Icons.Filled.PrivacyTip, "Privacy Policy", "") { }
            }
            Spacer(Modifier.height(90.dp))
        }
    }
}

@Composable
private fun SettingsSectionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.labelLarge, color = SubtleGray)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardWhite)
    ) { content() }
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun SettingsSwitchRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = BrandOrange,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                checkedTrackColor = BrandOrange
            )
        )
    }
}

@Composable
private fun SettingsNavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = BrandOrange,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (value.isNotBlank()) {
            Text(value, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
            Spacer(Modifier.width(6.dp))
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = SubtleGray)
    }
}

