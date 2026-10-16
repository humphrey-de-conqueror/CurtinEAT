package com.example.curtineat.view

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.curtineat.ui.components.AppBackButton
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.RegisterResult
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.VisualTransformation
import com.example.curtineat.ui.components.AppButton
import com.example.curtineat.ui.components.AppButtonStyle
import com.example.curtineat.ui.components.AppTextField
import com.example.curtineat.ui.components.VendorCategoryDropdown
import androidx.compose.foundation.background

@Composable
fun RegistrationScreen(
    appViewModel: AppViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onHomeClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isVendor by rememberSaveable {
        mutableStateOf(false)
    }

    var message by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var name by rememberSaveable {
        mutableStateOf("")
    }

    var category by rememberSaveable {
        mutableStateOf("")
    }

    var isRegistering by rememberSaveable {
        mutableStateOf(false)
    }

    var location by rememberSaveable {
        mutableStateOf("")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 64.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppText(
                text = "Create an account",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            AppText(
                text = "Join CurtinEAT to discover food or manage your restaurant.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))
            AppCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(20.dp),
                showShadow = false
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Account type selector
                    AppCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                        showShadow = true
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                AppText(
                                    text = if (isVendor) {
                                        "Vendor account"
                                    } else {
                                        "Customer account"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                AppText(
                                    text = if (isVendor) {
                                        "Register as a Vendor."
                                    } else {
                                        "Register as a Customer."
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Switch(
                                checked = isVendor,
                                onCheckedChange = {
                                    isVendor = it
                                    message = null
                                },
                                enabled = !isRegistering
                            )
                        }
                    }

                    // Name
                    AppTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            message = null
                        },
                        label = if (isVendor) "Restaurant name" else "Full name",
                        enabled = !isRegistering,
                        imeAction = ImeAction.Next
                    )

                    // Email
                    AppTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            message = null
                        },
                        label = "Email address",
                        enabled = !isRegistering,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )

                    // Password
                    AppTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            message = null
                        },
                        label = "Password",
                        supportingText = "At least 6 characters",
                        enabled = !isRegistering,
                        isPassword = true,
                        imeAction = ImeAction.Next
                    )

                    // Confirm password
                    AppTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            message = null
                        },
                        label = "Confirm password",
                        enabled = !isRegistering,
                        isPassword = true,
                        imeAction = ImeAction.Done
                    )

                    // Vendor address
                    if (isVendor) {
                        AppTextField(
                            value = location,
                            onValueChange = {
                                location = it
                                message = null
                            },
                            label = "Restaurant address",
                            placeholder = "Enter your restaurant address",
                            singleLine = false,
                            minLines = 2,
                            maxLines = 3,
                            enabled = !isRegistering,
                            imeAction = ImeAction.Next
                        )
                    }

                    // Vendor-only category
                    if (isVendor) {
                        VendorCategoryDropdown(
                            selectedCategory = category,
                            onCategorySelected = {
                                category = it
                                message = null
                            },
                            enabled = !isRegistering
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    // Registration action

                    //Apphere
                    AppButton(
                        text = "Create account",
                        loading = isRegistering,
                        loadingText = "Creating account...",
                        onClick = {
                            message = null

                            if (name.isBlank()) {
                                message = "Name cannot be empty"
                                return@AppButton
                            }

                            if (isVendor && category.isBlank()) {
                                message = "Category cannot be empty"
                                return@AppButton
                            }

                            if (email.isBlank()) {
                                message = "Email cannot be empty"
                                return@AppButton
                            }

                            if (password != confirmPassword) {
                                message = "Passwords do not match"
                                return@AppButton
                            }

                            if (password.isBlank()) {
                                message = "Password cannot be empty"
                                return@AppButton
                            }

                            if (password.length < 6) {
                                message = "Password must be at least 6 characters long"
                                return@AppButton
                            }

                            isRegistering = true

                            appViewModel.register(
                                email = email.trim(),
                                password = password,
                                name = name.trim(),
                                category = category.trim(),
                                isVendor = isVendor
                            ) { result ->
                                isRegistering = false

                                when (result) {
                                    RegisterResult.SUCCESS -> {
                                        onLoginSuccess(isVendor)
                                    }

                                    RegisterResult.INVALID_EMAIL -> {
                                        message = "Please enter a valid email"
                                    }

                                    RegisterResult.EMAIL_EXISTS -> {
                                        message =
                                            "An account with this email already exists"
                                    }

                                    RegisterResult.ERROR -> {
                                        message =
                                            "Registration failed. Please try again"
                                    }
                                }
                            }
                        },
                        modifier = Modifier.height(52.dp),
                        fullWidth = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = "Already have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        AppButton(
                            text = "Log in",
                            onClick = onBackButtonClick,
                            style = AppButtonStyle.TEXT,
                            enabled = !isRegistering
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        // Temporary message
        if (message != null) {
            LaunchedEffect(message) {
                delay(3000)
                message = null
            }


            AppCard(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(16.dp),
                showShadow = true
            ) {
                AppText(
                    text = message.orEmpty(),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        AppBackButton(
            onClick = onBackButtonClick,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }
}