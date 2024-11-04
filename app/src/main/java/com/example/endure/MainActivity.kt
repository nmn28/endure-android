package com.example.endure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.example.endure.ui.explore.ExploreScreen
import com.example.endure.ui.fettle.FettleScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.endure.customtopbar.CustomTopBar
import com.example.endure.ui.ai.aiScreen
import com.example.endure.ui.calendar.CalendarScreen
import com.example.endure.ui.finances.FinanceViewModel
import com.example.endure.ui.finances.wallet.CreditCard
import com.example.endure.ui.finances.wallet.SelectedCreditCardScreen
import com.example.endure.ui.finances.wallet.WalletScreen
import com.example.endure.ui.finances.wallet.sampleTransactions
import com.example.endure.ui.marketplace.CartScreen
import com.example.endure.ui.marketplace.MarketplaceScreen
import com.example.endure.ui.marketplace.ProductDetailScreen
import com.example.endure.ui.marketplace.model.CartViewModel
import com.example.endure.ui.social.SocialScreen
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val screens = listOf("fettle", "calendar", "social", "ai", "marketplace", "explore")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val financeViewModel: FinanceViewModel = viewModel()
    val cartViewModel: CartViewModel = hiltViewModel()

    val screenTitles = mapOf(
        "fettle" to "Fettle",
        "calendar" to "Calendar",
        "social" to "Social",
        "ai" to "AI",
        "marketplace" to "Marketplace",
        "explore" to "Explore"
    )

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent()
        }
    ) {
        Scaffold(
            topBar = {
                CustomTopBar(
                    title = screenTitles[navController.currentBackStackEntryAsState().value?.destination?.route] ?: "",
                    onSearchClick = { /* Handle search click */ },
                    onCartClick = { navController.navigate("cart") },
                    onMessagesClick = { /* Handle message click */ },
                    onNotificationsClick = { /* Handle notifications click */ },
                    onMenuClick = {
                        coroutineScope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            },
            bottomBar = { BottomNavigationBar(navController = navController) },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Open Bottom Sheet")
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            content = { paddingValues ->
                NavHost(
                    navController = navController,
                    startDestination = "fettle",
                    modifier = Modifier.padding(paddingValues)
                ) {
                    composable("fettle") { FettleScreen() }
                    composable("cart") { CartScreen(cartViewModel = cartViewModel, navController = navController) }
                    composable("product_detail/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        productId?.let {
                            ProductDetailScreen(productId = it, navController = navController, cartViewModel = cartViewModel)
                        }
                    }
                    composable("calendar") { CalendarScreen() }
                    composable("social") { SocialScreen() }
                    composable("ai") { aiScreen() }
                    composable("marketplace") { MarketplaceScreen(navController) }
                    composable("explore") { ExploreScreen() }
                    composable("wallet") {
                        WalletScreen(viewModel = financeViewModel, navController = navController)
                    }
                    composable(
                        "${Destinations.SelectedCreditCardScreen}/{creditCardJson}",
                        arguments = listOf(navArgument("creditCardJson") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val creditCardJson =
                            backStackEntry.arguments?.getString("creditCardJson") ?: ""
                        val creditCard = Gson().fromJson(creditCardJson, CreditCard::class.java)
                        SelectedCreditCardScreen(creditCard, sampleTransactions) {
                            // Define onDismiss logic here
                        }
                    }
                }
            }
        )
    }
}

object Destinations {
    const val WalletScreen = "wallet"
    const val SelectedCreditCardScreen = "selectedCreditCard"
}

@Composable
fun DrawerContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Drawer Item 1", modifier = Modifier.padding(16.dp))
        Text("Drawer Item 2", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem("fettle", Icons.Default.HealthAndSafety),
        NavigationItem("calendar", Icons.Default.CalendarMonth),
        NavigationItem("social", Icons.Default.Newspaper),
        NavigationItem("ai", Icons.Default.Computer),
        NavigationItem("marketplace", Icons.Default.Storefront),
        NavigationItem("explore", Icons.Default.Explore)
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                selected = currentRoute == item.route,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val title: String = "")

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}