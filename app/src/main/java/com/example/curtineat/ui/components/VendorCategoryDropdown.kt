package com.example.curtineat.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.curtineat.model.VendorCategory

@Composable
fun VendorCategoryDropdown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState =
        rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {

        val dropdownWidth = maxWidth

        OutlinedButton(
            onClick = {
                expanded = !expanded
            },
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 14.dp
            )
        ) {

            Text(
                text =
                    selectedCategory.ifBlank {
                        "Select restaurant category"
                    },
                modifier = Modifier.weight(1f),
                style =
                    MaterialTheme.typography.bodyLarge
            )

            Icon(
                imageVector =
                    if (expanded) {
                        Icons.Default.ArrowDropUp
                    } else {
                        Icons.Default.ArrowDropDown
                    },
                contentDescription =
                    "Select restaurant category"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .width(dropdownWidth)
                .heightIn(max = 280.dp),
            scrollState = scrollState,
            shape = RoundedCornerShape(16.dp),
            containerColor =
                MaterialTheme.colorScheme.surface
        ) {

            VendorCategory.entries.forEach { category ->

                val selected =
                    category.displayName ==
                            selectedCategory

                DropdownMenuItem(
                    text = {
                        Text(
                            text =
                                category.displayName,
                            fontWeight =
                                if (selected) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Normal
                                }
                        )
                    },
                    trailingIcon = {
                        if (selected) {
                            Icon(
                                imageVector =
                                    Icons.Default.Check,
                                contentDescription =
                                    "Selected",
                                tint =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onClick = {
                        onCategorySelected(
                            category.displayName
                        )

                        expanded = false
                    }
                )
            }
        }
    }
}