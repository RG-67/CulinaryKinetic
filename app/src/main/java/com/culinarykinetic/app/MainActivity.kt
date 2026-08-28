package com.culinarykinetic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.culinarykinetic.app.ui.navigation.CulinaryKineticNavHost
import com.culinarykinetic.app.ui.theme.CulinaryKineticTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            CulinaryKineticApp()
        }
    }
}

@Composable
fun CulinaryKineticApp() {
    CulinaryKineticTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            CulinaryKineticNavHost()
        }
    }
}
