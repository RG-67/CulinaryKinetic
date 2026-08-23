package com.culinarykinetic.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.theme.*

@Composable
fun OtpScreen(
    phoneNumber: String,
    onBack: () -> Unit,
    onVerified: () -> Unit
) {
    val digits = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    var secondsLeft by remember { mutableStateOf(30) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val isComplete = digits.all { it.isNotEmpty() }

    Column(
        Modifier
            .fillMaxSize()
            .background(BrandCream)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(Modifier.height(24.dp))
        Text(
            "Verify Phone",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = InkBlack
        )
        Spacer(Modifier.height(40.dp))
        Text(
            "We've sent a 6-digit code to",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = CharcoalText
        )
        Text(
            "+1 ($phoneNumber)  Change number",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            digits.forEachIndexed { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { newVal ->
                        val v = newVal.filter { it.isDigit() }.take(1)
                        digits[index] = v
                        if (v.isNotEmpty() && index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .focusRequester(focusRequesters[index]),
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandOrange,
                        unfocusedBorderColor = DividerGray,
                        focusedContainerColor = Color2Fff2,
                        unfocusedContainerColor = Color2Fff2
                    )
                )
            }
        }
        Spacer(Modifier.height(28.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Didn't receive the code? ", style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
            if (secondsLeft > 0) {
                Text("0:${secondsLeft.toString().padStart(2, '0')}", style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
            } else {
                Text(
                    "Resend Code",
                    style = MaterialTheme.typography.bodyMedium,
                    color = BrandOrange,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clip(RoundedCornerShape(4.dp))
                )
            }
        }
        Spacer(Modifier.height(36.dp))
        PrimaryButton(
            text = "Verify",
            trailingIcon = Icons.Filled.CheckCircle,
            enabled = true,
            onClick = onVerified
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (isComplete) "" else "Tip: any 6 digits work in this demo",
            style = MaterialTheme.typography.bodySmall,
            color = SubtleGray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

private val Color2Fff2 = androidx.compose.ui.graphics.Color(0xFFFFF8F3)
