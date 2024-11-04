package com.example.endure.ui.marketplace

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.endure.ui.marketplace.components.CartButton
import com.example.endure.ui.marketplace.components.ProductCard
import com.example.endure.ui.marketplace.model.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel() // Assuming you're using Hilt for DI
) {
    var searchText by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shop") },
                actions = {
                    IconButton(onClick = { isSearchBarVisible = !isSearchBarVisible }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { navController.navigate("cart") }) {
                        CartButton(cartItemCount = cartViewModel.products.size)
                    }
                }
            )
        }
    ) { paddingValues ->  // Add paddingValues to handle content padding if needed
        Column(
            modifier = Modifier
                .padding(paddingValues)  // Use paddingValues to apply padding
        ) {
            if (isSearchBarVisible) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(cartViewModel.filteredProducts(searchText).size) { index ->
                    val product = cartViewModel.filteredProducts(searchText)[index]
                    ProductCard(
                        product = product,
                        onAddToCart = {
                            cartViewModel.addToCart(product)
                        },
                        onProductClick = {
                            navController.navigate("product_detail/${product.id}")
                        }
                    )
                }
            }
        }
    }
}