package com.example.curtineat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.curtineat.ui.components.AppBackButton
import com.example.curtineat.ui.components.AppText
import com.example.curtineat.viewmodel.AppViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.VisualTransformation
import com.example.curtineat.ui.components.AppButton
import com.example.curtineat.ui.components.AppButtonStyle
import com.example.curtineat.ui.components.AppCard
import com.example.curtineat.ui.components.AppTextField

@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onRegistrationClick: () -> Unit,
    onHomeClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isVendor by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isLoginLoading by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) //****//
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        AppBackButton(
            onClick = onBackButtonClick,
            modifier = Modifier.align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(top = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppText(
                text = "Welcome back",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(8.dp))

            AppText(
                text = "Sign in to continue to CurtinEAT",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            //Apphere
            AppCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AppText(
                        text = "Log in",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    AppTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = null
                        },
                        label = "Email address",
                        leadingIcon = Icons.Default.Mail,
                        enabled = !isLoginLoading,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                    //app here
                    AppTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = "Password",
                        leadingIcon = Icons.Default.Lock,
                        enabled = !isLoginLoading,
                        isPassword = true,
                        imeAction = ImeAction.Done

                    )

                    // Account type selection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !isLoginLoading) {
                                isVendor = !isVendor
                                errorMessage = null
                            }
                            .padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isVendor,
                            onCheckedChange = {
                                isVendor = it
                                errorMessage = null
                            },
                            enabled = !isLoginLoading
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            AppText(
                                text = "I'm a vendor",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )

                            AppText(
                                text = "Select this if you manage a restaurant.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Login error
                    if (errorMessage != null) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            AppText(
                                text = errorMessage.orEmpty(),
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    // Login action

                    //Apphere
//                    Button(
//                        onClick = {
//                            errorMessage = null
//                            isLoginLoading = true
//
//                            appViewModel.login(
//                                email = email.trim(),
//                                password = password,
//                                isVendor = isVendor
//                            ) { success ->
//                                isLoginLoading = false
//
//                                if (success) {
//                                    onLoginSuccess(isVendor)
//                                } else {
//                                    errorMessage = if (isVendor) {
//                                        "Invalid login or this account is not registered as a vendor."
//                                    } else {
//                                        "Invalid login or this account is not registered as a customer."
//                                    }
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(52.dp),
//                        enabled = !isLoginLoading &&
//                                email.isNotBlank() &&
//                                password.isNotBlank(),
//                        shape = RoundedCornerShape(14.dp)
//                    ) {
//                        if (isLoginLoading) {
//                            CircularProgressIndicator(
//                                modifier = Modifier.size(20.dp),
//                                strokeWidth = 2.dp,
//                                color = MaterialTheme.colorScheme.onPrimary
//                            )
//
//                            Spacer(Modifier.size(10.dp))
//
//                            //Apphere
//
//                            Text("Signing in...")
//                        } else {
//                            Text(
//                                text = "Log in",
//                                style = MaterialTheme.typography.labelLarge
//                            )
//                        }
//                    }
//
//                    HorizontalDivider(
//                        color = MaterialTheme.colorScheme.outlineVariant
//                    )
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        AppText(
//                            text = "Don't have an account?",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
//
//                        TextButton(
//                            onClick = onRegistrationClick,
//                            enabled = !isLoginLoading
//                        ) {
//                            Text("Register")

                    AppButton(
                        text = "Log in",
                        loading = isLoginLoading,
                        loadingText = "Signing in...",
                        onClick = {
                            errorMessage = null
                            isLoginLoading = true

                            appViewModel.login(
                                email = email.trim(),
                                password = password,
                                isVendor = isVendor
                            ) { success ->
                                isLoginLoading = false

                                if (success) {
                                    onLoginSuccess(isVendor)
                                } else {
                                    errorMessage = if (isVendor) {
                                        "Invalid login or this account is not registered as a vendor."
                                    } else {
                                        "Invalid login or this account is not registered as a customer."
                                    }
                                }
                            }
                        },
                        modifier = Modifier.height(52.dp),
                        fullWidth = true,
                        enabled = email.isNotBlank() && password.isNotBlank()
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppText(
                            text = "Don't have an account?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        AppButton(
                            text = "Register",
                            onClick = onRegistrationClick,
                            style = AppButtonStyle.TEXT,
                            enabled = !isLoginLoading
                        )
                        }
                    }
                }
            }
        }
    }