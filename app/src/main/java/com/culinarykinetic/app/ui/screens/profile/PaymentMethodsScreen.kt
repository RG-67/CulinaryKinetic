package com.culinarykinetic.app.ui.screens.profile

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
import com.culinarykinetic.app.model.PaymentMethodType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun PaymentMethodsScreen(viewModel: AppViewModel, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Payment Methods", onBack = onBack)
        LazyColumn(
            Modifier.fillMaxSize().padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(6.dp)) }
            items(viewModel.paymentMethods) { method ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (viewModel.selectedPaymentMethod == method) BrandCreamAlt else CardWhite)
                        .clickable { viewModel.selectPaymentMethod(method) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(40.dp).clip(CircleShape).background(ChipGray), contentAlignment = Alignment.Center) {
                        Icon(
                            when (method.type) {
                                PaymentMethodType.UPI -> Icons.Filled.AccountBalanceWallet
                                PaymentMethodType.CARD -> Icons.Filled.CreditCard
                                PaymentMethodType.NETBANKING -> Icons.Filled.AccountBalance
                                PaymentMethodType.WALLET -> Icons.Filled.Wallet
                                PaymentMethodType.COD -> Icons.Filled.Money
                            },
                            contentDescription = null, tint = BrandOrange
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(method.title, style = MaterialTheme.typography.titleSmall)
                        Text(method.subtitle, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                    }
                    if (viewModel.selectedPaymentMethod == method) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Default", tint = SuccessGreen)
                    }
                }
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                        .clickable { }
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = BrandOrange)
                    Spacer(Modifier.width(6.dp))
                    Text("Add New Payment Method", color = BrandOrange, fontWeight = FontWeight.Bold)
                }
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}
