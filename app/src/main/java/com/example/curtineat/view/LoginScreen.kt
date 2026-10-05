package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.example.curtineat.R
import com.example.curtineat.ui.theme.BackButton
import com.example.curtineat.ui.theme.PrimaryButton
import com.example.curtineat.ui.theme.TextNormal
import com.example.curtineat.viewmodel.AppViewModel
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import androidx.credentials.exceptions.NoCredentialException
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox


@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onRegistrationClick: () -> Unit,
    onHomeClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {

    val context = LocalContext.current

    val webClientId = stringResource(R.string.default_web_client_id)

    val coroutineScope = rememberCoroutineScope()

    val credentialManager = remember {
        CredentialManager.create(context)
    }

    val isLoading by appViewModel.isLoading.collectAsState()

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isVendor by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {

        BackButton(
            onClick = onBackButtonClick, modifier = Modifier.align(
                Alignment.TopStart
            )
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextNormal(
                text = "Welcome to CurtinEAT", fontWeight = FontWeight.Bold, fontSize = 28.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            TextNormal(
                text = "Sign in to continue", fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(32.dp)
            )



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        isVendor = !isVendor
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {

                Checkbox(
                    checked = isVendor,
                    onCheckedChange = null
                )

                TextNormal(
                    text = "Are you a vendor?",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            PrimaryButton(
                text = "Continue with Google", enabled = !isLoading, onClick = {

                    errorMessage = null

                    coroutineScope.launch {

                        try {

                            /*
                             * Google account picker.
                             */

                            val googleSignInOption = GetSignInWithGoogleOption.Builder(
                                serverClientId = webClientId
                            ).build()

                            val request = GetCredentialRequest.Builder().addCredentialOption(
                                googleSignInOption
                            ).build()


                            val result = credentialManager.getCredential(
                                context = context, request = request
                            )


                            val credential = result.credential


                            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                                val googleCredential = GoogleIdTokenCredential.createFrom(
                                    credential.data
                                )


                                appViewModel.signInWithGoogle(
                                    idToken = googleCredential.idToken, isVendor = isVendor
                                ) { success, isVendor, message ->

                                    if (success) {

                                        errorMessage = null

                                        onLoginSuccess(
                                            isVendor
                                        )

                                    } else {

                                        errorMessage = message
                                    }
                                }

                            } else {

                                errorMessage = "Invalid Google credential."
                            }


                        } catch (
                            e: GetCredentialCancellationException
                        ) {

                            // User closed Google account picker.
                            // No error message needed.

                        } catch (
                            e: NoCredentialException
                        ) {

                            errorMessage =
                                "No Google account available. Please sign in to a Google account on this device."

                        } catch (
                            e: GetCredentialException
                        ) {

                            errorMessage = e.message ?: "Google sign in failed."

                        } catch (
                            e: Exception
                        ) {

                            errorMessage = e.message ?: "Google sign in failed."
                        }
                    }
                }, modifier = Modifier.fillMaxWidth()
            )


            if (isLoading) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                CircularProgressIndicator()
            }


            errorMessage?.let { message ->

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                TextNormal(
                    text = message, color = MaterialTheme.colorScheme.error, fontSize = 14.sp
                )
            }
        }
    }
}