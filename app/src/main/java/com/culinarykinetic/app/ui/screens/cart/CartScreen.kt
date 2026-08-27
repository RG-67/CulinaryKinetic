package com.culinarykinetic.app.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.*
import com.culinarykinetic.app.ui.theme.*
import androidx.compose.foundation.border
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

@Composable
fun CartScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onAddMoreItems: () -> Unit,
    onApplyCouponClick: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Your Cart", onBack = onBack, titleColor = com.culinarykinetic.app.ui.theme.InkBlack)

        if (viewModel.cartItems.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.RemoveShoppingCart,
                title = "Your cart is empty",
                message = "Looks like you haven't added anything yet. Browse restaurants to get started.",
                actionText = "Browse Restaurants",
                onAction = onAddMoreItems
            )
            return@Column
        }

        LazyColumn(
            Modifier
                .weight(1f)
                .padding(horizontal = Dimens.ScreenPadding)
        ) {
            item {
                viewModel.cartRestaurant?.let {
                    Text(it.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(10.dp))
                }
            }
            items(viewModel.cartItems, key = { it.id }) { line ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NetworkImage(line.menuItem.imageUrl, modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(line.menuItem.name, style = MaterialTheme.typography.titleSmall)
                        if (line.customizationSummary.isNotBlank()) {
                            Text(line.customizationSummary, style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text("\u20B9${line.lineTotal}", style = MaterialTheme.typography.titleSmall, color = BrandOrange, fontWeight = FontWeight.Bold)
                    }
                    QuantitySelector(
                        quantity = line.quantity,
                        onIncrement = { viewModel.updateQuantity(line.id, 1) },
                        onDecrement = { viewModel.updateQuantity(line.id, -1) }
                    )
                }
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(androidx.compose.ui.graphics.Color.Transparent)
                        .dashedBorder(1.dp, BrandOrange)
                        .clickable { onAddMoreItems() }
                        .padding(vertical = 14.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = BrandOrange)
                    Spacer(Modifier.width(6.dp))
                    Text("Add more items", color = BrandOrange, style = MaterialTheme.typography.titleSmall)
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(CardWhite)
                        .clickable { onApplyCouponClick() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.LocalOffer, contentDescription = null, tint = BrandOrange)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            viewModel.selectedCoupon?.let { "${it.code} applied" } ?: "Apply Coupon",
                            style = MaterialTheme.typography.titleSmall
                        )
                        if (viewModel.selectedCoupon != null) {
                            Text("You saved \u20B9${viewModel.cartDiscount}", style = MaterialTheme.typography.bodySmall, color = SuccessGreen)
                        }
                    }
                    Text(if (viewModel.selectedCoupon != null) "CHANGE" else "APPLY", color = BrandOrange, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardWhite)
                        .padding(16.dp)
                ) {
                    Text("Bill Summary", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(12.dp))
                    PriceRow("Item Total", "\u20B9${viewModel.cartItemTotal}")
                    Spacer(Modifier.height(8.dp))
                    PriceRow("Delivery Fee", if (viewModel.cartDeliveryFee == 0) "FREE" else "\u20B9${viewModel.cartDeliveryFee}", valueColor = if (viewModel.cartDeliveryFee == 0) SuccessGreen else CharcoalText)
                    Spacer(Modifier.height(8.dp))
                    PriceRow("Taxes & Fees", "\u20B9${viewModel.cartTaxes + viewModel.cartPlatformFee}")
                    if (viewModel.cartDiscount > 0) {
                        Spacer(Modifier.height(8.dp))
                        PriceRow("Coupon Discount", "-\u20B9${viewModel.cartDiscount}", valueColor = SuccessGreen)
                    }
                    Spacer(Modifier.height(12.dp))
                    Divider(color = DividerGray)
                    Spacer(Modifier.height(12.dp))
                    PriceRow("Grand Total", "\u20B9${viewModel.cartGrandTotal}", isBold = true, valueColor = BrandOrange)
                }
            }
            item { Spacer(Modifier.height(110.dp)) }
        }

        Surface(shadowElevation = 12.dp, color = BrandCream, modifier = Modifier.navigationBarsPadding()) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.ScreenPadding, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text("Total", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
                    Text("\u20B9${viewModel.cartGrandTotal}", style = MaterialTheme.typography.titleLarge)
                }
                PrimaryButton(
                    text = "Proceed to Checkout",
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward,
                    modifier = Modifier.weight(1f),
                    onClick = onCheckout
                )
            }
        }
    }
}

/*
private fun Modifier.dashedBorder(width: androidx.compose.ui.unit.Dp, color: androidx.compose.ui.graphics.Color): Modifier =
    this.then(
        androidx.compose.foundation.border(
            width = width,
            color = color,
            shape = RoundedCornerShape(14.dp)
        )
    )
*/

private fun Modifier.dashedBorder(
    width: Dp,
    color: Color,
    cornerRadius: Dp = 14.dp
): Modifier = drawBehind {
    val strokeWidth = width.toPx()

    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(cornerRadius.toPx()),
        style = Stroke(
            width = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(10f, 10f),
                0f
            ),
            cap = StrokeCap.Round
        )
    )
}
