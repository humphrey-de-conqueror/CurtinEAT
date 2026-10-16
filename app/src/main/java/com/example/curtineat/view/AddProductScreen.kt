package com.example.curtineat.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.style.TextOverflow
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.components.ConfirmDialog

private const val IMAGE_BASE_URL =
    "https://curtineat-image-api.work-gordonyewyangliew.workers.dev/images/"

@Composable
fun AddProductScreen(
    viewModel: AppViewModel,
    productId: String? = null,
    onBackClick: () -> Unit
) {
    val productState by
    viewModel.productState.collectAsState()

    val existingProduct =
        productId?.let { id ->
            productState.data.firstOrNull {
                it.productId == id
            }
        }

    val isExistingProduct =
        productId != null

    var editMode by rememberSaveable(productId) {
        mutableStateOf(!isExistingProduct)
    }

    var showDeleteDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var productName by rememberSaveable(productId) {
        mutableStateOf("")
    }

    var productPrice by rememberSaveable(productId) {
        mutableStateOf("")
    }

    var productImage by rememberSaveable(productId) {
        mutableStateOf("")
    }

    var isAvailable by rememberSaveable(productId) {
        mutableStateOf(true)
    }

    var uploadingImage by remember { mutableStateOf(false) }
    var saving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val authenticationState by
    viewModel.authenticationState.collectAsState()

    val vendorId = authenticationState.userId.takeIf {
        authenticationState.role == UserRole.VENDOR
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            uploadingImage = true
            errorMessage = null

            viewModel.uploadImage(uri) { imageId ->
                if (imageId != null) {
                    productImage = imageId
                } else {
                    errorMessage = "Unable to upload product image."
                }

                uploadingImage = false
            }
        }
    }

    val parsedPrice = productPrice.toDoubleOrNull()
    val validPrice = parsedPrice != null &&
            parsedPrice.isFinite() &&
            parsedPrice >= 0.0

    val scrollState = rememberScrollState()

    LaunchedEffect(
        existingProduct,
        editMode
    ) {
        if (existingProduct != null && !editMode) {
            productName = existingProduct.productName
            productPrice = existingProduct.productPrice.toString()
            productImage = existingProduct.productImage
            isAvailable = existingProduct.isAvailable
        }
    }

    if (
        isExistingProduct && existingProduct == null
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(scrollState)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Page heading
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AppText(
                    text =
                        if (isExistingProduct) {
                            "Product Details"
                        } else {
                            "Add Product"
                        },
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                        if (isExistingProduct) {
                            "View and manage this menu item."
                        } else {
                            "Add a new item to your restaurant menu."
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (
                isExistingProduct && !editMode) {
                OutlinedButton(
                    onClick = {
                        editMode = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit product"
                    )

                    Spacer(
                        Modifier.width(6.dp)
                    )

                    Text(text = "Edit Product",
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis)
                }
            }
        }

        // Product image section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(42.dp)
                                .padding(10.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "Product Photo",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Choose a clear photo of your food.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (productImage.isBlank()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fastfood,
                                contentDescription = "Product image placeholder",
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "No image selected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        AsyncImage(
                            model = IMAGE_BASE_URL + productImage,
                            contentDescription = "Product image preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    if (uploadingImage) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.scrim.copy(
                                alpha = 0.55f
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    color = MaterialTheme.colorScheme.inversePrimary
                                )

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = "Uploading image...",
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }

                if (editMode) {
                    OutlinedButton(
                        onClick = {
                            imagePicker.launch("image/*")
                        },
                        enabled = !uploadingImage && !saving,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = if (uploadingImage) {
                                "Uploading..."
                            } else if (productImage.isBlank()) {
                                "Choose Product Image"
                            } else {
                                "Change Product Image"
                            }
                        )
                    }
                }
            }
        }

        // Product details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Product Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = productName,
                    onValueChange = {
                        if (editMode) {
                            productName = it
                            errorMessage = null
                        }
                    },
                    readOnly = !editMode,
                    label = { Text("Product Name") },
                    placeholder = { Text("e.g. Chicken Rice") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    enabled = !saving,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = productPrice,
                    onValueChange = {
                        if (editMode) {
                            productPrice = it
                            errorMessage = null
                        }
                    },
                    readOnly = !editMode,
                    label = { Text("Price (RM)") },
                    placeholder = { Text("0.00") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Payments,
                            contentDescription = null
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    enabled = !saving,
                    isError = productPrice.isNotBlank() && !validPrice,
                    supportingText = {
                        if (productPrice.isNotBlank() && !validPrice) {
                            Text("Enter a valid non-negative price.")
                        } else {
                            Text("Enter the price in Malaysian Ringgit.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                // Availability control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Available for ordering",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = if (isAvailable) {
                                "Customers can order this product."
                            } else {
                                "This product is currently unavailable."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Switch(
                        checked = isAvailable,
                        onCheckedChange = {
                            isAvailable = it
                        },
                        enabled = editMode && !saving
                    )
                }
            }
        }

        // Error feedback
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = errorMessage.orEmpty(),
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Actions
        if (isExistingProduct && !editMode) {

            // VIEW EXISTING PRODUCT
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        showDeleteDialog = true
                    },
                    enabled = !saving,
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete product",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text("Delete")
                }
            }

        } else {

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        if (isExistingProduct) {
                            editMode = false
                            errorMessage = null

                        } else {
                            onBackClick()
                        }
                    },
                    enabled = !saving && !uploadingImage,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {

                        if (vendorId == null) {
                            errorMessage = "Please sign in with a vendor account."
                            return@Button
                        }

                        val price =
                            productPrice.toDoubleOrNull()

                        if (
                            price == null ||
                            !price.isFinite() ||
                            price < 0.0
                        ) {
                            errorMessage = "Enter a valid non-negative price."
                            return@Button
                        }

                        if (productName.isBlank()) {
                            errorMessage = "Enter a product name."
                            return@Button
                        }

                        if (productImage.isBlank()) {
                            errorMessage = "Please upload a product image."
                            return@Button
                        }

                        saving = true
                        errorMessage = null

                        if (
                            isExistingProduct &&
                            existingProduct != null
                        ) {

                            // UPDATE EXISTING PRODUCT
                            viewModel.updateProduct(
                                productId = existingProduct.productId,
                                vendorId = existingProduct.vendorId,
                                productName = productName.trim(),
                                productPrice = price,
                                productImage = productImage,
                                isAvailable = isAvailable
                            ) { success ->

                                saving = false

                                if (success) {
                                    editMode = false

                                } else {
                                    errorMessage = "Unable to update product. Please try again."
                                }
                            }

                        } else {

                            // ADD NEW PRODUCT
                            viewModel.addProduct(
                                vendorId = vendorId,
                                productName = productName.trim(),
                                productPrice = price,
                                productImage = productImage,
                                isAvailable = isAvailable
                            ) { success ->

                                saving = false

                                if (success) {
                                    onBackClick()

                                } else {
                                    errorMessage = "Unable to save product. Please try again."
                                }
                            }
                        }
                    },
                    enabled = !saving && !uploadingImage && vendorId != null &&
                                productName.isNotBlank() && validPrice && productImage.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {

                    if (saving) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Saving...")

                    } else {

                        Text(
                            if (isExistingProduct) {
                                "Save Changes"
                            } else {
                                "Save Product"
                            }
                        )
                    }
                }
            }
        }


    }

    if (
        showDeleteDialog &&
        existingProduct != null
    ) {
        ConfirmDialog(
            title = "Delete Product",
            message =
                "Are you sure you want to delete ${existingProduct.productName}?",
            confirmText = "Delete",
            destructive = true,
            onDismiss = {
                showDeleteDialog = false
            },
            onConfirm = {

                showDeleteDialog = false
                saving = true

                viewModel.deleteProduct(
                    productId =
                        existingProduct.productId,
                    vendorId =
                        existingProduct.vendorId
                ) { success ->

                    saving = false

                    if (success) {
                        onBackClick()
                    } else {
                        errorMessage =
                            "Unable to delete product. Please try again."
                    }
                }
            }
        )
    }
}