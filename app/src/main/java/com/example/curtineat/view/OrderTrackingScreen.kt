package com.example.curtineat.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.curtineat.viewmodel.AppViewModel

private val orderStages = listOf("Received", "Preparing", "Ready for pick up")

@Composable
fun OrderTrackingScreen(
    appViewModel: AppViewModel,
    onHomeClick: () -> Unit,
    onWalletClick: () -> Unit,
    onLogInClick: () -> Unit = {}
) {
    // 0 = Received, 1 = Preparing, 2 = Ready for pick up
    var stage by rememberSaveable { mutableStateOf(0) }

    // items being made: group the cart by product and count quantities
    val lines by appViewModel.cart.collectAsState()

    AppScaffold(
        appViewModel = appViewModel,
        onHomeClick = onHomeClick,
        onWalletClick = onWalletClick,
        onLoginClick = onLogInClick,
        showSearch = false,
        showNotifications = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OrderProgressBar(
                currentStage = stage,
                onStageClick = { stage = it }
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = when (stage) {
                    0 -> "Order received, waiting for the vendor to start."
                    1 -> "Your food is being prepared."
                    else -> "Your food is ready. Please pick it up!"
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { if (stage < orderStages.lastIndex) stage++ },
                enabled = stage < orderStages.lastIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (stage < orderStages.lastIndex)
                        "Move to: ${orderStages[stage + 1]}"
                    else
                        "Order complete"
                )
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text("Items being made", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            if (lines.isEmpty()) {
                Text("No items in this order.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = lines,
                        key = { it.product.productId }
                    ) { cartItem ->

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = cartItem.product.productName
                                    )

                                    Text(
                                        text = "RM %.2f each".format(
                                            cartItem.product.productPrice
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Text(
                                    text = "x${cartItem.quantity}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 3-stage progress bar. Steps up to and including [currentStage] are filled in.
 * Tapping a step calls [onStageClick] (remove the clickable if only vendors may change it).
 */
@Composable
fun OrderProgressBar(
    currentStage: Int,
    onStageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val active = MaterialTheme.colorScheme.primary
    val inactive = MaterialTheme.colorScheme.surfaceVariant

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        orderStages.forEachIndexed { index, label ->
            val reached = index <= currentStage

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onStageClick(index) },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // line to the left of the circle (hidden on the first step)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                when {
                                    index == 0 -> androidx.compose.ui.graphics.Color.Transparent
                                    reached -> active
                                    else -> inactive
                                }
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (reached) active else inactive),
                        contentAlignment = Alignment.Center
                    ) {
                        if (index < currentStage) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = "${index + 1}",
                                color = if (reached)
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // line to the right of the circle (hidden on the last step)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .background(
                                when {
                                    index == orderStages.lastIndex ->
                                        androidx.compose.ui.graphics.Color.Transparent
                                    index < currentStage -> active
                                    else -> inactive
                                }
                            )
                    )
                }

                Spacer(Modifier.height(6.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}