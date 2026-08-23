package com.culinarykinetic.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.culinarykinetic.app.state.AppViewModel
import com.culinarykinetic.app.ui.components.AppBottomNavBar
import com.culinarykinetic.app.ui.screens.address.AddressEditScreen
import com.culinarykinetic.app.ui.screens.address.AddressSelectScreen
import com.culinarykinetic.app.ui.screens.auth.AuthPhoneScreen
import com.culinarykinetic.app.ui.screens.auth.OtpScreen
import com.culinarykinetic.app.ui.screens.cart.CartScreen
import com.culinarykinetic.app.ui.screens.checkout.CheckoutScreen
import com.culinarykinetic.app.ui.screens.checkout.CouponsScreen
import com.culinarykinetic.app.ui.screens.favorites.FavoritesScreen
import com.culinarykinetic.app.ui.screens.food.FoodDetailsScreen
import com.culinarykinetic.app.ui.screens.help.HelpIssueScreen
import com.culinarykinetic.app.ui.screens.help.HelpScreen
import com.culinarykinetic.app.ui.screens.home.HomeScreen
import com.culinarykinetic.app.ui.screens.notifications.NotificationsScreen
import com.culinarykinetic.app.ui.screens.offers.OffersScreen
import com.culinarykinetic.app.ui.screens.onboarding.OnboardingScreen
import com.culinarykinetic.app.ui.screens.order.OrderDetailsScreen
import com.culinarykinetic.app.ui.screens.order.OrderSuccessScreen
import com.culinarykinetic.app.ui.screens.order.OrdersScreen
import com.culinarykinetic.app.ui.screens.payment.PaymentFailedScreen
import com.culinarykinetic.app.ui.screens.payment.PaymentProcessingScreen
import com.culinarykinetic.app.ui.screens.payment.PaymentSelectScreen
import com.culinarykinetic.app.ui.screens.profile.EditProfileScreen
import com.culinarykinetic.app.ui.screens.profile.PaymentMethodsScreen
import com.culinarykinetic.app.ui.screens.profile.ProfileScreen
import com.culinarykinetic.app.ui.screens.restaurant.RestaurantDetailsScreen
import com.culinarykinetic.app.ui.screens.restaurant.RestaurantListingScreen
import com.culinarykinetic.app.ui.screens.reviews.ReviewScreen
import com.culinarykinetic.app.ui.screens.search.SearchScreen
import com.culinarykinetic.app.ui.screens.settings.AboutScreen
import com.culinarykinetic.app.ui.screens.settings.SettingsScreen
import com.culinarykinetic.app.ui.screens.splash.SplashScreen

private val screensWithBottomBar = setOf(
    Routes.HOME, Routes.SEARCH, Routes.ORDERS, Routes.FAVORITES, Routes.PROFILE
)

