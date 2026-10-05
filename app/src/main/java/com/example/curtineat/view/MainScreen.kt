package com.example.curtineat.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import androidx.compose.foundation.lazy.items
import com.example.curtineat.data.remote.firebase.model.FirebaseVendorData
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.curtineat.R
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.SecondaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer


@Composable
fun AppScaffold(
    // expect onHistoryClick and onSettingClick
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
//    onVendorToggle: () -> Unit,
    showSearch: Boolean = true,
    showNotifications: Boolean = true,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    MainDrawer(
        // expect to give onHistoryClick and onSettingClick
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
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
            content(innerPadding)
        }
    }
}

@Composable
fun MainScreen(
    //expect onHistory and onSetting
    appViewModel: AppViewModel,
    onCartButtonClick: () -> Unit,
    onHomeClick: () -> Unit,
    onWalletClick:() -> Unit,
    onLoginClick: () -> Unit,
    onFirestoreTestClick: () -> Unit //testing Firebase
) {
    val cart by appViewModel.cart.collectAsState()
    //Testing for notification
    val account by appViewModel.account.collectAsState()
    val isHomeLoading by appViewModel.isHomeLoading.collectAsState()

    LaunchedEffect(account.customerId) {

        account.customerId?.let { customerId ->

            appViewModel.startNotificationListener(
                customerId
            )
        }
    }
    LaunchedEffect(Unit) {
        appViewModel.loadHomeData()
    }

    AppScaffold(
        // expect onHistory and onSetting
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = true,
        showNotifications = true,
        floatingActionButton = {
            CartButton(
                onCartButtonClick = onCartButtonClick,
                totalQuantity = cart.sumOf { it.quantity }
            )
        }
    ) { innerPadding ->

        if (isHomeLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            BodyScreen(
                innerPadding = innerPadding,
                appViewModel = appViewModel,
                onFirestoreTestClick = onFirestoreTestClick
            )
        }

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

    SecondaryCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (isClickable) {
                onClick()
            }
        },
        containerColor =
            if (!isRead)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface,
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 16.dp
        )
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

                Spacer(
                    modifier = Modifier.width(10.dp)
                )
            }

            TextNormal(
                text = message,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight =
                    if (!isRead)
                        FontWeight.Bold
                    else
                        FontWeight.Normal
            )

            TextNormal(
                text = time,
                fontSize = 12.sp
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
    var searching by remember { mutableStateOf(false) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var notificationsOpen by remember { mutableStateOf(false) }

    val notifications by appViewModel.notifications.collectAsState()

    val notificationSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false //allows partial to expanded behavior
    )

    TopAppBar(
        title = {
            if (searching && showSearch) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        TextNormal(
                            text = "Search food...",
                            fontSize = 16.sp
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),

                    // change the meaning of phone's bottom right ENTER key
                    keyboardOptions = KeyboardOptions( imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            appViewModel.searchProduct(productName = searchText)
                        }
                    )
                )
            } else {
                TextNormal(
                    text = "CurtinEAT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },

        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu"
                )
            }
        },

        actions = {

            // SEARCH
            if (showSearch) {
                IconButton(
                    onClick = {
                        searching = !searching

                        if (!searching) {
                            searchText = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector =
                            if (searching)
                                Icons.Default.Close
                            else
                                Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }

            // NOTIFICATION
            if (showNotifications) {
                IconButton(
                    onClick = {
                        notificationsOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications"
                    )
                }
            }


        }
    )
    if (notificationsOpen) {

        val sortedNotifications =
            notifications.sortedByDescending {
                it.timestamp.toDate()
            }

        val groupedNotifications =
            sortedNotifications.groupBy {
                getNotificationDateLabel(
                    it.timestamp.toDate()
                )
            }

        ModalBottomSheet(
            onDismissRequest = {

                notificationsOpen = false

                notifications
                    .filter { notification ->
                        !notification.isRead
                    }
                    .forEach { notification ->

                        appViewModel.updateNotification(
                            notification.copy(
                                isRead = true
                            )
                        )
                    }
            }
            ){

            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
            ) {

                groupedNotifications.forEach { (dateLabel, notificationList) ->

                    // DATE
                    item {

                        TextNormal(
                            text = dateLabel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 16.dp,
                                    bottom = 8.dp
                                ),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // NOTIFICATIONS UNDER THAT DATE
                    items(
                        items = notificationList,
                        key = {
                            it.notificationId
                        }
                    ) { notification ->

                        NotificationItem(
                            message = notification.message,
                            time = formatNotificationTime(
                                notification.timestamp.toDate()
                            ),
                            isRead = notification.isRead
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }
        }
    }
}


//Notification helper
fun getNotificationDateLabel(
    date: java.util.Date
): String {

    val today = java.util.Calendar.getInstance()

    val notificationDate =
        java.util.Calendar.getInstance().apply {
            time = date
        }

    // TODAY
    if (
        today.get(java.util.Calendar.YEAR) ==
        notificationDate.get(java.util.Calendar.YEAR) &&

        today.get(java.util.Calendar.DAY_OF_YEAR) ==
        notificationDate.get(java.util.Calendar.DAY_OF_YEAR)
    ) {
        return "Today"
    }

    // YESTERDAY
    val yesterday =
        java.util.Calendar.getInstance().apply {
            add(
                java.util.Calendar.DAY_OF_YEAR,
                -1
            )
        }

    if (
        yesterday.get(java.util.Calendar.YEAR) ==
        notificationDate.get(java.util.Calendar.YEAR) &&

        yesterday.get(java.util.Calendar.DAY_OF_YEAR) ==
        notificationDate.get(java.util.Calendar.DAY_OF_YEAR)
    ) {
        return "Yesterday"
    }

    // OTHER DATES
    return java.text.SimpleDateFormat(
        "d MMMM yyyy",
        java.util.Locale.getDefault()
    ).format(date)
}

fun formatNotificationTime(
    date: java.util.Date
): String {

    return java.text.SimpleDateFormat(
        "h:mm a",
        java.util.Locale.getDefault()
    ).format(date)
}

@Composable
fun CartButton(
    onCartButtonClick: () -> Unit,
    totalQuantity: Int
) {
    Box {

        FloatingActionButton(
            onClick = onCartButtonClick
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart"
            )
        }

        if (totalQuantity > 0) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        color = Color.Red,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = totalQuantity.toString(),
                    color = Color.White,
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
    appViewModel: AppViewModel,
    onFirestoreTestClick: () -> Unit //Testing Firebase
) {
    val listState = rememberLazyListState()

    val vendors by appViewModel.vendors.collectAsState()
    val products by appViewModel.products.collectAsState()
    val searchVendorId by appViewModel.searchVendorId.collectAsState()

    LaunchedEffect(
        searchVendorId,
        vendors
    ) {
        if (searchVendorId != null) {
            val index = vendors.indexOfFirst {
                it.vendorId == searchVendorId
            }

            println("VENDOR INDEX = $index")

            if (index >= 0) {
                listState.animateScrollToItem(index)
            }
        }
    }


    LazyColumn(
        state = listState,
        modifier = Modifier.padding(innerPadding)
    ) {
        //Testing Firebase
        item {
            PrimaryButton(
                text = "Test Firebase",
                onClick = onFirestoreTestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

//            PrimaryButton(
//                text = "Seed Firestore",
//                onClick = {
//                    appViewModel.seedFirestore()
//                }
//            )

        }

        item {
            // either a swap up action to reload or
            // a button to reload
            // intentionally do this
            // nothing in viewmodel will do auto reload
            // if need reload, run reload() manually
            PrimaryButton(
                text = "Reload",
                onClick = { appViewModel.loadHomeData() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        items(
            items = vendors,
            key = { eachVendor -> eachVendor.vendorId }
        ) { eachVendor ->
            val vendorProducts = products.filter {
                it.vendorId == eachVendor.vendorId
            }
            RestaurantCard(appViewModel = appViewModel, eachVendor, vendorProducts)
        }

    }
}

@Composable
fun RestaurantCard(
    appViewModel: AppViewModel,
    vendor: FirebaseVendorData,
    products: List<FirebaseProductData>
) {
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        TextNormal(
            text = vendor.vendorName,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        mySpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextNormal(
                    text = "%.1f".format(vendor.rating),
                    fontSize = 18.sp
                )

                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xffEA7422),
                    modifier = Modifier.size(20.dp)
                )
            }

            TextNormal(
                text = vendor.category
            )

            TextNormal(
                text = "%.1f km".format(vendor.distance)
            )
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(products) { product ->
                FoodItem(
                    appViewModel = appViewModel,
                    product = product
                )
            }
        }
    }
}


//Helper to get product Image
@Composable
fun getDrawableId(imageName: String): Int {
    return try {
        R.drawable::class.java
            .getField(imageName)
            .getInt(null)
    } catch (e: Exception) {
        R.drawable.food1
    }
}

@Composable
fun FoodItem(
    appViewModel: AppViewModel,
    product: FirebaseProductData
) {
    val imageRes = getDrawableId(product.productImage)

    SecondaryCard(
        onClick = {
            appViewModel.addToCart(product)
        },

        modifier = Modifier.width(150.dp)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            TextNormal(
                text = "RM %.2f".format(product.productPrice),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            TextNormal(
                text = product.productName,
                fontSize = 14.sp
            )
        }
    }
}