package com.example.curtineat.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.database.Product
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import coil.compose.AsyncImage
import com.example.curtineat.viewmodel.AppViewModel
import java.io.File

@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    onVendorToggle: (Boolean) -> Unit
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onOrderStatusClick: () -> Unit,
    onFoodClick: (Int) -> Unit,
    onAddFoodClick: () -> Unit
) {
    // system back returns to customer mode so the toggle never gets out of sync
    BackHandler { onVendorToggle(false) }
    val loggedInVendorId: Int? = 1
    // val loggedInVendorId = appViewModel.account.vendorId

    val currentVendor = appViewModel.vendor.find {
        it.vendorId == loggedInVendorId
    }

    val vendorProducts = appViewModel.product.filter {
        it.vendorID == loggedInVendorId
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onLogInClick = onLogInClick,
        onVendorToggle = onVendorToggle,
        title = "Vendor",
        showSearch = false,
        showNotifications = false
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = true,
        showNotifications = true,

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFoodClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add food"
                )
            }
        }
    ) { innerPadding ->
        Column(

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (appViewModel.myVendorId == null) {
                CreateStoreForm(onCreate = { appViewModel.createStore(it) })
            } else {
                StoreDashboard(appViewModel)
            }
        }
    }
}

@Composable
private fun CreateStoreForm(onCreate: (String) -> Unit) {
    var storeName by remember { mutableStateOf("") }

            item {
                TextNormal(
                    text = currentVendor?.vendorName ?: "Vendor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
    Text("Create your store", style = MaterialTheme.typography.titleLarge)
    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = storeName,
        onValueChange = { storeName = it },
        label = { Text("Store name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(12.dp))

    Button(
        onClick = { onCreate(storeName.trim()) },
        enabled = storeName.isNotBlank(),
        modifier = Modifier.fillMaxWidth()
    ) { Text("Create Store") }
}

@Composable
private fun StoreDashboard(appViewModel: AppViewModel) {
    val myProducts = appViewModel.product.filter {
        it.vendorID == appViewModel.myVendorId
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text(appViewModel.myStoreName, style = MaterialTheme.typography.headlineSmall)
        }
        item {
            AddProductForm(onAdd = { name, price, imagePath ->
                appViewModel.addProduct(name, price, imagePath)
            })
        }
        items(myProducts, key = { it.productId }) { p ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                AsyncImage(
                    model = File(p.productImage),
                    contentDescription = p.productName,
                    modifier = Modifier.size(64.dp)
                )
                Column(Modifier.padding(start = 12.dp)) {
                    Text(p.productName)
                    Text("RM %.2f".format(p.productPrice))
                }
            }
        }
    }
}

@Composable
private fun AddProductForm(onAdd: (String, Double, String) -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> imageUri = uri }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Add a product", style = MaterialTheme.typography.titleMedium)

                mySpacer()
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

                PrimaryButton(
                    text = "Order Status",
                    onClick = onOrderStatusClick,
                    modifier = Modifier.fillMaxWidth()
                )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

                mySpacer()

                TextNormal(
                    text = "My Product(s)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }

            items(
                items = vendorProducts,
                key = { it.productId }
            ) { product ->

                VendorFoodItem(
                    product = product,
                    onClick = {
                        onFoodClick(product.productId)
                    }
                )
            }
        }
        OutlinedButton(
            onClick = {
                picker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) { Text(if (imageUri == null) "Choose Image" else "Change Image") }

        imageUri?.let {
            AsyncImage(
                model = it,
                contentDescription = "Selected image",
                modifier = Modifier.size(120.dp)
            )
        }

        Button(
            onClick = {
                val path = copyImageToInternalStorage(context, imageUri!!)
                onAdd(name.trim(), price.toDouble(), path)
                name = ""; price = ""; imageUri = null
            },
            enabled = name.isNotBlank() && price.toDoubleOrNull() != null && imageUri != null,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Add Product") }
    }
}

@Composable
fun VendorFoodItem(
    product: Product,
    onClick: () -> Unit
) {
    val imageRes = getDrawableId(product.productImage)

    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentPadding = PaddingValues(12.dp)
    ) {

        Image(
            painter = painterResource(imageRes),
            contentDescription = product.productName,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentScale = ContentScale.Crop
        )

        mySpacer()

        TextNormal(
            text = product.productName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        TextNormal(
            text = "RM %.2f".format(product.productPrice),
            fontSize = 16.sp
        )
// Picker URIs stop working after a restart, so copy the image into app storage
// and save that file path instead.
private fun copyImageToInternalStorage(context: Context, uri: Uri): String {
    val file = File(context.filesDir, "product_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    return file.absolutePath
}