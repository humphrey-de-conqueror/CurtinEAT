package com.example.curtineat.view

import android.content.Context
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.curtineat.viewmodel.AppViewModel
import java.io.File

@Composable
fun VendorLandingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onLogInClick: () -> Unit = {},
    onVendorToggle: (Boolean) -> Unit
) {
    // system back returns to customer mode so the toggle never gets out of sync
    BackHandler { onVendorToggle(false) }

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onLogInClick = onLogInClick,
        onVendorToggle = onVendorToggle,
        title = "Vendor",
        showSearch = false,
        showNotifications = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
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
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

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

// Picker URIs stop working after a restart, so copy the image into app storage
// and save that file path instead.
private fun copyImageToInternalStorage(context: Context, uri: Uri): String {
    val file = File(context.filesDir, "product_${System.currentTimeMillis()}.jpg")
    context.contentResolver.openInputStream(uri)?.use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    return file.absolutePath
}