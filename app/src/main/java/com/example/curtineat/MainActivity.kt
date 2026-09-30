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
import com.example.curtineat.view.BalanceScreen
import com.example.curtineat.view.CartScreen
import com.example.curtineat.view.LoginScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.RegistrationScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.AppViewModelFactory
import kotlinx.serialization.Serializable


class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels {
        AppViewModelFactory(
            AppDatabase.buildDatabase(this).vendorDao(),
            AppDatabase.buildDatabase(this).customerDao(),
            AppDatabase.buildDatabase(this).productDao(),
            AppDatabase.buildDatabase(this).notificationDao(),
            AppDatabase.buildDatabase(this).orderDao(),
            AppDatabase.buildDatabase(this).orderItemDao()
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
object RouteCartScreen

@Serializable
object RouteLoginScreen

@Serializable
object RouteRegistrationScreen
@Serializable
object RouteBalanceScreen

@Composable
fun ScreenNavigation(appViewModel: AppViewModel) {
    val nav = rememberNavController()

    val onCartButtonClick: () -> Unit = {
        nav.navigate(RouteCartScreen)
    }

    val onBackButtonClick: () -> Unit = {
        nav.popBackStack()
    }

    val onHomeClick: () -> Unit = {
        nav.navigate(RouteMainScreen) {
            launchSingleTop = true
        }
    }

    val onLoginClick: () -> Unit = {
        nav.navigate(RouteLoginScreen)
    }

    val onRegistrationClick: () -> Unit = {
        nav.navigate(RouteRegistrationScreen)
    }

    NavHost(
        navController = nav,
        startDestination = RouteMainScreen
    ) {
        composable <RouteApiTestingScreen> {
            ApiTestingScreen(appViewModel = appViewModel)
        }

        composable<RouteMainScreen> {
            MainScreen(
                appViewModel = appViewModel,
                onCartButtonClick = onCartButtonClick,
                onHomeClick = onHomeClick,
                onLoginClick = onLoginClick
            )
        }

        composable<RouteCartScreen> {
            CartScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick,
                onHomeClick = onHomeClick,
                onLoginClick = onLoginClick
            )
        }

        composable <RouteLoginScreen> {
            LoginScreen(
                appViewModel = appViewModel,
                onRegistrationClick = onRegistrationClick,
                onBackButtonClick = onBackButtonClick
            )
        }

        composable <RouteRegistrationScreen> {
            RegistrationScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick
            )
        }

        composable <RouteBalanceScreen> {
            BalanceScreen(
                appViewModel = appViewModel
            )
        }
    }
}