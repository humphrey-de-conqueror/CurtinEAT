//package com.example.curtineat.view
//
//
//
//import android.widget.NumberPicker
//import com.example.curtineat.R
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.curtineat.ui.theme.PrimaryCard
//import com.example.curtineat.ui.theme.TextNormal
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.size
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.material3.Icon
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import com.example.curtineat.ui.theme.mySpacer
//
//@Composable
//fun LogIn(){
//
//    // var email by remember { mutableStateOf("")}
//    // var password by remember { mutableStateOf("")}
//
//    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top){
//
//        Text( text = "Welcome to CurtinEat o reverend one")
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        TextField(value = email, onValueChange = {email = it})
//        TextField(value = password, onValueChange = {password = it})
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = { /* save details */ }, modifier = Modifier.fillMaxWidth()) {Text ("Log In")}
//
//        Text(
//            text = "Register Account",
//            modifier = Modifier.clickable {
//                // the function to open register page
//            }
//        )
//
//    }
//}
//
//@Composable
//fun Registeration()
//{
//
////     var selectedGender by remember { mutableStateOf("")}
////     var username by remember { mutableStateOf("")}
////     var email by remember { mutableStateOf("")}
////     var password by remember { mutableStateOf("")}
////     var passwordcheck by remember { mutableStateOf("")}
////     var dob by remember { mutableStateOf("") }
//
//    Column( horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)){
//        TextField(value = username, onValueChange = {username = it})
//        TextField(value = email, onValueChange = {email = it})
//        TextField(value = password, onValueChange = {password = it})
//        TextField(value = passwordcheck, onValueChange = {passwordcheck = it})
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//
//            Text(
//                text = "♂",
//                fontSize = 50.sp,
//                color = if (selectedGender == "Male")
//                    Color.Blue
//                else
//                    Color.Gray,
//                modifier = Modifier.clickable {
//                    selectedGender = "Male"
//                }
//            )
//
//            //Spacer(modifier = Modifier.width(40.dp))
//
//            Text(
//                text = "♀",
//                fontSize = 50.sp,
//                color = if (selectedGender == "Female")
//                    Color.Magenta
//                else
//                    Color.Gray,
//                modifier = Modifier.clickable {
//                    selectedGender = "Female"
//                }
//            )
//        }
//
//
//        OutlinedTextField(
//            value = dob,
//            onValueChange = { },
//            readOnly = true,
//            label = { Text("Date of Birth") },
//            placeholder = { Text("DD/MM/YYYY") },
//            trailingIcon = {
//                Icon(
//                    imageVector = Icons.Default.DateRange,
//                    contentDescription = "Select date"
//                )
//            },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//    }
//}
//
// everything above is before mvvm

package com.example.curtineat.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.curtineat.viewmodel.AuthViewModel

@Composable
fun LogIn( viewModel: AuthViewModel = viewModel() ){
    Column(verticalArrangement = Arrangement.Top){

        Text( text = "Welcome to CurtinEat o reverend one" )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            label = {
                Text("Email")
            }
        )

        TextField(
            value = viewModel.password,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            label = {
                Text("Password")
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {  viewModel.Login() },
            modifier = Modifier.fillMaxWidth()
        )
        { Text("Log In") }

        Text(
            text = "Register Account",
            modifier = Modifier.clickable {
                // the function to open register page
            }
        )

    }
}



@Composable
fun Registeration(
    viewModel: AuthViewModel = viewModel()
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth()
    ) {

        // Username
        TextField(
            value = viewModel.username,
            onValueChange = {
                viewModel.updateUsername(it)
            },
            label = {
                Text("Username")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        TextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.updateEmail(it)
            },
            label = {
                Text("Email")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password
        TextField(
            value = viewModel.password,
            onValueChange = {
                viewModel.updatePassword(it)
            },
            label = {
                Text("Password")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Password
        TextField(
            value = viewModel.passwordCheck,
            onValueChange = {
                viewModel.updatePasswordCheck(it)
            },
            label = {
                Text("Confirm Password")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        // Gender
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Text(
                text = "♂",
                fontSize = 50.sp,
                color = if (viewModel.selectedGender == "Male")
                    Color.Blue
                else
                    Color.Gray,

                modifier = Modifier.clickable {
                    viewModel.updateGender("Male")
                }
            )

            Text(
                text = "♀",
                fontSize = 50.sp,
                color = if (viewModel.selectedGender == "Female")
                    Color.Magenta
                else
                    Color.Gray,

                modifier = Modifier.clickable {
                    viewModel.updateGender("Female")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        // Date of Birth
        OutlinedTextField(
            value = viewModel.dob,
            onValueChange = { },
            readOnly = true,

            label = {
                Text("Date of Birth")
            },

            placeholder = {
                Text("DD/MM/YYYY")
            },

            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            },

            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {  viewModel.CreateAccount() },
            modifier = Modifier.fillMaxWidth()
        )
        { Text("Create Account") }
    }
}

// havent make the model file for the data class
//package com.example.curtineat.model
//
//data class User(
//    val username: String,
//    val email: String,
//    val password: String,
//    val gender: String,
//    val dob: String
//)