@Composable
fun CulinaryKineticNavHost() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.weight(1f)) {
            NavHost(
                navController = navController,
                startDestination = Routes.SPLASH
            ) {
                composable(Routes.SPLASH) {
                    SplashScreen(onFinished = {
                        navController.navigate(Routes.ONBOARDING) { popUpTo(Routes.SPLASH) { inclusive = true } }
                    })
                }
                composable(Routes.ONBOARDING) {
                    OnboardingScreen(onFinished = {
                        navController.navigate(Routes.AUTH_PHONE) { popUpTo(Routes.ONBOARDING) { inclusive = true } }
                    })
                }
                composable(Routes.AUTH_PHONE) {
                    AuthPhoneScreen(
                        initialPhone = viewModel.phoneNumber,
                        onContinue = { phone ->
                            viewModel.updatePhoneNumber(phone)
                            navController.navigate(Routes.AUTH_OTP)
                        },
                        onSkipDemo = {
                            viewModel.completeAuth()
                            navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
                        }
                    )
                }
                composable(Routes.AUTH_OTP) {
                    OtpScreen(
                        phoneNumber = viewModel.phoneNumber,
                        onBack = { navController.popBackStack() },
                        onVerified = {
                            viewModel.completeAuth()
                            navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
                        }
                    )
                }
                composable(Routes.HOME) {
                    HomeScreen(
                        viewModel = viewModel,
                        onSearchClick = { navController.navigate(Routes.SEARCH) },
                        onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                        onCategoryClick = { category -> navController.navigate(Routes.restaurantListing(category)) },
                        onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetails(id)) }
                    )
                }
                composable(Routes.SEARCH) {
                    SearchScreen(
                        viewModel = viewModel,
                        onBack = null,
                        onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetails(id)) },
                        onFoodClick = { restaurantId, foodId -> navController.navigate(Routes.foodDetails(restaurantId, foodId)) }
                    )
                }
                composable(
                    Routes.RESTAURANT_LISTING,
                    arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
                ) { entry ->
                    val category = entry.arguments?.getString("categoryName") ?: ""
                    RestaurantListingScreen(
                        categoryName = category,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetails(id)) }
                    )
                }
                composable(
                    Routes.RESTAURANT_DETAILS,
                    arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
                ) { entry ->
                    val id = entry.arguments?.getString("restaurantId") ?: ""
                    RestaurantDetailsScreen(
                        restaurantId = id,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onFoodClick = { foodId -> navController.navigate(Routes.foodDetails(id, foodId)) },
                        onCartClick = { navController.navigate(Routes.CART) }
                    )
                }
                composable(
                    Routes.FOOD_DETAILS,
                    arguments = listOf(
                        navArgument("restaurantId") { type = NavType.StringType },
                        navArgument("foodId") { type = NavType.StringType }
                    )
                ) { entry ->
                    val restaurantId = entry.arguments?.getString("restaurantId") ?: ""
                    val foodId = entry.arguments?.getString("foodId") ?: ""
                    FoodDetailsScreen(
                        restaurantId = restaurantId,
                        foodId = foodId,
                        viewModel = viewModel,
                        onClose = { navController.popBackStack() },
                        onGoToCart = { navController.navigate(Routes.CART) }
                    )
                }
                composable(Routes.CART) {
                    CartScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onAddMoreItems = {
                            val rid = viewModel.cartRestaurant?.id
                            if (rid != null) navController.navigate(Routes.restaurantDetails(rid)) else navController.navigate(Routes.HOME)
                        },
                        onApplyCouponClick = { navController.navigate(Routes.COUPONS) },
                        onCheckout = { navController.navigate(Routes.ADDRESS_SELECT) }
                    )
                }
                composable(Routes.COUPONS) {
                    CouponsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
                composable(Routes.ADDRESS_SELECT) {
                    AddressSelectScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onAddNew = { navController.navigate(Routes.addressEdit()) },
                        onEdit = { addressId -> navController.navigate(Routes.addressEdit(addressId)) },
                        onConfirm = { navController.navigate(Routes.CHECKOUT) }
                    )
                }
                composable(
                    Routes.ADDRESS_EDIT,
                    arguments = listOf(navArgument("addressId") { type = NavType.StringType; nullable = true; defaultValue = null })
                ) { entry ->
                    AddressEditScreen(
                        viewModel = viewModel,
                        addressId = entry.arguments?.getString("addressId"),
                        onBack = { navController.popBackStack() },
                        onSaved = { navController.popBackStack() }
                    )
                }
                composable(Routes.CHECKOUT) {
                    CheckoutScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onEditAddress = { navController.navigate(Routes.ADDRESS_SELECT) },
                        onChoosePayment = { navController.navigate(Routes.PAYMENT_SELECT) },
                        onPlaceOrder = { navController.navigate(Routes.PAYMENT_PROCESSING) }
                    )
                }
                composable(Routes.PAYMENT_SELECT) {
                    PaymentSelectScreen(
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onProceed = { navController.popBackStack() }
                    )
                }
                composable(Routes.PAYMENT_PROCESSING) {
                    PaymentProcessingScreen(
                        viewModel = viewModel,
                        onSuccess = {
                            navController.navigate(Routes.ORDER_SUCCESS) {
                                popUpTo(Routes.CART) { inclusive = true }
                            }
                        },
                        onFailure = {
                            navController.navigate(Routes.PAYMENT_FAILED) {
                                popUpTo(Routes.PAYMENT_PROCESSING) { inclusive = true }
                            }
                        }
                    )
                }
                composable(Routes.PAYMENT_FAILED) {
                    PaymentFailedScreen(
                        onRetry = {
                            viewModel.resetPaymentState()
                            navController.navigate(Routes.PAYMENT_PROCESSING)
                        },
                        onChangeMethod = { navController.navigate(Routes.PAYMENT_SELECT) }
                    )
                }
                composable(Routes.ORDER_SUCCESS) {
                    OrderSuccessScreen(
                        viewModel = viewModel,
                        onTrackOrder = {
                            val id = viewModel.currentOrder?.id ?: return@OrderSuccessScreen
                            navController.navigate(Routes.orderTracking(id)) { popUpTo(Routes.HOME) }
                        },
                        onBackToHome = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } } },
                        onViewOrder = {
                            val id = viewModel.currentOrder?.id ?: return@OrderSuccessScreen
                            navController.navigate(Routes.orderDetails(id))
                        }
                    )
                }
                composable(
                    Routes.ORDER_TRACKING,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { entry ->
                    val orderId = entry.arguments?.getString("orderId") ?: ""
                    com.culinarykinetic.app.ui.screens.tracking.OrderTrackingScreen(
                        orderId = orderId,
                        viewModel = viewModel,
                        onClose = { navController.popBackStack() }
                    )
                }
                composable(
                    Routes.ORDER_DETAILS,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { entry ->
                    val orderId = entry.arguments?.getString("orderId") ?: ""
                    OrderDetailsScreen(
                        orderId = orderId,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onTrack = { navController.navigate(Routes.orderTracking(orderId)) },
                        onReorder = {
                            val order = viewModel.getOrderById(orderId)
                            if (order != null) viewModel.reorder(order)
                            navController.navigate(Routes.CART)
                        },
                        onHelp = { navController.navigate(Routes.HELP) },
                        onReview = { navController.navigate(Routes.review(orderId)) }
                    )
                }
                composable(Routes.ORDERS) {
                    OrdersScreen(
                        viewModel = viewModel,
                        onBack = null,
                        onOrderDetails = { id -> navController.navigate(Routes.orderDetails(id)) },
                        onTrackOrder = { id -> navController.navigate(Routes.orderTracking(id)) },
                        onReorder = { id ->
                            val order = viewModel.getOrderById(id)
                            if (order != null) viewModel.reorder(order)
                            navController.navigate(Routes.CART)
                        }
                    )
                }
                composable(Routes.FAVORITES) {
                    FavoritesScreen(
                        viewModel = viewModel,
                        onBack = null,
                        onRestaurantClick = { id -> navController.navigate(Routes.restaurantDetails(id)) },
                        onFoodClick = { restaurantId, foodId -> navController.navigate(Routes.foodDetails(restaurantId, foodId)) },
                        onBrowse = { navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) } }
                    )
                }
                composable(Routes.OFFERS) {
                    OffersScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
                composable(Routes.NOTIFICATIONS) {
                    NotificationsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        viewModel = viewModel,
                        onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                        onOrders = { navController.navigate(Routes.ORDERS) },
                        onFavorites = { navController.navigate(Routes.FAVORITES) },
                        onAddresses = { navController.navigate(Routes.ADDRESS_SELECT) },
                        onPayment = { navController.navigate(Routes.PAYMENT_METHODS) },
                        onOffers = { navController.navigate(Routes.OFFERS) },
                        onNotifications = { navController.navigate(Routes.NOTIFICATIONS) },
                        onHelp = { navController.navigate(Routes.HELP) },
                        onSettings = { navController.navigate(Routes.SETTINGS) },
                        onLogOut = {
                            viewModel.logOut()
                            navController.navigate(Routes.AUTH_PHONE) { popUpTo(0) { inclusive = true } }
                        }
                    )
                }
                composable(Routes.EDIT_PROFILE) {
                    EditProfileScreen(viewModel = viewModel, onBack = { navController.popBackStack() }, onSaved = { navController.popBackStack() })
                }
                composable(Routes.PAYMENT_METHODS) {
                    PaymentMethodsScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
                }
                composable(Routes.SETTINGS) {
                    SettingsScreen(viewModel = viewModel, onBack = { navController.popBackStack() }, onAbout = { navController.navigate(Routes.ABOUT) })
                }
                composable(Routes.ABOUT) {
                    AboutScreen(onBack = { navController.popBackStack() })
                }
                composable(Routes.HELP) {
                    HelpScreen(
                        onBack = { navController.popBackStack() },
                        onTopicClick = { topic -> navController.navigate(Routes.helpIssue(topic)) },
                        onChatSupport = { navController.navigate(Routes.helpIssue("Chat Support")) }
                    )
                }
                composable(
                    Routes.HELP_ISSUE,
                    arguments = listOf(navArgument("topic") { type = NavType.StringType })
                ) { entry ->
                    val topic = entry.arguments?.getString("topic") ?: "Help"
                    HelpIssueScreen(topic = topic, onBack = { navController.popBackStack() }, onSubmit = { navController.popBackStack() })
                }
                composable(
                    Routes.REVIEW,
                    arguments = listOf(navArgument("orderId") { type = NavType.StringType })
                ) { entry ->
                    val orderId = entry.arguments?.getString("orderId") ?: ""
                    ReviewScreen(
                        orderId = orderId,
                        viewModel = viewModel,
                        onBack = { navController.popBackStack() },
                        onSubmitted = { navController.popBackStack() }
                    )
                }
            }
        }
        if (currentRoute in screensWithBottomBar) {
            AppBottomNavBar(currentRoute = currentRoute) { route ->
                if (route != currentRoute) {
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}
