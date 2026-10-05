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
import com.example.curtineat.ui.theme.CurtinEATTheme
import com.example.curtineat.view.BalanceScreen
import com.example.curtineat.view.CartScreen
import com.example.curtineat.view.FirestoreTestingScreen
import com.example.curtineat.view.LoginScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.RegistrationScreen
import com.example.curtineat.view.VendorLandingScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.AppViewModelFactory
import kotlinx.serialization.Serializable
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels {
        AppViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Testing for notification
        // TEMPORARY TEST LOGIN
        vm.setCustomerAccount("customer_test_1")

        enableEdgeToEdge()

        setContent {
            CurtinEATTheme {
                ScreenNavigation(vm)
            }
        }
    }
}


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

@Serializable
object RouteVendorLandingScreen

@Serializable
object RouteFirestoreTestingScreen


@Composable
fun ScreenNavigation(
    appViewModel: AppViewModel
) {

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

    val onWalletClick: () -> Unit = {

        if (
            appViewModel.account.value.vendorId != null ||
            appViewModel.account.value.customerId != null
        ) {
            nav.navigate(RouteBalanceScreen)
        } else {
            nav.navigate(RouteLoginScreen)
        }
    }

    val onLoginClick: () -> Unit = {
        nav.navigate(RouteLoginScreen)
    }

    val onRegistrationClick: () -> Unit = {
        nav.navigate(RouteRegistrationScreen)
    }

    val onLoginSuccess: (Boolean) -> Unit = { isVendor ->

        if (isVendor) {
            nav.navigate(RouteVendorLandingScreen)
        } else {
            nav.navigate(RouteMainScreen)
        }
    }

    val onVendorHomeClick: () -> Unit = {
        nav.navigate(RouteVendorLandingScreen) {
            launchSingleTop = true
        }
    }

    val onFirestoreTestClick: () -> Unit = {
        nav.navigate(RouteFirestoreTestingScreen)
    }

    NavHost(
        navController = nav,
        startDestination = RouteVendorLandingScreen
    ) {

        composable<RouteMainScreen> {

            MainScreen(
                appViewModel = appViewModel,
                onCartButtonClick = onCartButtonClick,
                onHomeClick = onHomeClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick,
                onFirestoreTestClick = onFirestoreTestClick
            )
        }

        composable<RouteCartScreen> {

            CartScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick,
                onHomeClick = onHomeClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick
            )
        }

        composable<RouteLoginScreen> {

            LoginScreen(
                appViewModel = appViewModel,
                onLoginSuccess = onLoginSuccess,
                onRegistrationClick = onRegistrationClick,
                onHomeClick = onHomeClick,
                onBackButtonClick = onBackButtonClick
            )
        }

        composable<RouteRegistrationScreen> {

            RegistrationScreen(
                appViewModel = appViewModel,
                onLoginSuccess = onLoginSuccess,
                onHomeClick = onHomeClick,
                onBackButtonClick = onBackButtonClick
            )
        }

        composable<RouteBalanceScreen> {

            BalanceScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick
            )
        }

        composable<RouteVendorLandingScreen> {

            VendorLandingScreen(
                appViewModel = appViewModel,
                onHomeClick = onVendorHomeClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick,
                onOrderStatusClick = {},
                onFoodClick = { productId -> },
                onAddFoodClick = {}
            )
        }

        composable<RouteFirestoreTestingScreen> {

            FirestoreTestingScreen(
                appViewModel = appViewModel
            )
        }
    }
}