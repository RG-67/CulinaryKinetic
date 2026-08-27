package com.culinarykinetic.app.ui.screens.address

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.model.Address
import com.culinarykinetic.app.model.AddressType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.*
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
fun AddressSelectScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onAddNew: () -> Unit,
    onEdit: (String) -> Unit,
    onConfirm: () -> Unit
) {
    var selected by remember { mutableStateOf(viewModel.selectedAddress) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Select Address", onBack = onBack)
        LazyColumn(
            Modifier
                .weight(1f)
                .padding(horizontal = Dimens.ScreenPadding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(Modifier.height(6.dp)) }
            items(viewModel.addresses, key = { it.id }) { address ->
                AddressCard(
                    address = address,
                    selected = selected?.id == address.id,
                    onClick = { selected = address },
                    onEdit = { onEdit(address.id) },
                    onDelete = {
                        viewModel.deleteAddress(address.id); if (selected?.id == address.id) selected =
                        viewModel.addresses.firstOrNull()
                    }
                )
            }
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color.White)
                        .border(1.dp, BrandOrange, RoundedCornerShape(100.dp))
                        .clickable { onAddNew() }
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = BrandOrange)
                    Spacer(Modifier.width(6.dp))
                    Text("Add New Address", color = BrandOrange, fontWeight = FontWeight.Bold)
                }
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
        Surface(shadowElevation = 12.dp, color = BrandCream, modifier = Modifier.navigationBarsPadding()) {
            Column(Modifier.padding(Dimens.ScreenPadding)) {
                PrimaryButton(
                    text = "Confirm Address",
                    enabled = selected != null,
                    onClick = {
                        selected?.let { viewModel.selectAddress(it) }
                        onConfirm()
                    }
                )
            }
        }
    }
}

@Composable
private fun AddressCard(
    address: Address,
    selected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (selected) BrandCreamAlt else Color.White)
            .border(
                if (selected) 2.dp else 1.dp,
                if (selected) BrandOrange else DividerGray,
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ChipGray),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (address.type) {
                        AddressType.HOME -> Icons.Filled.Home
                        AddressType.WORK -> Icons.Filled.Work
                        AddressType.OTHER -> Icons.Filled.LocationOn
                    },
                    contentDescription = null,
                    tint = BrandOrange
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(address.label, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text(address.line1, style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
                Text(address.line2, style = MaterialTheme.typography.bodyMedium, color = SubtleGray)
                if (address.instructions.isNotBlank()) {
                    Text(
                        "Note: ${address.instructions}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubtleGray
                    )
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                "Edit",
                color = BrandOrange,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.clickable { onEdit() })
            Text(
                "Delete",
                color = ErrorRed,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.clickable { onDelete() })
        }
    }
}

/*private fun Modifier.border(width: androidx.compose.ui.unit.Dp, color: Color, shape: androidx.compose.ui.graphics.Shape): Modifier =
    this.then(androidx.compose.foundation.border(width = width, color = color, shape = shape))*/

private fun Modifier.myBorder(
    width: Dp,
    color: Color,
    shape: Shape
): Modifier =
    this.border(
        width = width,
        color = color,
        shape = shape
    )
