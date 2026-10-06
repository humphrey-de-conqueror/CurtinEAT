package com.example.curtineat.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.data.remote.firebase.model.FirebaseProductData
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.ui.theme.mySpacer
import com.example.curtineat.viewmodel.AppViewModel
import java.io.File
import java.util.UUID

@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLoginClick: () -> Unit,
    onOrderStatusClick: () -> Unit,
    onFoodClick: (String) -> Unit
) {
    val account by appViewModel.account.collectAsState()
    val vendors by appViewModel.vendors.collectAsState()
    val products by appViewModel.products.collectAsState()

    val loggedInVendorId: String? = "seed-vendor-001" //account.vendorId

    val currentVendor = vendors.find { it.vendorId == loggedInVendorId }
    val vendorProducts = products.filter { it.vendorId == loggedInVendorId }

    // controls the "Add food" panel
    var showAddSheet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        appViewModel.loadHomeData()
    }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLoginClick,
        showSearch = true,
        showNotifications = true,
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSheet = true }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add food"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                TextNormal(
                    text = currentVendor?.vendorName ?: "Vendor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )

                mySpacer()

                PrimaryButton(
                    text = "Order Status",
                    onClick = onOrderStatusClick,
                    modifier = Modifier.fillMaxWidth()
                )

                mySpacer()

                TextNormal(
                    text = "My Product(s)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }

            // vendor sees what they are listing; new items appear at the bottom
            items(
                items = vendorProducts,
                key = { it.productId }
            ) { product ->
                VendorFoodItem(
                    product = product,
                    onClick = { onFoodClick(product.productId) }
                )
            }
        }
    }

    if (showAddSheet) {
        AddFoodSheet(
            onDismiss = { showAddSheet = false },
            onSave = { name, price ->  /* , imagePath */
                val newProduct = FirebaseProductData(
                    productId = UUID.randomUUID().toString(),
                    vendorId = loggedInVendorId ?: "",
                    productName = name,
                    productPrice = price,
                    productImage =  "" /* imagePath */
//                    isAvailable = true
                )
                appViewModel.addProduct(newProduct) {
                    success -> if (success) appViewModel.loadHomeData()
                }
                showAddSheet = false
            }
        )
    }
}

@Composable
fun VendorFoodItem(
    product: FirebaseProductData,
    onClick: () -> Unit
) {
    // Seeded items use a drawable name; vendor-added items use a saved file path.
//    val imageFile = File(product.productImage)

    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        contentPadding = PaddingValues(12.dp)
    ) {
//        if (imageFile.exists()) {
//            AsyncImage(
//                model = imageFile,
//                contentDescription = product.productName,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(160.dp),
//                contentScale = ContentScale.Crop
//            )
//        } else {
//            Image(
//                painter = painterResource(getDrawableId(product.productImage)),
//                contentDescription = product.productName,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(160.dp),
//                contentScale = ContentScale.Crop
//            )
//        }  leaving blank for now

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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddFoodSheet(
    onDismiss: () -> Unit,
    onSave: (name: String, price: Double) -> Unit /* , imagePath: String */
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
//    var imageUri by remember { mutableStateOf<Uri?>(null) }

//    val picker = rememberLauncherForActivityResult(
//        ActivityResultContracts.PickVisualMedia()
//    ) { uri -> if (uri != null) imageUri = uri }

    val priceValue = price.toDoubleOrNull()
    val canSave = name.isNotBlank() && priceValue != null /* && imageUri != null */

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Add food", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price (RM)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

//            OutlinedButton(
//                onClick = {
//                    picker.launch(
//                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                    )
//                }
//            ) { Text(if (imageUri == null) "Choose Image" else "Change Image") }
//
//            imageUri?.let {
//                AsyncImage(
//                    model = it,
//                    contentDescription = "Selected image",
//                    modifier = Modifier.size(120.dp)
//                )
//            }

            Button(
                onClick = {
//                    val uri = imageUri ?: return@Button
                    val value = priceValue ?: return@Button
//                    val path = copyImageToInternalStorage(context, uri)
                    onSave(name.trim(), value) /* , path */
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save") }
        }
    }
}

// Picker URIs stop working after a restart, so copy the image into app storage
// and save that file path instead.
//private fun copyImageToInternalStorage(context: Context, uri: Uri): String {
//    val file = File(context.filesDir, "product_${System.currentTimeMillis()}.jpg")
//    context.contentResolver.openInputStream(uri)?.use { input ->
//        file.outputStream().use { output -> input.copyTo(output) }
//    }
//    return file.absolutePath
//}