package com.example.curtineat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.curtineat.ui.theme.CurtinEATTheme
import com.example.curtineat.view.CardScreen
import com.example.curtineat.view.MainScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurtinEATTheme {
                ScreenNavigation()
            }
        }
    }
}

@Serializable
object RouteMainScreen

@Serializable
object RouteCardScreen
@Composable
fun ScreenNavigation() {
    val nav = rememberNavController()
    val onCardButtonClick: () -> Unit = { nav.navigate(RouteCardScreen) }
    val onBackButtonClick: () -> Unit = { nav.popBackStack() }

    NavHost (
        navController = nav,
        startDestination = RouteMainScreen
    ) {
        composable <RouteMainScreen> { MainScreen(onCardButtonClick) }
        composable <RouteCardScreen> { CardScreen(onBackButtonClick) }
    }
}