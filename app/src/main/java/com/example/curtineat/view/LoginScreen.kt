package com.example.curtineat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun LoginScreen(
    appViewModel: AppViewModel,
    onRegistrationClick: () -> Unit,
    onBackButtonClick: () -> Unit
){
    var email by rememberSaveable { mutableStateOf("")}
    var password by rememberSaveable { mutableStateOf("")}

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Top){

        Text( text = "Welcome to CurtinEat o reverend one",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = {
                Text("Email")
            } ,
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text("Password")
            },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(16.dp))

        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {}
            ) { Text(text = "login as Customer")}

            Button(
                onClick = {}
            ) { Text(text = "login as Vendor")}
        }

        Text(
            text = "No account? Create New Account",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    onRegistrationClick()
                }
        )

        Button (
            onClick = { onBackButtonClick() }
        ) {
            Text(text = "Back")
        }

    }
}