package com.example.endure.ui.fettle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.endure.ui.custompicker.CustomPicker
import com.example.endure.ui.custompicker.PickerItem

@Composable
fun FettleScreen() {
    val items = listOf(
        PickerItem(label = "Outlook"), // Text only
        PickerItem(label = "Activity"), // Text only
    )

    val dropdownOptions = listOf("For You", "Following")

    Column(modifier = Modifier.fillMaxSize()) {
        CustomPicker(
            items = items,
            dropdownOptions = dropdownOptions,
            showLabel = true,
            showIcon = true  // Icons are disabled
        )

        // Placeholder for Fettle details, replace with actual content
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Fettle Details Coming Soon")
        }
    }
}