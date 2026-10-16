package com.example.curtineat.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.local.room.entity.ProductEntity
import com.example.curtineat.data.local.room.entity.VendorEntity
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.components.EmptyState
import com.example.curtineat.ui.components.LoadingState
import com.example.curtineat.ui.components.SectionHeader
import com.example.curtineat.ui.theme.AppThemeMode
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppScaffold(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit,
    showSearch: Boolean = true,
    showNotifications: Boolean = true,
    loadingOverride: Boolean? = null,
    floatingActionButton: @Composable () -> Unit = {},
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit,
    content: @Composable (PaddingValues) -> Unit,

    ) {
    val showLoading = loadingOverride ?: false
    val authenticationState by appViewModel.authenticationState.collectAsState()

    val currentUserId = authenticationState.userId
    val currentRole = authenticationState.role

    LaunchedEffect(currentUserId, currentRole) {
        if (currentUserId != null) {
            appViewModel.observeNotificationsForCurrentUser()
        }
    }

    MainDrawer(
        viewModel = appViewModel,
        onHomeClick = onHomeClick,
        onProfileClick = onProfileClick,
//        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        onHistoryClick = onHistoryClick,
        isLoggedIn = currentUserId != null,
        onLogOutClick = onLogoutClick,

        isVendor = authenticationState.role == UserRole.VENDOR,

        themeMode = themeMode,
        onThemeModeChange = onThemeModeChange
    ) { onMenuClick ->

        Scaffold(
            topBar = {
                TopBarScreen(
                    appViewModel = appViewModel,
                    onMenuClick = onMenuClick,
                    showSearch = showSearch,
                    showNotifications = showNotifications
                )
            },
            floatingActionButton = floatingActionButton
        ) { innerPadding ->

            Box(modifier = Modifier.fillMaxSize()) {
                content(innerPadding)

                AnimatedVisibility(
                    visible = showLoading,
                    enter = fadeIn(animationSpec = tween(180)),
                    exit = fadeOut(animationSpec = tween(180)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(
                                MaterialTheme.colorScheme.background
                                    .copy(alpha = 0.85f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    appViewModel: AppViewModel,
    onCartButtonClick: () -> Unit,
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onLogoutClick: () -> Unit,
    themeMode: AppThemeMode,
    onThemeModeChange: (AppThemeMode) -> Unit
) {
    val cart by appViewModel.cart.collectAsState()
    val homeLoading by appViewModel.homeLoading.collectAsState()

    LaunchedEffect(Unit) {
        appViewModel.ensureHomeDataLoaded()
    }

    if (homeLoading) {

        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onProfileClick = onProfileClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        onHistoryClick = onHistoryClick,
        showSearch = false,
        showNotifications = true,
        floatingActionButton = {
            CartButton(
                onCartButtonClick = onCartButtonClick,
                totalQuantity = cart.sumOf { it.quantity }
            )
        },
        themeMode = themeMode,
        onLogoutClick = onLogoutClick,
        onThemeModeChange = onThemeModeChange
    ) { innerPadding ->
        BodyScreen(
            innerPadding = innerPadding,
            appViewModel = appViewModel
        )
    }
}

@Composable
fun NotificationItem(
    message: String,
    time: String,
    isRead: Boolean,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = if (isClickable) onClick else null,
        containerColor = if (!isRead) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 16.dp
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            AppText(
                text = message,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (!isRead) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            AppText(
                text = time,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (isClickable) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open order",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarScreen(
    appViewModel: AppViewModel,
    onMenuClick: () -> Unit,
    showSearch: Boolean = true,
    showNotifications: Boolean = true
) {
    var searching by rememberSaveable { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var notificationsOpen by remember { mutableStateOf(false) }

    val notificationState by appViewModel.notificationState.collectAsState()
    val notifications = notificationState.data
    val unreadCount = notifications.count { !it.isRead }

    var notificationDrawerLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(notificationDrawerLoading) {
        if (notificationDrawerLoading) {
            delay(500)
            notificationDrawerLoading = false
        }
    }

    TopAppBar(
        title = {
            if (searching && showSearch) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text("Search food...")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            appViewModel.searchProduct(
                                productName = searchText
                            )
                        }
                    )
                )
            } else {
                AppText(
                    text = "CurtinEAT",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            if (showSearch) {
                IconButton(
                    onClick = {
                        searching = !searching

                        if (!searching) {
                            searchText = ""
                            appViewModel.searchProduct("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (searching) {
                            Icons.Default.Close
                        } else {
                            Icons.Default.Search
                        },
                        contentDescription = "Search"
                    )
                }
            }

            if (showNotifications) {
                NotificationAction(
                    unreadCount = unreadCount,
                    onClick = {
                        notificationDrawerLoading = true
                        notificationsOpen = true
                    }
                )
            }
        }
    )

    if (notificationsOpen) {
        val sortedNotifications = notifications.sortedByDescending {
            it.timestamp
        }

        val groupedNotifications = sortedNotifications.groupBy {
            getNotificationDateLabel(java.util.Date(it.timestamp))
        }

        ModalBottomSheet(
            onDismissRequest = {
                notificationsOpen = false
                appViewModel.markAllNotificationsAsRead()
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
            ) {
                if (notificationDrawerLoading) {
                    item {
                        LoadingState(
                            message = "Loading notifications..."
                        )
                    }
                } else if (notifications.isEmpty()) {
                    item {
                        EmptyState(
                            title = "No notifications",
                            description = "You're all caught up."
                        )
                    }
                } else {
                    groupedNotifications.forEach { (dateLabel, notificationList) ->
                        item(key = "date-$dateLabel") {
                            AppText(
                                text = dateLabel,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 16.dp,
                                        bottom = 8.dp
                                    ),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }

                        items(
                            items = notificationList,
                            key = { it.notificationId }
                        ) { notification ->
                            NotificationItem(
                                message = notification.message,
                                time = formatNotificationTime(
                                    java.util.Date(notification.timestamp)
                                ),
                                isRead = notification.isRead
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationAction(
    unreadCount: Int,
    onClick: () -> Unit
) {
    val badgeScale = remember { Animatable(1f) }

    LaunchedEffect(unreadCount) {
        if (unreadCount > 0) {
            badgeScale.snapTo(0.75f)
            badgeScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Box {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications"
            )
        }

        AnimatedVisibility(
            visible = unreadCount > 0,
            enter = fadeIn(tween(150)) +
                    slideInVertically(
                        animationSpec = tween(180),
                        initialOffsetY = { -it / 3 }
                    ),
            exit = fadeOut(tween(120)) +
                    slideOutVertically(
                        animationSpec = tween(120),
                        targetOffsetY = { -it / 3 }
                    ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 6.dp, end = 6.dp)

        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = badgeScale.value
                        scaleY = badgeScale.value
                    }
                    .size(20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (unreadCount > 99) {
                        "99+"
                    } else {
                        unreadCount.toString()
                    },
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun LoadingDots() {
    var dots by remember { mutableStateOf(".") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(350)
            dots = when (dots) {
                "." -> ".."
                ".." -> "..."
                else -> "."
            }
        }
    }

    AppText(
        text = dots,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold
    )
}

fun getNotificationDateLabel(date: java.util.Date): String {
    val timeZone = java.util.TimeZone.getTimeZone("Asia/Kuala_Lumpur")

    val today = java.util.Calendar.getInstance(timeZone)
    val notificationDate = java.util.Calendar.getInstance(timeZone).apply {
        time = date
    }

    if (
        today.get(java.util.Calendar.YEAR) ==
        notificationDate.get(java.util.Calendar.YEAR) &&
        today.get(java.util.Calendar.DAY_OF_YEAR) ==
        notificationDate.get(java.util.Calendar.DAY_OF_YEAR)
    ) {
        return "Today"
    }

    val yesterday = java.util.Calendar.getInstance(timeZone).apply {
        add(java.util.Calendar.DAY_OF_YEAR, -1)
    }

    if (
        yesterday.get(java.util.Calendar.YEAR) ==
        notificationDate.get(java.util.Calendar.YEAR) &&
        yesterday.get(java.util.Calendar.DAY_OF_YEAR) ==
        notificationDate.get(java.util.Calendar.DAY_OF_YEAR)
    ) {
        return "Yesterday"
    }

    val formatter = java.text.SimpleDateFormat(
        "d MMMM yyyy",
        java.util.Locale.getDefault()
    )

    formatter.timeZone = timeZone
    return formatter.format(date)
}

fun formatNotificationTime(date: java.util.Date): String {
    val formatter = java.text.SimpleDateFormat(
        "h:mm a",
        java.util.Locale.getDefault()
    )

    formatter.timeZone =
        java.util.TimeZone.getTimeZone("Asia/Kuala_Lumpur")

    return formatter.format(date)
}

@Composable
fun CartButton(
    onCartButtonClick: () -> Unit,
    totalQuantity: Int
) {
    val badgeScale = remember { Animatable(1f) }

    LaunchedEffect(totalQuantity) {
        if (totalQuantity > 0) {
            badgeScale.snapTo(0.65f)
            badgeScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Box {
        FloatingActionButton(
            onClick = onCartButtonClick
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }

        AnimatedVisibility(
            visible = totalQuantity > 0,
            enter = fadeIn(tween(120)),
            exit = fadeOut(tween(100)),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = badgeScale.value
                        scaleY = badgeScale.value
                    }
                    .size(22.dp)
                    .background(
                        color = MaterialTheme.colorScheme.error,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = totalQuantity.toString(),
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun BodyScreen(
    innerPadding: PaddingValues,
    appViewModel: AppViewModel
) {
    val listState = rememberLazyListState()

    val vendorState by appViewModel.vendorState.collectAsState()
    val productState by appViewModel.productState.collectAsState()
    val searchQuery by appViewModel.searchQuery.collectAsState()
    val searchVendorId by appViewModel.searchVendorId.collectAsState()

    var searchText by rememberSaveable {
        mutableStateOf(searchQuery)
    }

    val vendors = vendorState.data
    val products = productState.data

    val visibleVendors =
        vendors.filter { vendor ->
            products.any { product ->
                product.vendorId == vendor.vendorId && product.isAvailable &&
                        (searchQuery.isBlank() || product.productName
                            .contains(searchQuery, ignoreCase = true))
            }
        }

    LaunchedEffect(searchQuery) {
        searchText = searchQuery
    }

    LaunchedEffect(searchVendorId, vendors) {
        if (searchVendorId != null) {
            val index = vendors.indexOfFirst {
                it.vendorId == searchVendorId
            }

            if (index >= 0) {
                // Search field and section heading occupy the first two positions.
                listState.animateScrollToItem(index + 2)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 12.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "search") {
//            OutlinedTextField(
//                value = searchText,
//                onValueChange = { searchText = it },
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                shape = RoundedCornerShape(16.dp),
//                placeholder = {
//                    Text("Search food...")
//                },
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = null
//                    )
//                },
//                trailingIcon = {
//                    AnimatedVisibility(
//                        visible = searchText.isNotBlank(),
//                        enter = fadeIn(tween(120)),
//                        exit = fadeOut(tween(120))
//                    ) {
//                        IconButton(
//                            onClick = {
//                                searchText = ""
//                                appViewModel.searchProduct("")
//                            }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Close,
//                                contentDescription = "Clear search"
//                            )
//                        }
//                    }
//                },
//                keyboardOptions = KeyboardOptions(
//                    imeAction = ImeAction.Search
//                ),
//                keyboardActions = KeyboardActions(
//                    onSearch = {
//                        appViewModel.searchProduct(searchText.trim())
//                    }
//                )
//            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { appViewModel.updateSearchText(it) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                placeholder = {
                    Text("Search food...")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotBlank(),
                        enter = fadeIn(tween(120)),
                        exit = fadeOut(tween(120))
                    ) {
                        IconButton(
                            onClick = {
                                appViewModel.searchProduct("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        appViewModel.searchProduct(searchQuery)
                    }
                )
            )

        }

        item(key = "restaurant-heading") {
            SectionHeader(
                title = "Restaurants",
                subtitle = "Explore available food and vendors",
                actionText = "Refresh",
                onActionClick = {
                    appViewModel.refreshHomeData()
                }
            )
        }

        if (visibleVendors.isEmpty()) {

            item(key = "empty-restaurants") {
                EmptyState(
                    title =
                        if (searchQuery.isBlank()) {
                            "No food available"
                        } else {
                            "No matching food"
                        },
                    description =
                        if (searchQuery.isBlank()) {
                            "There are currently no products available for ordering."
                        } else {
                            "Try searching for another food item."
                        },
                    icon = Icons.Default.Search
                )
            }

        } else {

            items(
                items = visibleVendors,
                key = { it.vendorId }
            ) { vendor ->

                val vendorProducts =
                    products.filter { product ->

                        product.vendorId == vendor.vendorId &&
                                product.isAvailable &&
                                (
                                        searchQuery.isBlank() ||
                                                product.productName.contains(
                                                    searchQuery,
                                                    ignoreCase = true
                                                )
                                        )
                    }

                AnimatedVisibility(
                    visible = true,
                    enter =
                        fadeIn(tween(220)) +
                                slideInVertically(
                                    animationSpec = tween(220),
                                    initialOffsetY = {
                                        it / 12
                                    }
                                )
                ) {
                    RestaurantCard(
                        appViewModel = appViewModel,
                        vendor = vendor,
                        products = vendorProducts
                    )
                }
            }
        }

        item(key = "bottom-space") {
            Spacer(
                modifier = Modifier.height(72.dp)
            )
        }
    }
}

@Composable
fun RestaurantCard(
    appViewModel: AppViewModel,
    vendor: VendorEntity,
    products: List<ProductEntity>
) {
    AppCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AppText(
                text = vendor.vendorName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(17.dp)
                    )

                    AppText(
                        text = "%.1f".format(vendor.rating),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                AppText(
                    text = vendor.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Text(
                    text = vendor.location.ifBlank { "No location" },
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            if (products.isEmpty()) {
                AppText(
                    text = "No products available",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 2.dp)
                ) {
                    items(
                        items = products,
                        key = { it.productId }
                    ) { product ->
                        FoodItem(
                            appViewModel = appViewModel,
                            product = product
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodItem(
    appViewModel: AppViewModel,
    product: ProductEntity,
    enabled: Boolean = true
) {
    val scope = rememberCoroutineScope()
    val addButtonScale = remember { Animatable(1f) }

    Card(
        modifier = Modifier.width(156.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        onClick = {
            if (enabled) {
                appViewModel.addToCart(product)
            }
        },
        enabled = enabled
    ) {
        Column {
            CachedImage(
                imageId = product.productImage,
                viewModel = appViewModel,
                contentDescription = product.productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
            )

            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AppText(
                    text = product.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppText(
                        text = "RM %.2f".format(product.productPrice),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    FilledTonalIconButton(
                        onClick = {
                            appViewModel.addToCart(product)

                            scope.launch {
                                addButtonScale.animateTo(
                                    targetValue = 0.82f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessHigh
                                    )
                                )

                                addButtonScale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        },
                        enabled = enabled,
                        modifier = Modifier
                            .size(40.dp)
                            .graphicsLayer {
                                scaleX = addButtonScale.value
                                scaleY = addButtonScale.value
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add ${product.productName} to cart"
                        )
                    }
                }
            }
        }
    }
}