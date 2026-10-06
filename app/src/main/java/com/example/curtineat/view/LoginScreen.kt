package com.example.curtineat.view

import android.R.attr.fontWeight
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.ui.theme.BackButton
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.viewmodel.AppViewModel
import androidx.compose.material3.Text
import com.example.curtineat.ui.theme.mySpacerWidth

@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onRegistrationClick: () -> Unit,
    onHomeClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
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
            .safeDrawingPadding()
            .padding(24.dp)
    ) {

        BackButton(
            onClick = onBackButtonClick,
            modifier = Modifier.align(
                Alignment.TopStart
            )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {

            TextNormal(
                text = "Log In",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            TextNormal(
                text = "Welcome back to CurtinEAT",
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = {
                    TextNormal(
                        text = "Email"
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = {
                    TextNormal(
                        text = "Password"
                    )
                },
                modifier =
                    Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation =
                    PasswordVisualTransformation()
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isVendor = !isVendor
                        errorMessage = null
                    },
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = isVendor,
                    onCheckedChange = null
                )

                mySpacerWidth()

                TextNormal(
                    text = "Are you a vendor?",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            PrimaryButton(
                text = "Login",
                enabled =
                    !isLoginLoading &&
                            email.isNotBlank() &&
                            password.isNotBlank(),
                onClick = {

                    errorMessage = null
                    isLoginLoading = true

                    appViewModel.login(
                        email = email,
                        password = password,
                        isVendor = isVendor
                    ) { success ->

                        isLoginLoading = false

                        if (success) {

                            onLoginSuccess(
                                isVendor
                            )

                        } else {

                            errorMessage =
                                if (isVendor) {
                                    "Invalid login or this account is not registered as a vendor."
                                } else {
                                    "Invalid login or this account is not registered as a customer."
                                }
                        }
                    }
                },
                modifier =
                    Modifier.fillMaxWidth()
            )

            if (isLoginLoading) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                CircularProgressIndicator()
            }

            errorMessage?.let { message ->

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TextNormal(
                    text = message,
                    color =
                        MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Don't have an account? Register",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    onRegistrationClick()
                }
            )
        }
    }
}