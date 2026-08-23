package com.culinarykinetic.app.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.PaymentMethodType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.components.PriceRow
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun CheckoutScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onEditAddress: () -> Unit,
    onChoosePayment: () -> Unit,
    onPlaceOrder: () -> Unit
) {
    val address = viewModel.selectedAddress
    val restaurant = viewModel.cartRestaurant

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Checkout", onBack = onBack)
        LazyColumn(
            Modifier
                .weight(1f)
                .padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(34.dp).clip(CircleShape).background(ChipGray), contentAlignment = Alignment.Center) {
                                Icon(Icons.Filled.Home, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(18.dp))
                            }
                            Spacer(Modifier.width(10.dp))
                            Text("Delivering to ${address?.label ?: "Address"}", style = MaterialTheme.typography.titleSmall)
                        }
                        Text("EDIT", color = BrandOrange, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onEditAddress() })
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(address?.line1 ?: "No address selected", style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
                    Text(address?.line2 ?: "", style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
                    if (!address?.instructions.isNullOrBlank()) {
                        Text("Instructions: ${address?.instructions}", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Restaurant, contentDescription = null, tint = BrandOrange, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Order Summary", style = MaterialTheme.typography.titleSmall)
                    }
                    Spacer(Modifier.height(12.dp))
                    viewModel.cartItems.forEach { line ->
                        Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                            NetworkImage(line.menuItem.imageUrl, modifier = Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)))
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(restaurant?.name ?: "", style = MaterialTheme.typography.labelSmall, color = SubtleGray)
                                Text("${line.quantity}x ${line.menuItem.name}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Text("\u20B9${line.lineTotal}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Divider(color = DividerGray)
                    Spacer(Modifier.height(10.dp))
                    PriceRow("Item Total", "\u20B9${viewModel.cartItemTotal}")
                    Spacer(Modifier.height(6.dp))
                    PriceRow("Delivery Fee", if (viewModel.cartDeliveryFee == 0) "FREE" else "\u20B9${viewModel.cartDeliveryFee}")
                    Spacer(Modifier.height(6.dp))
                    PriceRow("Taxes & Platform Fee", "\u20B9${viewModel.cartTaxes + viewModel.cartPlatformFee}")
                    if (viewModel.cartDiscount > 0) {
                        Spacer(Modifier.height(6.dp))
                        PriceRow("Discount (${viewModel.selectedCoupon?.code})", "-\u20B9${viewModel.cartDiscount}", valueColor = SuccessGreen)
                    }
                    Spacer(Modifier.height(10.dp))
                    Divider(color = DividerGray)
                    Spacer(Modifier.height(10.dp))
                    PriceRow("To Pay", "\u20B9${viewModel.cartGrandTotal}", isBold = true, valueColor = BrandOrange)
                }
            }
            item {
                Column {
                    Text("Payment Method", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(10.dp))
                }
            }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .clickable { onChoosePayment() }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(38.dp).clip(CircleShape).background(ChipGray), contentAlignment = Alignment.Center) {
                            Icon(paymentIcon(viewModel.selectedPaymentMethod.type), contentDescription = null, tint = BrandOrange)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(viewModel.selectedPaymentMethod.title, style = MaterialTheme.typography.titleSmall)
                            Text(viewModel.selectedPaymentMethod.subtitle, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                        }
                        Text("CHANGE", color = BrandOrange, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
            item { Spacer(Modifier.height(100.dp)) }
        }
        Surface(shadowElevation = 12.dp, color = BrandCream) {
            Column(Modifier.padding(Dimens.ScreenPadding)) {
                PrimaryButton(
                    text = "Place Order \u2022 \u20B9${viewModel.cartGrandTotal}",
                    enabled = viewModel.cartItems.isNotEmpty() && address != null,
                    onClick = onPlaceOrder
                )
            }
        }
    }
}

private fun paymentIcon(type: PaymentMethodType) = when (type) {
    PaymentMethodType.UPI -> Icons.Filled.AccountBalanceWallet
    PaymentMethodType.CARD -> Icons.Filled.CreditCard
    PaymentMethodType.NETBANKING -> Icons.Filled.AccountBalance
    PaymentMethodType.WALLET -> Icons.Filled.Wallet
    PaymentMethodType.COD -> Icons.Filled.Money
}
