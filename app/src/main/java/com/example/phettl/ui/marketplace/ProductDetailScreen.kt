package com.example.phettl.ui.marketplace

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip // Ensure this import is present
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.phettl.ui.marketplace.model.CartViewModel
import com.example.phettl.ui.marketplace.product.MarketplaceProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController, // Add NavController as a parameter
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val product = cartViewModel.getProductById(productId)

    product?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Product Details") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = product.image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(300.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Sold by: Phettl", color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${product.price}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Description of the product goes here.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Estimated delivery: 5-10 days", color = Color.Gray)
                }
            }
        )
    } ?: run {
        // Handle the case where the product is not found (show an error message or go back)
        Text("Product not found", modifier = Modifier.padding(16.dp))
    }
}
@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    // Creating a fake NavController for the preview
    val navController = rememberNavController()
    ProductDetailScreen(productId = "sample_product_id", navController = navController)
}