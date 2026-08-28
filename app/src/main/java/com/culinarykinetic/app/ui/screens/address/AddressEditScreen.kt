package com.culinarykinetic.app.ui.screens.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.Address
import com.culinarykinetic.app.model.AddressType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.BrandOrange
import com.culinarykinetic.app.ui.theme.DividerGray
import com.culinarykinetic.app.ui.theme.Dimens

@Composable
fun AddressEditScreen(
    viewModel: AppViewModel,
    addressId: String?,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val existing = remember(addressId) { viewModel.addresses.find { it.id == addressId } }

    var label by remember { mutableStateOf(existing?.label ?: "") }
    var line1 by remember { mutableStateOf(existing?.line1 ?: "") }
    var line2 by remember { mutableStateOf(existing?.line2 ?: "") }
    var instructions by remember { mutableStateOf(existing?.instructions ?: "") }
    var type by remember { mutableStateOf(existing?.type ?: AddressType.HOME) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = if (existing != null) "Edit Address" else "Add New Address", onBack = onBack)
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            Text("Address Type", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                AddressType.values().forEach { t ->
                    FilterChipSimple(
                        text = t.name.lowercase().replaceFirstChar { it.uppercase() },
                        selected = type == t,
                        onClick = { type = t; if (label.isBlank()) label = t.name.lowercase().replaceFirstChar { c -> c.uppercase() } }
                    )
                }
            }
            OutlinedTextField(
                value = label, onValueChange = { label = it },
                label = { Text("Label (e.g. Home, Work)") },
                modifier = Modifier.fillMaxWidth(), singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            OutlinedTextField(
                value = line1, onValueChange = { line1 = it },
                label = { Text("Address Line 1") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            OutlinedTextField(
                value = line2, onValueChange = { line2 = it },
                label = { Text("City, State, ZIP") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            OutlinedTextField(
                value = instructions, onValueChange = { instructions = it },
                label = { Text("Delivery Instructions (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            Spacer(Modifier.height(80.dp))
        }
        Surface(shadowElevation = 12.dp) {
            Column(Modifier.padding(Dimens.ScreenPadding).navigationBarsPadding()) {
                PrimaryButton(
                    text = "Save Address",
                    enabled = label.isNotBlank() && line1.isNotBlank(),
                    onClick = {
                        val addr = Address(
                            id = existing?.id ?: "addr_${System.currentTimeMillis()}",
                            label = label,
                            line1 = line1,
                            line2 = line2,
                            instructions = instructions,
                            type = type
                        )
                        if (existing != null) viewModel.updateAddress(addr) else viewModel.addAddress(addr)
                        onSaved()
                    }
                )
            }
        }
    }
}


@Composable
private fun FilterChipSimple(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    com.culinarykinetic.app.ui.components.FilterChip(
        label = text,
        selected = selected,
        onClick = onClick
    )
}
