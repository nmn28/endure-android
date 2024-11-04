package com.example.endure.ui.marketplace.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.endure.ui.marketplace.product.MarketplaceProduct

@Composable
fun ProductCard(product: MarketplaceProduct, onAddToCart: () -> Unit, onProductClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onProductClick() }
    ) {
        Column {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = null,
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium, // Replacing subtitle1
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall // Replacing caption
            )
            Spacer(modifier = Modifier.height(8.dp))
            IconButton(onClick = { onAddToCart() }) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = "Add to Cart")
            }
        }
    }
}