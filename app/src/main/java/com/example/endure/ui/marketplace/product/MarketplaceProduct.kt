package com.example.endure.ui.marketplace.product

import com.example.endure.R

data class MarketplaceProduct(
    val id: String,
    val name: String,
    val image: Int,
    val price: Int
)

val productList = listOf(
    MarketplaceProduct(id = "1", name = "Sleep Stack", image = R.drawable.sleepstack, price = 110),
    MarketplaceProduct(id = "2", name = "Fettle Ring", image = R.drawable.ring, price = 269),
    MarketplaceProduct(id = "3", name = "Test Kit", image = R.drawable.testkit, price = 220),
    // Add other products here...
)