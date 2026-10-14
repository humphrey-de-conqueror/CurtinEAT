package com.example.curtineat.view

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
import com.example.curtineat.ui.components.VendorCategoryDropdown

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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    20.dp
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
//                    AppText(
//                        text = "Account details",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.SemiBold
//                    )

                    // Account type selector
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
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
                                        "Register to manage your restaurant."
                                    } else {
                                        "Register to browse food and place orders."
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            message = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                if (isVendor) "Restaurant name" else "Full name"
                            )
                        },
                        singleLine = true,
                        enabled = !isRegistering,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            message = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Email address")
                        },
                        singleLine = true,
                        enabled = !isRegistering,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            message = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Password")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible) {
                                        Icons.Default.Visibility
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = if (passwordVisible) {
                                        "Show password"
                                    } else {
                                        "Hide password"
                                    }
                                )
                            }
                        },
                        singleLine = true,
                        enabled = !isRegistering,
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        supportingText = {
                            Text("At least 6 characters")
                        },
                        shape = RoundedCornerShape(14.dp)
                    )

                    // Confirm password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            message = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Confirm password")
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    confirmPasswordVisible = !confirmPasswordVisible
                                }
                            ) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) {
                                        Icons.Default.Visibility
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = if (confirmPasswordVisible) {
                                        "Show confirm password"
                                    } else {
                                        "Hide confirm password"
                                    }
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        singleLine = true,
                        enabled = !isRegistering,
//                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )

                    // vendor address
                    if (isVendor) {
                        OutlinedTextField(
                            value = location,
                            onValueChange = {
                                location = it
                                message = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text("Restaurant address")
                            },
                            placeholder = {
                                Text("Enter your restaurant address")
                            },
                            singleLine = false,
                            minLines = 2,
                            maxLines = 3,
                            enabled = !isRegistering,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(14.dp)
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

                    // Registration action
                    Button(
                        onClick = {
                            message = null

                            if (name.isBlank()) {
                                message = "Name cannot be empty"
                                return@Button
                            }

                            if (isVendor && category.isBlank()) {
                                message = "Category cannot be empty"
                                return@Button
                            }

                            if (email.isBlank()) {
                                message = "Email cannot be empty"
                                return@Button
                            }

                            if (password != confirmPassword) {
                                message = "Passwords do not match"
                                return@Button
                            }

                            if (password.isBlank()) {
                                message = "Password cannot be empty"
                                return@Button
                            }

                            if (password.length < 6) {
                                message = "Password must be at least 6 characters long"
                                return@Button
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled = !isRegistering,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        if (isRegistering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            Spacer(Modifier.size(10.dp))

                            Text("Creating account...")
                        } else {
                            Text("Create account")
                        }
                    }

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

                        TextButton(
                            onClick = onBackButtonClick,
                            enabled = !isRegistering
                        ) {
                            Text("Log in")
                        }
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

            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
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