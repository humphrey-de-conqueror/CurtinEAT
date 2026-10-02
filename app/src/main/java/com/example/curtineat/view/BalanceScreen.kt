package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.ui.theme.BackButton
import com.example.curtineat.viewmodel.AppViewModel
import com.example.curtineat.viewmodel.TopUpResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BalanceScreen(
    appViewModel: AppViewModel,
    onBackButtonClick: () -> Unit
) {
    var balance by remember {
        mutableStateOf(0.0)
    }

    var accountName by remember {
        mutableStateOf("")
    }

    var isVendor by remember {
        mutableStateOf(false)
    }

    var showTopUpSheet by remember {
        mutableStateOf(false)
    }

    var topUpAmount by remember {
        mutableStateOf("")
    }

    var topUpMessage by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(appViewModel.account) {

        val vendorId = appViewModel.account.vendorId
        val customerId = appViewModel.account.customerId

        if (vendorId != null) {

            val vendor = appViewModel.getVendorById(vendorId)

            if (vendor != null) {
                balance = vendor.moneyBalance
                accountName = vendor.vendorName
                isVendor = true
            }

        } else if (customerId != null) {

            val customer = appViewModel.getCustomerById(customerId)

            if (customer != null) {
                balance = customer.moneyBalance
                accountName = customer.customerName
                isVendor = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(
                horizontal = 24.dp,
                vertical = 20.dp
            )
    ) {

        BackButton(
            onClick = onBackButtonClick
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = "Wallet",
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Column {

                Text(
                    text = "My Wallet",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (isVendor) {
                        "Vendor wallet"
                    } else {
                        "Customer wallet"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp)
            ) {

                Text(
                    text = "Available Balance",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Text(
                    text = "RM %.2f".format(balance),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = if (isVendor) {
                        Icons.Default.Store
                    } else {
                        Icons.Default.Person
                    },
                    contentDescription = "Account type"
                )

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column {

                    Text(
                        text = if (isVendor) {
                            "Vendor Account"
                        } else {
                            "Customer Account"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = accountName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = {
                topUpAmount = ""
                topUpMessage = null
                showTopUpSheet = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = "Top up"
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Top Up",
                fontSize = 18.sp
            )
        }
    }

    if (showTopUpSheet) {

        ModalBottomSheet(
            onDismissRequest = {
                showTopUpSheet = false
            },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = false
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
            ) {

                Text(
                    text = "Top Up Wallet",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Enter the amount you want to add.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                OutlinedTextField(
                    value = topUpAmount,
                    onValueChange = {
                        topUpAmount = it
                        topUpMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Amount")
                    },
                    prefix = {
                        Text("RM ")
                    },
                    singleLine = true
                )

                if (topUpMessage != null) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = topUpMessage!!,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
                        onClick = {
                            showTopUpSheet = false
                        }
                    ) {
                        Text("Cancel")
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        onClick = {

                            val amount = topUpAmount.toDoubleOrNull()

                            if (amount == null || amount <= 0.0) {

                                topUpMessage =
                                    "Please enter a valid amount"

                            } else {

                                appViewModel.topUp(
                                    amount = amount
                                ) { result ->

                                    when (result) {

                                        TopUpResult.SUCCESS -> {
                                            showTopUpSheet = false

                                            appViewModel.getCurrentBalance { newBalance ->
                                                balance = newBalance
                                            }
                                        }

                                        TopUpResult.INVALID_AMOUNT -> {
                                            topUpMessage =
                                                "Please enter a valid amount"
                                        }

                                        TopUpResult.ERROR -> {
                                            topUpMessage =
                                                "Top up failed. Please try again"
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Top Up"
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }
}