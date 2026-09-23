package com.example.curtineat.view

import com.example.curtineat.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.ui.theme.PrimaryCard
import com.example.curtineat.ui.theme.TextNormal
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Star
import com.example.curtineat.ui.theme.mySpacer

@Composable
fun RestaurantCard() {
    PrimaryCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        TextNormal(
            "Claypot House",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        mySpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextNormal("4.6", fontSize = 18.sp)

                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xffEA7422),
                    modifier = Modifier.size(20.dp)
                )
            }

            TextNormal("Non-Halal")
            TextNormal("0.6km")
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(5) {
                FoodItem(
                    onClick = {
                        println("Food clicked")
                    }
                )
            }
        }
    }
}

@Composable
fun FoodItem(
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.food1),
                contentDescription = "Bak Kut Teh",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                TextNormal(
                    "RM %.2f".format(23.4256),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                TextNormal(
                    "Bak Kut Teh",
                    fontSize = 14.sp
                )
            }
        }
    }
}
