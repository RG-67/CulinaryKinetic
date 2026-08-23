package com.culinarykinetic.app.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.Coupon
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*

@Composable
fun CouponsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    var code by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Offers & Coupons", onBack = onBack)
        Column(Modifier.padding(horizontal = Dimens.ScreenPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it; errorText = null },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Enter coupon code") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
                )
                Spacer(Modifier.width(10.dp))
                Button(
                    onClick = {
                        if (viewModel.applyCouponCode(code)) {
                            onBack()
                        } else {
                            errorText = "Invalid code or minimum order not met"
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandOrange),
                    modifier = Modifier.height(56.dp)
                ) { Text("Apply") }
            }
            if (errorText != null) {
                Spacer(Modifier.height(6.dp))
                Text(errorText!!, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            Modifier.padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Available Offers", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(6.dp))
            }
            items(viewModel.coupons) { coupon ->
                CouponCard(
                    coupon = coupon,
                    applied = viewModel.selectedCoupon?.code == coupon.code,
                    eligible = viewModel.cartItemTotal >= coupon.minOrder || viewModel.cartItemTotal == 0,
                    onApply = {
                        viewModel.applyCoupon(coupon)
                        onBack()
                    }
                )
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}

@Composable
private fun CouponCard(coupon: Coupon, applied: Boolean, eligible: Boolean, onApply: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (applied) BrandCreamAlt else CardWhite)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(ChipGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.LocalOffer, contentDescription = null, tint = BrandOrange)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(coupon.code, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(coupon.title, style = MaterialTheme.typography.bodyMedium)
            Text("Minimum order \u20B9${coupon.minOrder}", style = MaterialTheme.typography.bodySmall, color = SubtleGray)
        }
        if (applied) {
            Icon(Icons.Filled.CheckCircle, contentDescription = "Applied", tint = SuccessGreen)
        } else {
            Text(
                "APPLY",
                color = if (eligible) BrandOrange else SubtleGray,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = eligible) { onApply() }
            )
        }
    }
}
