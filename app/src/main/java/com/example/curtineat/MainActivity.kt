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
import com.example.curtineat.view.AddProductScreen
import com.example.curtineat.view.BalanceScreen
import com.example.curtineat.view.CartScreen
import com.example.curtineat.view.CustomerProfileScreen
import com.example.curtineat.view.LoginScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.RegistrationScreen
import com.example.curtineat.view.VendorLandingScreen
import com.example.curtineat.view.VendorProfileScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import kotlinx.serialization.Serializable
import kotlin.getValue
import androidx.navigation.toRoute
import com.example.curtineat.view.OrderHistoryScreen
import com.example.curtineat.view.OrderDetailScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.curtineat.ui.theme.AppThemeMode

class MainActivity : ComponentActivity() {

    private val vm: AppViewModel by viewModels {
        (application as CurtinEATApplication)
            .appContainer
            .appViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            var themeModeName by rememberSaveable {
                mutableStateOf(AppThemeMode.SYSTEM.name)
            }

            val themeMode = AppThemeMode.valueOf(themeModeName)

            CurtinEATTheme(themeMode = themeMode) {
                ScreenNavigation(
                    appViewModel = vm,
                    themeMode = themeMode,
                    onThemeModeChange = { selectedMode ->
                        themeModeName = selectedMode.name
                    }
                )
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
object RouteAddProductScreen

@Serializable
object RouteOrderHistoryScreen

@Serializable
data class RouteOrderDetailScreen(
    val orderId: String
)

@Composable
fun ScreenNavigation(
    appViewModel: AppViewModel,
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit
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
        appViewModel.checkCurrentUserRole { role ->
            when (role) {
                UserRole.CUSTOMER,
                UserRole.VENDOR -> {
                    nav.navigate(RouteBalanceScreen) {
                        launchSingleTop = true
                    }
                }

                UserRole.NONE -> {
                    nav.navigate(RouteLoginScreen) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    val onLoginClick: () -> Unit = {
        nav.navigate(RouteLoginScreen) {
            launchSingleTop = true
        }
    }

    val onRegistrationClick: () -> Unit = {
        nav.navigate(RouteRegistrationScreen)
    }

    val onLoginSuccess: (Boolean) -> Unit = { isVendor ->
        if (isVendor) {
            nav.navigate(RouteVendorLandingScreen) {
                popUpTo(RouteLoginScreen) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        } else {
            nav.navigate(RouteMainScreen) {
                popUpTo(RouteLoginScreen) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    val onVendorHomeClick: () -> Unit = {
        nav.navigate(RouteVendorLandingScreen) {
            launchSingleTop = true
        }
    }

    val onVendorProfileClick: () -> Unit = {
        nav.navigate(RouteVendorProfileScreen) {
            launchSingleTop = true
        }
    }

    val onCustomerProfileClick: () -> Unit = {
        nav.navigate(RouteCustomerProfileScreen) {
            launchSingleTop = true
        }
    }

    val onProfileClick: () -> Unit = {
        appViewModel.checkCurrentUserRole { role ->
            when (role) {
                UserRole.VENDOR -> onVendorProfileClick()
                UserRole.CUSTOMER -> onCustomerProfileClick()
                UserRole.NONE -> onLoginClick()
            }
        }
    }

    val onAddProductClick: () -> Unit = {
        nav.navigate(RouteAddProductScreen)
    }

    val onHistoryClick: () -> Unit = {
        nav.navigate(RouteOrderHistoryScreen) {
            launchSingleTop = true
        }
    }

    val onOrderClick: (String) -> Unit = { orderId ->
        nav.navigate(RouteOrderDetailScreen(orderId))
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
            onHistoryClick = onHistoryClick,
            themeMode = themeMode,
            onThemeModeChange = onThemeModeChange
        )
    }

        composable<RouteCartScreen> {
            CartScreen(
                appViewModel = appViewModel,
                onBackButtonClick = onBackButtonClick,
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick,
                onHistoryClick = onHistoryClick,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
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
                onHistoryClick = onHistoryClick,
                onFoodClick = { _ -> },
                onAddProductClick = onAddProductClick,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
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

        composable<RouteAddProductScreen> {
            AddProductScreen(
                viewModel = appViewModel,
                onBackClick = {
                    nav.popBackStack()
                }
            )
        }

        composable<RouteOrderHistoryScreen> {
            OrderHistoryScreen(
                appViewModel = appViewModel,
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick,
                onWalletClick = onWalletClick,
                onLoginClick = onLoginClick,
                onHistoryClick = onHistoryClick,
                onOrderClick = onOrderClick,
                themeMode = themeMode,
                onThemeModeChange = onThemeModeChange
            )
        }

        composable<RouteOrderDetailScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<RouteOrderDetailScreen>()

            OrderDetailScreen(
                appViewModel = appViewModel,
                orderId = route.orderId,
                onBackButtonClick = onBackButtonClick
            )
        }
    }
}