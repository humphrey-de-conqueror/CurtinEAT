package com.example.curtineat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit
) {

    //calendar box show boolean
    var showDateBox by rememberSaveable() { mutableStateOf (false) }
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordConfirm by rememberSaveable { mutableStateOf("") }


    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = {
                Text("Username")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = email,
            onValueChange = { email = it},
            label = {
                Text("Email")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password
        TextField(
            value = passwordConfirm,
            onValueChange = { passwordConfirm = it },
            label = {
                Text("Confirm Password")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Gender
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.surfaceVariant)
//                .padding(8.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(80.dp)
//                    .background(
//                        if (appViewModel.selectedGender == "Male")
//                            MaterialTheme.colorScheme.primary
//                        else
//                            MaterialTheme.colorScheme.surfaceContainerHighest,
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .clickable { appViewModel.updateGender("Male") },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "♂",
//                    fontSize = 50.sp,
//                    color = if (appViewModel.selectedGender == "Male")
//                        MaterialTheme.colorScheme.onPrimary
//                    else
//                        MaterialTheme.colorScheme.onSurface
//                )
//            }
//
//            Box(
//                modifier = Modifier
//                    .weight(1f)
//                    .height(80.dp)
//                    .background(
//                        if (appViewModel.selectedGender == "Female")
//                            MaterialTheme.colorScheme.tertiary
//                        else
//                            MaterialTheme.colorScheme.surfaceContainerHighest,
//                        shape = RoundedCornerShape(8.dp)
//                    )
//                    .clickable { appViewModel.updateGender("Female") },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "♀",
//                    fontSize = 50.sp,
//                    color = if (appViewModel.selectedGender == "Female")
//                        MaterialTheme.colorScheme.onTertiary
//                    else
//                        MaterialTheme.colorScheme.onSurface
//                )
//            }
//        }

        Spacer(modifier = Modifier.height(16.dp))


        // Date of Birth
//        OutlinedTextField(
//            value = appViewModel.dob,
//            onValueChange = { appViewModel.updateDob(it)},
//
//            label = {
//                Text("Date of Birth")
//            },
//
//            placeholder = {
//                Text("DD/MM/YYYY")
//            },
//
//            trailingIcon = {
//                IconButton(
//                    onClick = { showDateBox = true
//                    }
//                ) { Icon(imageVector = Icons.Default.DateRange,
//                    contentDescription = "Select Date")
//                }
//
//            },
//
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )

        Button(
            onClick = {
//                if (!InputValidation.areAllFieldsFilled(
//                        appViewModel.username,
//                        appViewModel.email,
//                        appViewModel.password,
//                        appViewModel.passwordCheck,
//                        appViewModel.selectedGender,
//                        appViewModel.dob
//                    )
//                ) {
//                    // any empty
//                }
//                else if (!InputValidation.isValidEmail(appViewModel.email)) {
//                    // invalid email format
//                }
//                else if (!InputValidation.passwordsMatch(
//                        appViewModel.password,
//                        appViewModel.passwordCheck
//                    )
//                ) {
//                    // passwords no match
//                }
//                else {
//                    // if all ok
//                    appViewModel.CreateAccount()
//                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Create Account") }

        Button(
            onClick = { onBackButtonClick() }
        ) { Text(text = "Back") }
    }

    // Calendar dialog
//    if (showDateBox) {
//        DatePickerDialog(
//            onDismissRequest = {
//                showDateBox = false
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        datePickerState.selectedDateMillis?.let { millis ->
//                            val formatter = java.text.SimpleDateFormat(
//                                "dd/MM/yyyy",
//                                java.util.Locale.getDefault()
//                            ).apply {
//                                timeZone = java.util.TimeZone.getTimeZone("UTC")
//                            }
//
//                            appViewModel.updateDob(
//                                formatter.format(java.util.Date(millis))
//                            )
//                        }
//
//                        showDateBox = false
//                    }
//                ) {
//                    Text("OK")
//                }
//            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        showDateBox = false
//                    }
//                ) {
//                    Text("Cancel")
//                }
//            }
//        ) {
//            DatePicker(
//                state = datePickerState
//            )
//        }
//    }
}