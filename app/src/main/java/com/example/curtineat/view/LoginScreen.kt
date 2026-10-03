package com.example.curtineat.view

import androidx.compose.runtime.Composable
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    onLoginSuccess: (Boolean) -> Unit,
    onRegistrationClick: () -> Unit,
    onHomeClick: () -> Unit,
    onBackButtonClick: () -> Unit
) {}



//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import com.example.curtineat.viewmodel.AppViewModel
//
//@Composable
//fun LoginScreen(
//    appViewModel: AppViewModel,
//    onLoginSuccess: (Boolean) -> Unit,
//    onRegistrationClick: () -> Unit,
//    onHomeClick: () -> Unit,
//    onBackButtonClick: () -> Unit
//) {
//    var email by remember {
//        mutableStateOf("")
//    }
//
//    var password by remember {
//        mutableStateOf("")
//    }
//
//    var isVendor by remember {
//        mutableStateOf(false)
//    }
//
//    var loginError by remember {
//        mutableStateOf(false)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//
//        Text(
//            text = if (isVendor) {
//                "Vendor Login"
//            } else {
//                "Customer Login"
//            }
//        )
//
//        Spacer(
//            modifier = Modifier.height(24.dp)
//        )
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.End,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//            Text(
//                text = if (isVendor) {
//                    "Vendor  "
//                } else {
//                    "Customer  "
//                }
//            )
//
//            Spacer(
//                modifier = Modifier.height(0.dp)
//            )
//
//            Switch(
//                checked = isVendor,
//                onCheckedChange = {
//                    isVendor = it
//                    loginError = false
//                }
//            )
//        }
//
//        Spacer(
//            modifier = Modifier.height(8.dp)
//        )
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = {
//                email = it
//                loginError = false
//            },
//            label = {
//                Text("Email")
//            },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = {
//                password = it
//                loginError = false
//            },
//            label = {
//                Text("Password")
//            },
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(
//            modifier = Modifier.height(24.dp)
//        )
//
//        Button(
//            onClick = {
//                appViewModel.login(
//                    email = email,
//                    password = password,
//                    isVendor = isVendor
//                ) { success ->
//
//                    if (success) {
//                        loginError = false
//                        onLoginSuccess(isVendor)
//                    } else {
//                        loginError = true
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//
//        if (loginError) {
//            Spacer(
//                modifier = Modifier.height(12.dp)
//            )
//
//            Text(
//                text = "Invalid email or password"
//            )
//        }
//
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//        TextButton(
//            onClick = onRegistrationClick
//        ) {
//            Text("Don't have an account? Register")
//        }
//
//        Spacer(
//            modifier = Modifier.height(12.dp)
//        )
//
//        TextButton(
//            onClick = onBackButtonClick
//        ) {
//            Text("Back")
//        }
//    }
//}

//package com.example.curtineat.view
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.DateRange
//import androidx.compose.material3.Button
//import androidx.compose.material3.DatePicker
//import androidx.compose.material3.DatePickerDialog
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Switch
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TextField
//import androidx.compose.material3.rememberDatePickerState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.modifier.modifierLocalConsumer
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.unit.dp
//import com.example.curtineat.viewmodel.AppViewModel
//import com.example.curtineat.validation.InputValidation
//
//
//@Composable
//fun LogIn(
//    onRegisterHit: () -> Unit,
//    viewModel: AppViewModel ){
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp),
//        verticalArrangement = Arrangement.Top){
//
//
//        Text( text = "Welcome to CurtinEat o reverend one",
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 24.dp),
//            textAlign = TextAlign.Center)
//
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//        TextField(
//            value = viewModel.email,
//            onValueChange = {
//                viewModel.updateEmail(it)
//            },
//            label = {
//                Text("Email")
//            } ,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//
//        TextField(
//            value = viewModel.password,
//            onValueChange = {
//                viewModel.updatePassword(it)
//            },
//            label = {
//                Text("Password")
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//
//
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//
//        Button(
//            onClick = {  viewModel.Login() },
//            modifier = Modifier
//                .width(200.dp)
//                .align(Alignment.CenterHorizontally)
//        )
//        { Text("Log In") }
//
//
//        Text(
//            text = "Create New Account",
//            modifier = Modifier
//                .align(Alignment.CenterHorizontally)
//                .clickable {
//                    onRegisterHit()
//                }
//        )
//
//
//    }
//}
//
//
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Registeration(
//    viewModel: AppViewModel
//) {
//
//
//    //calendar box show boolean
//    var showDateBox by remember { mutableStateOf (false)}
//    val datePickerState = rememberDatePickerState()
//
//
//    var isVendor by remember { mutableStateOf(false)}
//
//
//    Column(
//        modifier = Modifier.fillMaxWidth().padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Top
//
//
//    ) {
//
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//
//        //toggler
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = if (isVendor) "Vendor" else "Customer"
//            )
//
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//
//            Switch(
//                checked = isVendor,
//                onCheckedChange = { isVendor = it }
//            )
//        }
//
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//
//
//
//
//
//
//
//        if (!isVendor) {
//
//
//            // Set A - Customer details
//            // Username
//            TextField(
//                value = viewModel.username,
//                onValueChange = {
//                    viewModel.updateUsername(it)
//                },
//                label = {
//                    Text("Username")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Email
//            TextField(
//                value = viewModel.email,
//                onValueChange = {
//                    viewModel.updateEmail(it)
//                },
//                label = {
//                    Text("Email")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Password
//            TextField(
//                value = viewModel.password,
//                onValueChange = {
//                    viewModel.updatePassword(it)
//                },
//                label = {
//                    Text("Password")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Confirm Password
//            TextField(
//                value = viewModel.passwordCheck,
//                onValueChange = {
//                    viewModel.updatePasswordCheck(it)
//                },
//                label = {
//                    Text("Confirm Password")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//
//
//            // Gender
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.surfaceVariant)
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//
//
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(80.dp)
//                        .background(
//                            if (viewModel.selectedGender == "Male")
//                                MaterialTheme.colorScheme.primary
//                            else
//                                MaterialTheme.colorScheme.surfaceContainerHighest,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .clickable { viewModel.updateGender("Male") },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "♂",
//                        fontSize = 50.sp,
//                        color = if (viewModel.selectedGender == "Male")
//                            MaterialTheme.colorScheme.onPrimary
//                        else
//                            MaterialTheme.colorScheme.onSurface
//                    )
//                }
//
//
//                Box(
//                    modifier = Modifier
//                        .weight(1f)
//                        .height(80.dp)
//                        .background(
//                            if (viewModel.selectedGender == "Female")
//                                MaterialTheme.colorScheme.tertiary
//                            else
//                                MaterialTheme.colorScheme.surfaceContainerHighest,
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .clickable { viewModel.updateGender("Female") },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = "♀",
//                        fontSize = 50.sp,
//                        color = if (viewModel.selectedGender == "Female")
//                            MaterialTheme.colorScheme.onTertiary
//                        else
//                            MaterialTheme.colorScheme.onSurface
//                    )
//                }
//            }
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//
//
//            // Date of Birth
//            OutlinedTextField(
//                value = viewModel.dob,
//                onValueChange = { viewModel.updateDob(it) },
//
//
//                label = {
//                    Text("Date of Birth")
//                },
//
//
//                placeholder = {
//                    Text("DD/MM/YYYY")
//                },
//
//
//                trailingIcon = {
//                    IconButton(
//                        onClick = {
//                            showDateBox = true
//                        }
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.DateRange,
//                            contentDescription = "Select Date"
//                        )
//                    }
//
//
//                },
//
//
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true
//            )
//
//
//            Button(
//                onClick = {
//
//
//                    if (!InputValidation.areAllFieldsFilled(
//                            viewModel.username,
//                            viewModel.email,
//                            viewModel.password,
//                            viewModel.passwordCheck,
//                            viewModel.selectedGender,
//                            viewModel.dob
//                        )
//                    ) {
//                        // any empty
//                    } else if (!InputValidation.isValidEmail(viewModel.email)) {
//                        // invalid email format
//                    } else if (!InputValidation.passwordsMatch(
//                            viewModel.password,
//                            viewModel.passwordCheck
//                        )
//                    ) {
//                        // passwords no match
//                    } else {
//                        // if all ok
//                        viewModel.CreateAccount()
//                    }
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//            { Text("Create Account") }
//        } else {
//
//
//
//
//            //Set B - Vendor details
//            TextField(
//                value = viewModel.vendorName,
//                onValueChange = {
//                    viewModel.updateVendorName(it)
//                },
//                label = {
//                    Text("Vendor name")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Email
//            TextField(
//                value = viewModel.vendorEmail,
//                onValueChange = {
//                    viewModel.updateVendorEmail(it)
//                },
//                label = {
//                    Text("Email")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Password
//            TextField(
//                value = viewModel.vendorPassword,
//                onValueChange = {
//                    viewModel.updateVendorPassword(it)
//                },
//                label = {
//                    Text("Password")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//            // Confirm Password
//            TextField(
//                value = viewModel.vendorPasswordCheck,
//                onValueChange = {
//                    viewModel.updateVendorPasswordCheck(it)
//                },
//                label = {
//                    Text("Confirm Password")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            TextField(
//                value = viewModel.vendorRating,
//                onValueChange = {
//                    viewModel.updateVendorRating(it)
//                },
//                label = {
//                    Text("Rating")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            TextField(
//                value = viewModel.category,
//                onValueChange = {
//                    viewModel.updateCategory(it)
//                },
//                label = {
//                    Text("Category")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            TextField(
//                value = viewModel.distance,
//                onValueChange = {
//                    viewModel.updateDistance(it)
//                },
//                label = {
//                    Text("Distance")
//                },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//
//            Button(
//                onClick = {
//                    //Vendor valid stuff
//                },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Create Vendor Account")
//            }
//
//
//        }
//    }
//
//
//
//
//    // Calendar dialog
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
//
//                            viewModel.updateDob(
//                                formatter.format(java.util.Date(millis))
//                            )
//                        }
//
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
//}
//
//
//
//
//
//
//
//
//
//
//
//