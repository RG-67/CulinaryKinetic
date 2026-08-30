package com.culinarykinetic.app.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.culinarykinetic.app.mock.MockData
import com.culinarykinetic.app.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AppViewModel : ViewModel() {

    // ----- Auth (demo only) -----
    var isAuthenticated by mutableStateOf(false)
        private set
    var phoneNumber by mutableStateOf("")
        private set

    fun updatePhoneNumber(number: String) {
        phoneNumber = number
    }

    var otpVerified by mutableStateOf(false)
        private set

    val currentUser = MockData.demoUser

    fun completeAuth() {
        isAuthenticated = true
        otpVerified = true
    }

    fun logOut() {
        isAuthenticated = false
        otpVerified = false
        phoneNumber = ""
    }

    // ----- Location -----
    var currentLocationLabel by mutableStateOf("Gopinathpur, West Bengal")

    // ----- Data -----
    val restaurants = MockData.restaurants
    val categories = MockData.categories
    val coupons = MockData.coupons
    val paymentMethods = MockData.paymentMethods

    // ----- Favorites -----
    val favoriteRestaurantIds: SnapshotStateList<String> =
        mutableListOf<String>().toMutableStateList()
    val favoriteFoodIds: SnapshotStateList<String> = mutableListOf<String>().toMutableStateList()

    fun toggleFavoriteRestaurant(id: String) {
        if (favoriteRestaurantIds.contains(id)) favoriteRestaurantIds.remove(id) else favoriteRestaurantIds.add(
            id
        )
    }

    fun toggleFavoriteFood(id: String) {
        if (favoriteFoodIds.contains(id)) favoriteFoodIds.remove(id) else favoriteFoodIds.add(id)
    }

    // ----- Cart -----
    val cartItems: SnapshotStateList<CartLineItem> =
        mutableListOf<CartLineItem>().toMutableStateList()

    val cartRestaurant: Restaurant?
        get() = cartItems.firstOrNull()
            ?.let { item -> restaurants.find { it.id == item.restaurantId } }

    fun addToCart(
        restaurant: Restaurant,
        item: MenuItem,
        size: SizeOption,
        addOns: List<AddOn>,
        quantity: Int
    ) {
        // If cart has items from a different restaurant, clear it first (real app would confirm)
        if (cartItems.isNotEmpty() && cartItems.first().restaurantId != restaurant.id) {
            cartItems.clear()
        }
        val existing = cartItems.find {
            it.menuItem.id == item.id && it.size.label == size.label && it.addOns.map { a -> a.id } == addOns.map { a -> a.id }
        }
        if (existing != null) {
            existing.quantity += quantity
            val idx = cartItems.indexOf(existing)
            cartItems[idx] = existing.copy(quantity = existing.quantity)
        } else {
            cartItems.add(
                CartLineItem(
                    id = "cart_${System.currentTimeMillis()}_${(0..999).random()}",
                    restaurantId = restaurant.id,
                    restaurantName = restaurant.name,
                    menuItem = item,
                    size = size,
                    addOns = addOns,
                    quantity = quantity
                )
            )
        }
    }

    fun updateQuantity(lineItemId: String, delta: Int) {
        val idx = cartItems.indexOfFirst { it.id == lineItemId }
        if (idx == -1) return
        val item = cartItems[idx]
        val newQty = item.quantity + delta
        if (newQty <= 0) {
            cartItems.removeAt(idx)
        } else {
            cartItems[idx] = item.copy(quantity = newQty)
        }
    }

    fun removeFromCart(lineItemId: String) {
        cartItems.removeAll { it.id == lineItemId }
    }

    fun clearCart() {
        cartItems.clear()
        selectedCoupon = null
    }

    val cartItemTotal: Int get() = cartItems.sumOf { it.lineTotal }
    val cartDeliveryFee: Int get() = if (cartItems.isEmpty()) 0 else if (cartRestaurant?.freeDelivery == true) 0 else 40
    val cartTaxes: Int get() = (cartItemTotal * 0.05).toInt()
    val cartPlatformFee: Int get() = if (cartItems.isEmpty()) 0 else 8
    val cartDiscount: Int
        get() {
            val c = selectedCoupon ?: return 0
            return if (cartItemTotal >= c.minOrder) c.discountFlat else 0
        }
    val cartGrandTotal: Int
        get() = (cartItemTotal + cartDeliveryFee + cartTaxes + cartPlatformFee - cartDiscount).coerceAtLeast(
            0
        )

    // ----- Coupon -----
    var selectedCoupon by mutableStateOf<Coupon?>(null)

    fun applyCouponCode(code: String): Boolean {
        val coupon = coupons.find { it.code.equals(code.trim(), ignoreCase = true) } ?: return false
        if (cartItemTotal < coupon.minOrder) return false
        selectedCoupon = coupon
        return true
    }

    fun applyCoupon(coupon: Coupon) {
        selectedCoupon = coupon
    }

    fun removeCoupon() {
        selectedCoupon = null
    }

    // ----- Address -----
    val addresses: SnapshotStateList<Address> = MockData.addresses.toMutableStateList()
    var selectedAddress by mutableStateOf<Address?>(addresses.firstOrNull())

    fun selectAddress(address: Address) {
        selectedAddress = address
    }

    fun addAddress(address: Address) {
        addresses.add(address)
        selectedAddress = address
    }

    fun updateAddress(address: Address) {
        val idx = addresses.indexOfFirst { it.id == address.id }
        if (idx != -1) addresses[idx] = address
        if (selectedAddress?.id == address.id) selectedAddress = address
    }

    fun deleteAddress(addressId: String) {
        addresses.removeAll { it.id == addressId }
        if (selectedAddress?.id == addressId) selectedAddress = addresses.firstOrNull()
    }

    // ----- Payment -----
    var selectedPaymentMethod by mutableStateOf(paymentMethods.first())

    fun selectPaymentMethod(method: PaymentMethod) {
        selectedPaymentMethod = method
    }

    // ----- Payment processing state -----
    var paymentState by mutableStateOf(PaymentState.IDLE)
        private set

    enum class PaymentState { IDLE, PROCESSING, SUCCESS, FAILED }

    // ----- Orders -----
    val orderHistory: SnapshotStateList<Order> = mutableListOf<Order>().toMutableStateList()
    var currentOrder by mutableStateOf<Order?>(null)
        private set

    private fun genOrderId(): String = "CK${(1000..9999).random()}"

    fun startPayment(onResult: (Boolean) -> Unit) {
        paymentState = PaymentState.PROCESSING
        viewModelScope.launch {
            delay(1800)
            // Demo: succeed unless COD selected as a "simulate failure" toggle isn't needed; always succeed for smooth demo
            val success = true
            paymentState = if (success) PaymentState.SUCCESS else PaymentState.FAILED
            if (success) {
                placeOrder()
            }
            onResult(success)
        }
    }

    fun resetPaymentState() {
        paymentState = PaymentState.IDLE
    }

    private fun placeOrder() {
        val restaurant = cartRestaurant ?: return
        val address = selectedAddress ?: addresses.first()
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val order = Order(
            id = genOrderId(),
            restaurantId = restaurant.id,
            restaurantName = restaurant.name,
            restaurantImageUrl = restaurant.coverImageUrl,
            items = cartItems.toList(),
            itemTotal = cartItemTotal,
            deliveryFee = cartDeliveryFee,
            taxes = cartTaxes,
            platformFee = cartPlatformFee,
            discount = cartDiscount,
            grandTotal = cartGrandTotal,
            address = address,
            paymentMethod = selectedPaymentMethod,
            placedAt = sdf.format(Date()),
            status = OrderStatus.PLACED
        )
        orderHistory.add(0, order)
        currentOrder = order
        clearCart()
        simulateOrderProgress(order.id)
    }

    private fun simulateOrderProgress(orderId: String) {
        viewModelScope.launch {
            val sequence = listOf(
                OrderStatus.CONFIRMED,
                OrderStatus.PREPARING,
                OrderStatus.READY,
                OrderStatus.PICKED_UP,
                OrderStatus.ON_THE_WAY,
                OrderStatus.DELIVERED
            )
            for (status in sequence) {
                delay(6000)
                updateOrderStatus(orderId, status)
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: OrderStatus) {
        val idx = orderHistory.indexOfFirst { it.id == orderId }
        if (idx != -1) {
            orderHistory[idx].status = status
            // trigger recomposition by replacing reference
            val updated = orderHistory[idx].copy(status = status)
            orderHistory[idx] = updated
            if (currentOrder?.id == orderId) currentOrder = updated
        }
    }

    fun advanceCurrentOrderManually() {
        val order = currentOrder ?: return
        val all = OrderStatus.values().filter { it != OrderStatus.CANCELLED }
        val currentIdx = all.indexOf(order.status)
        if (currentIdx < all.size - 1) {
            updateOrderStatus(order.id, all[currentIdx + 1])
        }
    }

    fun reorder(order: Order) {
        cartItems.clear()
        order.items.forEach { cartItems.add(it.copy(id = "cart_${System.currentTimeMillis()}_${(0..999).random()}")) }
    }

    fun getOrderById(id: String): Order? =
        orderHistory.find { it.id == id } ?: currentOrder?.takeIf { it.id == id }

    // ----- Notifications -----
    val notifications: SnapshotStateList<AppNotification> =
        MockData.initialNotifications.toMutableStateList()

    val unreadNotificationCount: Int get() = notifications.count { !it.isRead }

    fun markNotificationRead(id: String) {
        val idx = notifications.indexOfFirst { it.id == id }
        if (idx != -1) notifications[idx] = notifications[idx].copy(isRead = true)
    }

    fun markAllNotificationsRead() {
        for (i in notifications.indices) notifications[i] = notifications[i].copy(isRead = true)
    }

    // ----- Search -----
    var searchQuery by mutableStateOf("")
    val recentSearches: SnapshotStateList<String> = MockData.recentSearches.toMutableStateList()

    fun commitSearch(query: String) {
        if (query.isBlank()) return
        recentSearches.remove(query)
        recentSearches.add(0, query)
        if (recentSearches.size > 6) recentSearches.removeAt(recentSearches.size - 1)
    }

    fun clearRecentSearches() {
        recentSearches.clear()
    }

    // ----- Settings (demo) -----
    var pushNotificationsEnabled by mutableStateOf(true)
    var promoNotificationsEnabled by mutableStateOf(true)
    var orderUpdatesEnabled by mutableStateOf(true)
    var darkModeEnabled by mutableStateOf(false)
    var selectedLanguage by mutableStateOf("English")
}
