package com.culinarykinetic.app.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.NetworkImage
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.components.ScreenTopBar
import com.culinarykinetic.app.ui.theme.BrandOrange
import com.culinarykinetic.app.ui.theme.DividerGray
import com.culinarykinetic.app.ui.theme.Dimens

@Composable
fun EditProfileScreen(viewModel: AppViewModel, onBack: () -> Unit, onSaved: () -> Unit) {
    var name by remember { mutableStateOf(viewModel.currentUser.name) }
    var email by remember { mutableStateOf(viewModel.currentUser.email) }
    var phone by remember { mutableStateOf(viewModel.currentUser.phone) }

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(title = "Edit Profile", onBack = onBack)
        Column(
            Modifier.weight(1f).padding(horizontal = Dimens.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            NetworkImage(viewModel.currentUser.avatarUrl, modifier = Modifier.size(90.dp).clip(CircleShape))
            Spacer(Modifier.height(8.dp))
            Text("Tap to change photo (demo)", color = BrandOrange, style = MaterialTheme.typography.labelLarge)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it }, label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it }, label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
            Spacer(Modifier.height(14.dp))
            OutlinedTextField(
                value = phone, onValueChange = { phone = it }, label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BrandOrange, unfocusedBorderColor = DividerGray)
            )
        }
        Surface(shadowElevation = 12.dp) {
            Column(Modifier.padding(Dimens.ScreenPadding).navigationBarsPadding()) {
                PrimaryButton(text = "Save Changes", onClick = onSaved)
            }
        }
    }
}
