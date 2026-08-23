package com.culinarykinetic.app.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val AUTH_PHONE = "auth_phone"
    const val AUTH_OTP = "auth_otp"

    const val HOME = "home"
    const val SEARCH = "search"
    const val RESTAURANT_LISTING = "restaurant_listing/{categoryName}"
    fun restaurantListing(categoryName: String) = "restaurant_listing/$categoryName"

    const val RESTAURANT_DETAILS = "restaurant_details/{restaurantId}"
    fun restaurantDetails(id: String) = "restaurant_details/$id"

    const val FOOD_DETAILS = "food_details/{restaurantId}/{foodId}"
    fun foodDetails(restaurantId: String, foodId: String) = "food_details/$restaurantId/$foodId"

    const val CART = "cart"
    const val ADDRESS_SELECT = "address_select"
    const val ADDRESS_EDIT = "address_edit?addressId={addressId}"
    fun addressEdit(addressId: String? = null) = if (addressId != null) "address_edit?addressId=$addressId" else "address_edit"

    const val COUPONS = "coupons"
    const val CHECKOUT = "checkout"
    const val PAYMENT_SELECT = "payment_select"
    const val PAYMENT_PROCESSING = "payment_processing"
    const val PAYMENT_FAILED = "payment_failed"

    const val ORDER_SUCCESS = "order_success"
    const val ORDER_TRACKING = "order_tracking/{orderId}"
    fun orderTracking(orderId: String) = "order_tracking/$orderId"

    const val ORDER_DETAILS = "order_details/{orderId}"
    fun orderDetails(orderId: String) = "order_details/$orderId"

    const val ORDERS = "orders"
    const val FAVORITES = "favorites"
    const val OFFERS = "offers"
    const val NOTIFICATIONS = "notifications"

    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
    const val SETTINGS = "settings"
    const val PAYMENT_METHODS = "payment_methods"
    const val HELP = "help"
    const val HELP_ISSUE = "help_issue/{topic}"
    fun helpIssue(topic: String) = "help_issue/$topic"
    const val ABOUT = "about"

    const val REVIEW = "review/{orderId}"
    fun review(orderId: String) = "review/$orderId"
}
