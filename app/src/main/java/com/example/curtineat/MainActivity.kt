package com.example.curtineat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.curtineat.database.AppDatabase
import com.example.curtineat.ui.theme.CurtinEATTheme
import com.example.curtineat.view.ApiTestingScreen
import com.example.curtineat.view.CardScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.AppViewModelFactory
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels {
        AppViewModelFactory(
            AppDatabase.buildDatabase(this).vendorDao(),
            AppDatabase.buildDatabase(this).customerDao(),
            AppDatabase.buildDatabase(this).productDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurtinEATTheme {
                ScreenNavigation(vm)
            }
        }
    }
}

@Serializable
object RouteApiTestingScreen
@Serializable
object RouteMainScreen

@Serializable
object RouteCardScreen

@Composable
fun ScreenNavigation(appViewModel: AppViewModel) {
    val nav = rememberNavController()

    val onCardButtonClick: () -> Unit = {
        nav.navigate(RouteCardScreen)
    }

    val onBackButtonClick: () -> Unit = {
        nav.popBackStack()
    }

    val onHomeClick: () -> Unit = {
        nav.navigate(RouteMainScreen) {
            launchSingleTop = true
        }
    }

    NavHost(
        navController = nav,

        //please change this
        startDestination = RouteApiTestingScreen
    ) {
        composable <RouteApiTestingScreen> {
            ApiTestingScreen(appViewModel = appViewModel)
        }

        composable<RouteMainScreen> {
            MainScreen(
                appViewModel = appViewModel,
                onCardButtonClick = onCardButtonClick,
                onHomeClick = onHomeClick
            )
        }

        composable<RouteCardScreen> {
            CardScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick
            )
        }

    }
}