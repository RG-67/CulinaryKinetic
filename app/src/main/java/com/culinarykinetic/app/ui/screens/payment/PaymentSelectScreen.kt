package com.culinarykinetic.app.ui.screens.payment

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.PaymentMethod
import com.culinarykinetic.app.model.PaymentMethodType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun PaymentSelectScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onProceed: () -> Unit
) {
    var selected by remember { mutableStateOf(viewModel.selectedPaymentMethod) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Choose Payment", onBack = onBack)
        LazyColumn(
            Modifier.weight(1f).padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }
            items(viewModel.paymentMethods) { method ->
                PaymentMethodRow(method, selected == method) { selected = method }
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
        Surface(shadowElevation = 12.dp, color = BrandCream) {
            Column(Modifier.padding(Dimens.ScreenPadding)) {
                PrimaryButton(
                    text = "Confirm \u2022 \u20B9${viewModel.cartGrandTotal}",
                    onClick = {
                        viewModel.selectPaymentMethod(selected)
                        onProceed()
                    }
                )
            }
        }
    }
}

@Composable
private fun PaymentMethodRow(method: PaymentMethod, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) BrandCreamAlt else CardWhite)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(ChipGray), contentAlignment = Alignment.Center) {
            Icon(paymentIcon(method.type), contentDescription = null, tint = BrandOrange)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(method.title, style = MaterialTheme.typography.titleSmall)
            Text(method.subtitle, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
        }
        RadioButton(selected = selected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = BrandOrange))
    }
}

private fun paymentIcon(type: PaymentMethodType) = when (type) {
    PaymentMethodType.UPI -> Icons.Filled.AccountBalanceWallet
    PaymentMethodType.CARD -> Icons.Filled.CreditCard
    PaymentMethodType.NETBANKING -> Icons.Filled.AccountBalance
    PaymentMethodType.WALLET -> Icons.Filled.Wallet
    PaymentMethodType.COD -> Icons.Filled.Money
}
