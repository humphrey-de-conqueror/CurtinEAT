package com.example.curtineat.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.ui.components.VendorCategoryDropdown
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.UserRole

@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onLoginClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val authenticationState by
    viewModel.authenticationState.collectAsState()

    val customerState by
    viewModel.customerState.collectAsState()

    val vendorState by
    viewModel.vendorProfileState.collectAsState()

    val userId = authenticationState.userId
    val role = authenticationState.role

    var authChecked by rememberSaveable {
        mutableStateOf(false)
    }

    var isLoggingOut by rememberSaveable {
        mutableStateOf(false)
    }

    var editMode by rememberSaveable {
        mutableStateOf(false)
    }

    var profileName by rememberSaveable {
        mutableStateOf("")
    }

    var profileImage by rememberSaveable {
        mutableStateOf("")
    }

    var category by rememberSaveable {
        mutableStateOf("")
    }

    var location by rememberSaveable {
        mutableStateOf("")
    }

    var saving by rememberSaveable {
        mutableStateOf(false)
    }

    var uploadingImage by rememberSaveable {
        mutableStateOf(false)
    }

    var errorMessage by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Check authentication
    LaunchedEffect(Unit) {
        viewModel.checkCurrentUserRole {
            authChecked = true
        }
    }

    // Load correct profile
    LaunchedEffect(
        authChecked,
        userId,
        role,
        isLoggingOut
    ) {
        if (!authChecked) {
            return@LaunchedEffect
        }

        if (
            userId == null ||
            role == UserRole.NONE
        ) {
            if (!isLoggingOut) {
                onLoginClick()
            }

            return@LaunchedEffect
        }

        when (role) {
            UserRole.CUSTOMER -> {
                viewModel.observeCustomer(userId)
            }

            UserRole.VENDOR -> {
                viewModel.observeVendorProfile(userId)
            }

            UserRole.NONE -> Unit
        }
    }

    val customer = customerState.data
    val vendor = vendorState.data

    val isVendor =
        role == UserRole.VENDOR

    val isLoading =
        when (role) {
            UserRole.CUSTOMER ->
                customerState.isInitialLoading

            UserRole.VENDOR ->
                vendorState.isInitialLoading

            UserRole.NONE ->
                false
        }

    // Copy database values into editable states
    LaunchedEffect(
        customer,
        vendor,
        role,
        editMode
    ) {
        if (!editMode) {
            when (role) {
                UserRole.CUSTOMER -> {
                    customer?.let {
                        profileName = it.customerName
                        profileImage = it.customerImage
                    }
                }

                UserRole.VENDOR -> {
                    vendor?.let {
                        profileName = it.vendorName
                        profileImage = it.vendorImage
                        category = it.category
                        location = it.location
                    }
                }

                UserRole.NONE -> Unit
            }
        }
    }

    // Image picker
    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->

            if (uri != null) {
                uploadingImage = true
                errorMessage = null

                viewModel.uploadImage(uri) { imageId ->

                    if (imageId != null) {
                        profileImage = imageId
                    } else {
                        errorMessage =
                            "Unable to upload profile picture."
                    }

                    uploadingImage = false
                }
            }
        }

    // Loading
    if (!authChecked || isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (
        userId == null ||
        role == UserRole.NONE
    ) {
        return
    }

    val profileExists =
        when (role) {
            UserRole.CUSTOMER ->
                customer != null

            UserRole.VENDOR ->
                vendor != null

            UserRole.NONE ->
                false
        }

    if (!profileExists) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(
                    WindowInsets.safeDrawing
                ),
            contentAlignment = Alignment.Center
        ) {
            AppText(
                text = "Profile unavailable",
                style = MaterialTheme.typography.titleLarge
            )
        }

        return
    }

    val email =
        if (isVendor) {
            vendor!!.vendorEmail
        } else {
            customer!!.customerEmail
        }

    val scrollState =
        rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .windowInsetsPadding(
                WindowInsets.safeDrawing
            )
            .verticalScroll(scrollState)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            ),
        verticalArrangement =
            Arrangement.spacedBy(20.dp)
    ) {

        // -------------------------------------------------
        // Header
        // -------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {
            AppText(
                text = if (editMode) {
                    "Edit Profile"
                } else {
                    "My Profile"
                },
                style =
                    MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (!editMode) {
                OutlinedButton(
                    onClick = {
                        errorMessage = null

                        if (isVendor) {
                            profileName =
                                vendor!!.vendorName
                            profileImage =
                                vendor.vendorImage
                            category =
                                vendor.category
                            location =
                                vendor.location
                        } else {
                            profileName =
                                customer!!.customerName
                            profileImage =
                                customer.customerImage
                        }

                        editMode = true
                    }
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Edit,
                        contentDescription =
                            "Edit profile",
                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(
                        Modifier.width(6.dp)
                    )

                    Text("Edit Profile")
                }
            }
        }

        // -------------------------------------------------
        // Profile summary
        // -------------------------------------------------

        AppCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            contentPadding =
                PaddingValues(20.dp)
        ) {
            Column(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                Box(
                    modifier =
                        Modifier.size(120.dp),
                    contentAlignment =
                        Alignment.Center
                ) {
                    Surface(
                        modifier =
                            Modifier.fillMaxSize(),
                        shape = CircleShape,
                        color =
                            MaterialTheme.colorScheme.surface
                    ) {
                        Box(
                            contentAlignment =
                                Alignment.Center
                        ) {

                            if (
                                profileImage.isBlank()
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Default.AccountCircle,
                                    contentDescription =
                                        "Default profile picture",
                                    modifier =
                                        Modifier.size(112.dp),
                                    tint =
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                CachedImage(
                                    imageId =
                                        profileImage,
                                    viewModel =
                                        viewModel,
                                    contentDescription =
                                        "Profile picture",
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .clip(
                                                CircleShape
                                            ),
                                    retryOnTap = true
                                )
                            }

                            if (uploadingImage) {
                                Surface(
                                    modifier =
                                        Modifier.fillMaxSize(),
                                    shape =
                                        CircleShape,
                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .scrim
                                            .copy(
                                                alpha = 0.6f
                                            )
                                ) {
                                    Box(
                                        contentAlignment =
                                            Alignment.Center
                                    ) {
                                        CircularProgressIndicator(
                                            modifier =
                                                Modifier.size(
                                                    32.dp
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (editMode) {
                    OutlinedButton(
                        onClick = {
                            imagePicker.launch(
                                "image/*"
                            )
                        },
                        enabled =
                            !uploadingImage &&
                                    !saving
                    ) {
                        Icon(
                            imageVector =
                                Icons.Default.Image,
                            contentDescription = null,
                            modifier =
                                Modifier.size(18.dp)
                        )

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        Text(
                            if (uploadingImage) {
                                "Uploading..."
                            } else {
                                "Change Profile Picture"
                            }
                        )
                    }
                }

                AppText(
                    text = profileName,
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow =
                        TextOverflow.Ellipsis
                )

                // Vendor only category chip
                if (isVendor) {
                    Surface(
                        shape =
                            RoundedCornerShape(50),
                        color =
                            MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        AppText(
                            text =
                                category.ifBlank {
                                    "Uncategorized"
                                },
                            modifier =
                                Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 7.dp
                                ),
                            style =
                                MaterialTheme.typography.labelLarge,
                            color =
                                MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                AppText(
                    text = email,
                    style =
                        MaterialTheme.typography.bodyMedium,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // -------------------------------------------------
        // Account details
        // -------------------------------------------------

        // -------------------------------------------------
// Account details
// -------------------------------------------------

        AppCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            contentPadding = PaddingValues(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppText(
                    text = "Account Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                if (editMode) {

                    // Name
                    OutlinedTextField(
                        value = profileName,
                        onValueChange = {
                            profileName = it
                            errorMessage = null
                        },
                        label = {
                            Text("Name")
                        },
                        singleLine = true,
                        enabled = !saving,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Email cannot be edited
                    OutlinedTextField(
                        value = email,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Email Address")
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Vendor-only editable fields
                    if (isVendor) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            AppText(
                                text = "Category",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            VendorCategoryDropdown(
                                selectedCategory = category,
                                onCategorySelected = {
                                    category = it
                                    errorMessage = null
                                },
                                enabled = !saving
                            )
                        }

                        OutlinedTextField(
                            value = location,
                            onValueChange = {
                                location = it
                                errorMessage = null
                            },
                            label = {
                                Text("Location")
                            },
                            enabled = !saving,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )
                    }

                } else {

                    // VIEW MODE
                    ProfileInfoRow(
                        label = "Name",
                        value = profileName
                    )

                    ProfileInfoRow(
                        label = "Email Address",
                        value = email
                    )

                    if (isVendor) {
                        ProfileInfoRow(
                            label = "Category",
                            value = category
                        )

                        ProfileInfoRow(
                            label = "Location",
                            value = location
                        )
                    }
                }
            }
        }

        // -------------------------------------------------
        // Error message
        // -------------------------------------------------

        AnimatedVisibility(
            visible =
                errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(14.dp),
                color =
                    MaterialTheme.colorScheme.errorContainer
            ) {
                AppText(
                    text =
                        errorMessage.orEmpty(),
                    modifier =
                        Modifier.padding(14.dp),
                    color =
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // -------------------------------------------------
        // Buttons
        // -------------------------------------------------

        if (editMode) {

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        errorMessage = null

                        if (isVendor) {
                            profileName = vendor!!.vendorName
                            profileImage = vendor.vendorImage
                            category = vendor.category
                            location = vendor.location
                        } else {
                            profileName = customer!!.customerName
                            profileImage = customer.customerImage
                        }

                        editMode = false
                    },
                    enabled = !saving && !uploadingImage,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {

                        if (
                            profileName.isBlank()
                        ) {
                            errorMessage = "Name cannot be empty."
                            return@Button
                        }

                        if (
                            isVendor &&
                            category.isBlank()
                        ) {
                            errorMessage = "Category cannot be empty."
                            return@Button
                        }

                        saving = true
                        errorMessage = null

                        when (role) {

                            UserRole.CUSTOMER -> {

                                viewModel
                                    .updateCustomerProfile(
                                        customerId = customer!!.customerId,
                                        customerName = profileName.trim(),
                                        customerImage = profileImage
                                    ) { success ->

                                        saving = false

                                        if (success) {
                                            editMode = false
                                        } else {
                                            errorMessage = "Unable to update profile."
                                        }
                                    }
                            }

                            UserRole.VENDOR -> {

                                viewModel
                                    .updateVendorProfile(
                                        vendorId = vendor!!.vendorId,
                                        vendorName = profileName.trim(),
                                        category = category,
                                        location = location.trim(),
                                        vendorImage = profileImage
                                    ) { success ->

                                        saving = false

                                        if (success) {
                                            editMode = false
                                        } else {
                                            errorMessage = "Unable to update profile."
                                        }
                                    }
                            }

                            UserRole.NONE -> {
                                saving = false
                            }
                        }
                    },
                    enabled = !saving && !uploadingImage && profileName.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {

                    if (saving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        Spacer(Modifier.width(8.dp))

                        Text("Saving...", maxLines = 1)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(
                            Modifier.width(8.dp)
                        )

                        Text(text = "Save Changes",
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis)
                    }
                }
            }

        } else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = onHomeClick,
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text("Back to Home")
                }

                Button(
                    onClick = {
                        isLoggingOut = true
                        viewModel.signOut()
                        onHomeClick()
                    },
                    modifier =
                        Modifier.weight(1f),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                MaterialTheme.colorScheme.error,
                            contentColor =
                                MaterialTheme.colorScheme.onError
                        )
                ) {
                    Icon(
                        imageVector =
                            Icons.Default.Logout,
                        contentDescription = null,
                        modifier =
                            Modifier.size(18.dp)
                    )

                    Spacer(
                        Modifier.width(6.dp)
                    )

                    Text("Logout")
                }
            }
        }

        Spacer(
            Modifier.size(4.dp)
        )
    }


}

@Composable
private fun ProfileInfoRow(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AppText(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        AppText(
            text = value.ifBlank { "Not provided" },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}