package com.culinarykinetic.app.ui.screens.payment

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.SecondaryButton
import com.culinarykinetic.app.ui.theme.*

@Composable
fun PaymentProcessingScreen(
    viewModel: AppViewModel,
    onSuccess: () -> Unit,
    onFailure: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.startPayment { success ->
            if (success) onSuccess() else onFailure()
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "rotation"
    )

    Column(
        Modifier
            .fillMaxSize()
            .background(BrandCream),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(BrandCreamAlt),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = BrandOrange,
                strokeWidth = 4.dp,
                modifier = Modifier.size(60.dp).rotate(rotation)
            )
        }
        Spacer(Modifier.height(28.dp))
        Text("Processing payment\u2026", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Text(
            "Please don't close the app while we confirm your payment of \u20B9${viewModel.cartGrandTotal.let { if (it == 0) viewModel.currentOrder?.grandTotal ?: 0 else it }}.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubtleGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
    }
}

@Composable
fun PaymentFailedScreen(
    onRetry: () -> Unit,
    onChangeMethod: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BrandCream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(90.dp).clip(CircleShape).background(ErrorRed.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = ErrorRed, modifier = Modifier.size(46.dp))
        }
        Spacer(Modifier.height(24.dp))
        Text("Payment Failed", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            "We couldn't process your payment. Please try again or use a different payment method.",
            style = MaterialTheme.typography.bodyMedium,
            color = SubtleGray,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        PrimaryButton(text = "Retry Payment", onClick = onRetry)
        Spacer(Modifier.height(12.dp))
        SecondaryButton(text = "Change Payment Method", onClick = onChangeMethod)
    }
}
