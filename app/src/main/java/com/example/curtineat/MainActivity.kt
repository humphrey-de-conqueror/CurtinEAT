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
import com.example.curtineat.view.CustomerProfileScreen
import com.example.curtineat.view.FirestoreTestingScreen
import com.example.curtineat.view.LoginScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.RegistrationScreen
import com.example.curtineat.view.VendorLandingScreen
import com.example.curtineat.view.VendorProfileScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.AppViewModelFactory
import com.example.curtineat.viewmodel.UserRole
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.Serializable
import kotlin.getValue


class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels {
        AppViewModelFactory()
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
object RouteVendorProfileScreen

@Serializable
object RouteCustomerProfileScreen

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
            1 == 1
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

    val onVendorProfileClick: () -> Unit = {
        nav.navigate(RouteVendorProfileScreen)
    }

    val onCustomerProfileClick: () -> Unit = {
        nav.navigate(RouteCustomerProfileScreen)
    }

    val onProfileClick: () -> Unit = {

        val currentUser =
            FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {

            onLoginClick()

        } else {

            appViewModel.checkCurrentUserRole { role ->

                when (role) {

                    UserRole.VENDOR -> {
                        onVendorProfileClick()
                    }

                    UserRole.CUSTOMER -> {
                        onCustomerProfileClick()
                    }

                    UserRole.NONE -> {
                        onLoginClick()
                    }
                }
            }
        }
    }

    val onFirestoreTestClick: () -> Unit = {
        nav.navigate(RouteFirestoreTestingScreen)
    }

    NavHost(
        navController = nav,
        startDestination = RouteMainScreen
    ) {

        composable<RouteMainScreen> {

            MainScreen(
                appViewModel = appViewModel,
                onCartButtonClick = onCartButtonClick,
                onProfileClick = onProfileClick,
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
                onProfileClick = onProfileClick,
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
                onProfileClick = onProfileClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick,
                onOrderStatusClick = {},
                onFoodClick = { productId -> },
                onAddFoodClick = {}
            )
        }

        composable<RouteVendorProfileScreen> {
            VendorProfileScreen(
                viewModel = appViewModel,
                onLoginClick = onLoginClick,
                onHomeClick = onHomeClick
            )
        }

        composable<RouteCustomerProfileScreen> {
            CustomerProfileScreen(
                viewModel = appViewModel,
                onLoginClick = onLoginClick,
                onHomeClick = onHomeClick
            )
        }

        composable<RouteFirestoreTestingScreen> {

            FirestoreTestingScreen(
                appViewModel = appViewModel
            )
        }
    }
}