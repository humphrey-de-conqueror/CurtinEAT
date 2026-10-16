package com.example.curtineat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.curtineat.ui.theme.CurtinEATTheme
import com.example.curtineat.view.AddProductScreen
import com.example.curtineat.view.CartScreen
import com.example.curtineat.view.LoginScreen
import com.example.curtineat.view.MainScreen
import com.example.curtineat.view.RegistrationScreen
import com.example.curtineat.view.VendorLandingScreen
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import kotlinx.serialization.Serializable
import kotlin.getValue
import androidx.navigation.toRoute
import com.example.curtineat.view.OrderHistoryScreen
import com.example.curtineat.view.OrderDetailScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.example.curtineat.ui.theme.AppThemeMode
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.curtineat.ui.components.OfflineBanner
import kotlinx.coroutines.flow.first
import com.example.curtineat.ui.theme.ThemePreferences
import com.example.curtineat.view.ProfileScreen
import com.example.curtineat.view.StartupScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.curtineat.util.OnlineCheck
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    private val vm: AppViewModel by viewModels {
        (application as CurtinEATApplication)
            .appContainer
            .appViewModelFactory
    }

    private val themePreferences by lazy { ThemePreferences(applicationContext) }
    private val onlineCheck by lazy { OnlineCheck(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        val initialMode = runBlocking { themePreferences.themeMode.first() }

        enableEdgeToEdge()

        setContent {
//            var themeModeName by rememberSaveable {
//                mutableStateOf(AppThemeMode.SYSTEM.name)
//            }

            val scope = rememberCoroutineScope()



            val themeMode by themePreferences.themeMode
                .collectAsState(initial = initialMode)

            val systemDark = isSystemInDarkTheme()
            val darkBackground = themeMode == AppThemeMode.DARK || (themeMode == AppThemeMode.SYSTEM && systemDark)


            DisposableEffect(darkBackground) {
                val transparent = android.graphics.Color.TRANSPARENT
                val style = if (darkBackground) {
                    SystemBarStyle.dark(transparent)
                } else {
                    SystemBarStyle.light(transparent, transparent)
                }
                enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
                onDispose {}
            }

            CurtinEATTheme(themeMode = themeMode) {
                val isOnline by onlineCheck.isOnline
                    .collectAsStateWithLifecycle(initialValue = true)

                ScreenNavigation(
                    appViewModel = vm,
                    themeMode = themeMode,
                    isOnline = isOnline,
                    onThemeModeChange = { scope.launch { themePreferences.setThemeMode(it) } }
                )
            }
        }
    }
}
@Serializable
object RouteStartupScreen

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
object RouteProfileScreen

@Serializable
data class RouteAddProductScreen(
    val productId: String? = null
)

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
    isOnline: Boolean,
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

        when (appViewModel.authenticationState.value.role) {

            UserRole.VENDOR -> {
                nav.navigate(RouteVendorLandingScreen) {
                    launchSingleTop = true
                }
            }

            UserRole.CUSTOMER,
            UserRole.NONE -> {
                nav.navigate(RouteMainScreen) {
                    launchSingleTop = true
                }
            }
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

    val onLogoutClick: () -> Unit = {

        appViewModel.signOut()

        nav.navigate(RouteStartupScreen) {
            popUpTo(nav.graph.id) {
                inclusive = true
            }

            launchSingleTop = true
        }
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


    val onProfileClick: () -> Unit = {
        appViewModel.checkCurrentUserRole { role ->

            when (role) {
                UserRole.CUSTOMER,
                UserRole.VENDOR -> {
                    nav.navigate(RouteProfileScreen) {
                        launchSingleTop = true
                    }
                }

                UserRole.NONE -> {
                    onLoginClick()
                }
            }
        }
    }

    val onProfileHomeClick: () -> Unit = {

        when (appViewModel.authenticationState.value.role) {

            UserRole.VENDOR -> {
                nav.navigate(RouteVendorLandingScreen) {
                    popUpTo(RouteProfileScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            UserRole.CUSTOMER,
            UserRole.NONE -> {
                nav.navigate(RouteMainScreen) {
                    popUpTo(RouteProfileScreen) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    val onAddProductClick: () -> Unit = {
        nav.navigate(
            RouteAddProductScreen()
        )
    }
    val onProductClick: (String) -> Unit = { productId ->
        nav.navigate(
            RouteAddProductScreen(
                productId = productId
            )
        )
    }

    val onHistoryClick: () -> Unit = {
        nav.navigate(RouteOrderHistoryScreen) {
            launchSingleTop = true
        }
    }

    val onOrderClick: (String) -> Unit = { orderId ->
        nav.navigate(RouteOrderDetailScreen(orderId))
    }

    Box(Modifier.fillMaxSize()) {


        NavHost(
            navController = nav,
            startDestination = RouteStartupScreen
        ) {
            composable<RouteStartupScreen> {

                StartupScreen(
                    appViewModel = appViewModel,
                    onRoleResolved = { role ->

                        when (role) {

                            UserRole.VENDOR -> {
                                nav.navigate(RouteVendorLandingScreen) {
                                    popUpTo(RouteStartupScreen) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }

                            UserRole.CUSTOMER,
                            UserRole.NONE -> {
                                nav.navigate(RouteMainScreen) {
                                    popUpTo(RouteStartupScreen) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                )
            }

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
                    onThemeModeChange = onThemeModeChange,
                    onLogoutClick = onLogoutClick,
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
                    onThemeModeChange = onThemeModeChange,
                    onLogoutClick = onLogoutClick,
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


            composable<RouteVendorLandingScreen> {
                VendorLandingScreen(
                    appViewModel = appViewModel,
                    onHomeClick = onHomeClick,
                    onProfileClick = onProfileClick,
                    onWalletClick = onWalletClick,
                    onLoginClick = onLoginClick,
                    onHistoryClick = onHistoryClick,
                    onFoodClick = onProductClick,
                    onAddProductClick = onAddProductClick,
                    themeMode = themeMode,
                    onThemeModeChange = onThemeModeChange,
                    onLogoutClick = onLogoutClick,
                )
            }

            composable<RouteProfileScreen> {
                ProfileScreen(
                    viewModel = appViewModel,
                    onLoginClick = onLoginClick,
                    onHomeClick = onProfileHomeClick
                )
            }

            composable<RouteAddProductScreen> { backStackEntry ->

                val route =
                    backStackEntry.toRoute<RouteAddProductScreen>()

                AddProductScreen(
                    viewModel = appViewModel,
                    productId = route.productId,
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
                    onThemeModeChange = onThemeModeChange,
                    onLogoutClick = onLogoutClick,
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

        OfflineBanner(isOnline, Modifier.align(Alignment.BottomCenter))
    }
}