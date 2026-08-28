package com.culinarykinetic.app.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val avatarUrl: String,
    val loyaltyPoints: Int,
    val tier: String,
    val pointsToNextTier: Int
)

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String
)

data class MenuItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val category: String,
    val isVeg: Boolean,
    val rating: Double = 4.5,
    val reviewCount: Int = 0,
    val sizes: List<SizeOption> = listOf(SizeOption("Regular", 0, "Serves 1")),
    val addOns: List<AddOn> = emptyList()
)

data class PopularCuisine(
    val id: String,
    val name: String,
    val imageUrl: String
)

data class SizeOption(val label: String, val extraPrice: Int, val note: String)
data class AddOn(val id: String, val name: String, val price: Int)

data class Restaurant(
    val id: String,
    val name: String,
    val coverImageUrl: String,
    val cuisines: List<String>,
    val rating: Double,
    val reviewCount: Int,
    val deliveryTimeMinutes: String,
    val distanceKm: Double,
    val priceForTwo: Int,
    val priceLevel: String,
    val isPromoted: Boolean = false,
    val offerText: String? = null,
    val freeDelivery: Boolean = false,
    val menu: List<MenuItem> = emptyList(),
    val menuCategories: List<String> = emptyList()
)

data class CartLineItem(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val menuItem: MenuItem,
    val size: SizeOption,
    val addOns: List<AddOn>,
    var quantity: Int
) {
    val unitPrice: Int get() = menuItem.price + size.extraPrice + addOns.sumOf { it.price }
    val lineTotal: Int get() = unitPrice * quantity
    val customizationSummary: String
        get() {
            val parts = mutableListOf<String>()
            if (size.label != "Regular") parts.add(size.label)
            parts.addAll(addOns.map { it.name })
            return parts.joinToString(", ")
        }
}

data class Address(
    val id: String,
    val label: String,
    val line1: String,
    val line2: String,
    val instructions: String = "",
    val type: AddressType
)

enum class AddressType { HOME, WORK, OTHER }

data class Coupon(
    val code: String,
    val title: String,
    val description: String,
    val discountFlat: Int,
    val minOrder: Int
)

enum class PaymentMethodType { UPI, CARD, NETBANKING, WALLET, COD }

data class PaymentMethod(
    val type: PaymentMethodType,
    val title: String,
    val subtitle: String
)

enum class OrderStatus(val label: String) {
    PLACED("Placed"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    PICKED_UP("Picked Up"),
    ON_THE_WAY("On the way"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled")
}

data class Order(
    val id: String,
    val restaurantId: String,
    val restaurantName: String,
    val restaurantImageUrl: String,
    val items: List<CartLineItem>,
    val itemTotal: Int,
    val deliveryFee: Int,
    val taxes: Int,
    val platformFee: Int,
    val discount: Int,
    val grandTotal: Int,
    val address: Address,
    val paymentMethod: PaymentMethod,
    val placedAt: String,
    var status: OrderStatus,
    val deliveryPartnerName: String = "Rahul Sharma",
    val deliveryPartnerRating: Double = 4.8,
    val deliveryPartnerVehicle: String = "Honda Activa \u2022 MH 12 AB 1234"
)

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    var isRead: Boolean = false
)

enum class NotificationType { ORDER, PAYMENT, OFFER, PROMO, DELIVERY }
