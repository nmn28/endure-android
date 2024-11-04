package com.example.phettl.ui.marketplace.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.phettl.ui.marketplace.product.MarketplaceProduct
import com.example.phettl.ui.marketplace.product.productList

class CartViewModel : ViewModel() {
    var products by mutableStateOf(listOf<MarketplaceProduct>())
        private set

    var total by mutableStateOf(0)
        private set

    var paymentSuccess by mutableStateOf(false)

    fun addToCart(product: MarketplaceProduct) {
        products = products + product
        total += product.price
    }

    fun removeFromCart(product: MarketplaceProduct) {
        products = products.filterNot { it.id == product.id }
        total -= product.price
    }

    fun pay() {
        paymentSuccess = true
        products = emptyList()
        total = 0
    }

    fun getProductById(id: String): MarketplaceProduct? {
        return productList.find { it.id == id }
    }

    fun filteredProducts(searchText: String): List<MarketplaceProduct> {
        return if (searchText.isEmpty()) {
            productList
        } else {
            productList.filter { it.name.contains(searchText, ignoreCase = true) }
        }
    }
}