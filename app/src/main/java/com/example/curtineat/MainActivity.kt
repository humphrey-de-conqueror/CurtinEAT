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
import com.example.curtineat.view.CartScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.LogIn
import com.example.curtineat.view.Registeration
import com.example.curtineat.view.VendorLandingScreen
import com.example.curtineat.view.OrderTrackingScreen
import com.example.curtineat.view.OrderProgressBar
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
object RouteLogInScreen

@Serializable
object RouteRegistrationScreen

@Serializable
object RouteVendorScreen

@Serializable
object RouteOrderListScreen

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
        appViewModel.updateVendorMode(false)
        nav.navigate(RouteMainScreen) {
            popUpTo(RouteMainScreen) { inclusive = false }
            launchSingleTop = true
        }
    }

    val onVendorToggle: (Boolean) -> Unit = { toVendor ->
        appViewModel.updateVendorMode(toVendor)
        nav.navigate(if (toVendor) RouteVendorScreen else RouteMainScreen) {
            popUpTo(RouteMainScreen) { inclusive = false }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = nav,
        startDestination = RouteOrderListScreen
    ) {
        composable <RouteApiTestingScreen> {
            ApiTestingScreen(appViewModel = appViewModel)
        }

        composable<RouteMainScreen> {
            MainScreen(
                appViewModel = appViewModel,
                onCartButtonClick = onCartButtonClick,
                onHomeClick = onHomeClick,
                onVendorToggle = onVendorToggle
            )
        }

        composable<RouteCartScreen> {
            CartScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick,
                onHomeClick = onHomeClick
            )
        }

        composable<RouteLogInScreen> {
            LogIn(
                onRegisterHit = {
                nav.navigate(RouteRegistrationScreen)},
                viewModel = appViewModel
            )
        }

        composable<RouteRegistrationScreen> {
            Registeration(viewModel = appViewModel)
        }

        composable<RouteVendorScreen> {
            VendorLandingScreen(
                appViewModel = appViewModel,
                onHomeClick = onHomeClick,
                onVendorToggle = onVendorToggle

            )
        }

        composable <RouteOrderListScreen> {
            OrderTrackingScreen(
                appViewModel = appViewModel,
                onHomeClick = onHomeClick,
                onVendorToggle = onVendorToggle
            )
        }
    }
}