package com.culinarykinetic.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.theme.*

@Composable
fun AuthPhoneScreen(
    initialPhone: String,
    onContinue: (String) -> Unit,
    onSkipDemo: () -> Unit
) {
    var phone by remember { mutableStateOf(initialPhone) }

    Box(
        Modifier
            .fillMaxSize()
            .background(BrandCreamAlt),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Welcome to",
                style = MaterialTheme.typography.headlineMedium,
                color = BrandOrangeDark,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                "Culinary Kinetic",
                style = MaterialTheme.typography.headlineMedium,
                color = BrandOrangeDark,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Enter your phone number to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = SubtleGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10) phone = it.filter { c -> c.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("(555) 000-0000") },
                leadingIcon = {
                    Text("  US +1", color = SubtleGray, style = MaterialTheme.typography.bodyMedium)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandOrange,
                    unfocusedBorderColor = DividerGray
                )
            )
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = "Continue",
                onClick = { onContinue(if (phone.isBlank()) "5551234567" else phone) }
            )
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(Modifier.weight(1f), color = DividerGray)
                Text("  OR SIGN IN WITH  ", style = MaterialTheme.typography.labelSmall, color = SubtleGray)
                Divider(Modifier.weight(1f), color = DividerGray)
            }
            Spacer(Modifier.height(20.dp))
            OutlinedButton(
                onClick = { onContinue(if (phone.isBlank()) "5551234567" else phone) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = ChipGray, contentColor = CharcoalText)
            ) {
                Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Continue with Email")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { onContinue(if (phone.isBlank()) "5551234567" else phone) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = ChipGray, contentColor = CharcoalText)
            ) {
                Text("G", fontWeight = FontWeight.Bold, color = BrandOrange)
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google")
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "By continuing, you agree to our Terms of Service and Privacy Policy.",
                style = MaterialTheme.typography.bodySmall,
                color = SubtleGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            TextButton(onClick = onSkipDemo) {
                Text("Skip sign-in (demo mode)", color = SubtleGray)
            }
        }
    }
}
