package com.example.curtineat.ui.components

import android.R.attr.fontWeight
import android.R.attr.maxLines
import android.R.attr.text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.curtineat.ui.theme.CurtinText

// To use/Overwrite
/*
    TextNormal(
        text = "Claypot House",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 28.sp
    )

    PrimaryButton(
        text = "Add",
        onClick = { },
        containerColor = MaterialTheme.colorScheme.secondary
    )

    PrimaryCard(
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        CurtinText("Special Card")
    }

*/

// ---------------- OTHERS ----------------
@Composable
fun mySpacer(){
    Spacer(modifier = Modifier.height(12.dp))
}

// ---------------- TEXT ----------------

@Composable
fun TextNormal(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE,
    fontSize: TextUnit = 20.sp

) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = style,
        fontWeight = fontWeight,
        maxLines = maxLines,
        fontSize = fontSize,
    )
}


// ---------------- BUTTON ----------------

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(text)
    }
}





// ---------------- CARD ----------------

@Composable
fun PrimaryCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shape: Shape = RoundedCornerShape(20.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        )
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            content = content
        )
    }
}