package com.example.curtineat.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

@Composable
fun ApiTestingScreen(appViewModel: AppViewModel) {
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
    ) {
//            Button(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {appViewModel.}
//            ) {
//                Text(text = "Clear")
//            }
        Spacer(modifier = Modifier.height(32.dp))
        if (appViewModel.vendor.isEmpty() || appViewModel.customer.isEmpty() || appViewModel.product.isEmpty()) {
            Text(text = "loading data")
        } else {
            Text(text = "testing vendor", color = MaterialTheme.colorScheme.primary)
            appViewModel.vendor.forEach { eachVendor ->
                Text(text = eachVendor.vendorName)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "testing customer", color = MaterialTheme.colorScheme.primary)
            appViewModel.customer.forEach { eachCustomer ->
                Text(text = eachCustomer.customerName)
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text(text = "testing product", color = MaterialTheme.colorScheme.primary)
            appViewModel.product.forEach { eachProduct ->
                Text(text = eachProduct.productName)
            }
        }
    }
}