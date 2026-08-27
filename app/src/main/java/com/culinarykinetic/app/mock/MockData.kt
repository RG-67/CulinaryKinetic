package com.culinarykinetic.app.mock

import com.culinarykinetic.app.model.*

object MockData {

    val demoUser = User(
        id = "u1",
        name = "Alex Johnson",
        email = "alex.johnson@example.com",
        phone = "+1 (555) 123-4567",
        avatarUrl = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&q=80",
        loyaltyPoints = 2450,
        tier = "Gold Tier Member",
        pointsToNextTier = 550
    )

    val categories = listOf(
        Category("c1", "Pizza", "https://images.unsplash.com/photo-1594007654729-407eedc4be65?w=300&q=80"),
        Category("c2", "Burgers", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300&q=80"),
        Category("c3", "Biryani", "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=300&q=80"),
        Category("c4", "Chinese", "https://images.unsplash.com/photo-1585032226651-759b368d7246?w=300&q=80"),
        Category("c5", "Desserts", "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=300&q=80"),
        Category("c6", "South Indian", "https://images.unsplash.com/photo-1630383249896-483b1cb1f1e4?w=300&q=80")
    )

    private val biryaniMenu = listOf(
        MenuItem(
            id = "m1", name = "Chicken Dum Biryani",
            description = "Authentic Hyderabadi biryani cooked with succulent chicken and aromatic basmati rice. Slow-cooked to perfection in a sealed handi.",
            price = 320, imageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=600&q=80",
            category = "Recommended", isVeg = false, rating = 4.8, reviewCount = 124,
            sizes = listOf(SizeOption("Regular", 0, "Serves 1"), SizeOption("Large", 150, "Serves 2")),
            addOns = listOf(AddOn("a1", "Extra Chicken", 80), AddOn("a2", "Raita", 40), AddOn("a3", "Soft Drink", 60))
        ),
        MenuItem(
            id = "m2", name = "Mutton Special Biryani",
            description = "Succulent pieces of mutton cooked with whole spices and long-grain basmati rice.",
            price = 450, imageUrl = "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?w=600&q=80",
            category = "Recommended", isVeg = false, rating = 4.7, reviewCount = 98,
            sizes = listOf(SizeOption("Regular", 0, "Serves 1"), SizeOption("Large", 180, "Serves 2")),
            addOns = listOf(AddOn("a1", "Extra Mutton", 120), AddOn("a2", "Raita", 40), AddOn("a3", "Soft Drink", 60))
        ),
        MenuItem(
            id = "m3", name = "Paneer Tikka Biryani",
            description = "Smoked paneer tikka chunks layered with aromatic rice and mild gravy.",
            price = 280, imageUrl = "https://orders.popskitchen.in/storage/2024/09/image-285.png",
            category = "Recommended", isVeg = true, rating = 4.4, reviewCount = 61,
            addOns = listOf(AddOn("a2", "Raita", 40), AddOn("a3", "Soft Drink", 60))
        ),
        MenuItem(
            id = "m4", name = "Chicken 65 Starter",
            description = "Spicy, deep-fried chicken bites tossed in curry leaves and red chillies.",
            price = 220, imageUrl = "https://mykitchendiaries.com/wp-content/webp-express/webp-images/doc-root/wp-content/uploads/2026/01/Chicken-65-Feature-Image.jpg.webp",
            category = "Starters", isVeg = false, rating = 4.5, reviewCount = 44
        ),
        MenuItem(
            id = "m5", name = "Veg Seekh Kebab",
            description = "Char-grilled skewers of mixed vegetables and paneer with mint chutney.",
            price = 190, imageUrl = "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600&q=80",
            category = "Starters", isVeg = true, rating = 4.2, reviewCount = 28
        ),
        MenuItem(
            id = "m6", name = "Butter Chicken",
            description = "Creamy tomato-based curry with tender chicken, a North Indian classic.",
            price = 340, imageUrl = "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=600&q=80",
            category = "Main Course", isVeg = false, rating = 4.6, reviewCount = 152
        )
    )

    private val burgerMenu = listOf(
        MenuItem(
            id = "b1", name = "Classic Cheese Burger",
            description = "Juicy beef patty, melted cheddar, lettuce, tomato and house sauce.",
            price = 220, imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80",
            category = "Recommended", isVeg = false, rating = 4.3, reviewCount = 88,
            sizes = listOf(SizeOption("Regular", 0, "Single"), SizeOption("Large", 90, "Double patty")),
            addOns = listOf(AddOn("a4", "Extra Cheese", 40), AddOn("a5", "Bacon", 70), AddOn("a6", "Fries", 90))
        ),
        MenuItem(
            id = "b2", name = "Smoky BBQ Burger",
            description = "Grilled chicken patty glazed in smoky BBQ sauce with crispy onions.",
            price = 260, imageUrl = "https://images.unsplash.com/photo-1550547660-d9450f859349?w=600&q=80",
            category = "Recommended", isVeg = false, rating = 4.5, reviewCount = 73,
            addOns = listOf(AddOn("a4", "Extra Cheese", 40), AddOn("a6", "Fries", 90))
        ),
        MenuItem(
            id = "b3", name = "Veggie Delight Burger",
            description = "Crispy potato & corn patty with fresh veggies and mayo.",
            price = 180, imageUrl = "https://images.unsplash.com/photo-1520072959219-c595dc870360?w=600&q=80",
            category = "Recommended", isVeg = true, rating = 4.1, reviewCount = 40
        )
    )

    val restaurants = listOf(
        Restaurant(
            id = "r1", name = "The Biryani House",
            coverImageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=900&q=80",
            cuisines = listOf("Biryani", "North Indian", "Mughlai"),
            rating = 4.6, reviewCount = 512, deliveryTimeMinutes = "25-30 min",
            distanceKm = 2.1, priceForTwo = 400, priceLevel = "$$",
            isPromoted = true, offerText = "\u20B9125 OFF above \u20B9499", freeDelivery = true,
            menu = biryaniMenu, menuCategories = listOf("Recommended", "Main Course", "Starters")
        ),
        Restaurant(
            id = "r2", name = "Burger District",
            coverImageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=900&q=80",
            cuisines = listOf("American", "Fast Food", "Beverages"),
            rating = 4.2, reviewCount = 340, deliveryTimeMinutes = "20 min",
            distanceKm = 1.4, priceForTwo = 350, priceLevel = "$$",
            offerText = "Free Delivery on orders above \u20B9299", freeDelivery = true,
            menu = burgerMenu, menuCategories = listOf("Recommended")
        ),
        Restaurant(
            id = "r3", name = "Paradise Biryani",
            coverImageUrl = "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=900&q=80",
            cuisines = listOf("Hyderabadi", "Indian", "Mughlai"),
            rating = 4.8, reviewCount = 900, deliveryTimeMinutes = "25-35 min",
            distanceKm = 2.5, priceForTwo = 500, priceLevel = "$$",
            offerText = "50% OFF up to \u20B9100", menu = biryaniMenu,
            menuCategories = listOf("Recommended", "Main Course", "Starters")
        ),
        Restaurant(
            id = "r4", name = "Awadhi Nawabs",
            coverImageUrl = "https://images.unsplash.com/photo-1631515243349-e0cb75fb8d3a?w=900&q=80",
            cuisines = listOf("Lucknowi", "North Indian", "Kebab"),
            rating = 4.5, reviewCount = 410, deliveryTimeMinutes = "30-45 min",
            distanceKm = 4.1, priceForTwo = 700, priceLevel = "$$$",
            freeDelivery = true, menu = biryaniMenu,
            menuCategories = listOf("Recommended", "Main Course", "Starters")
        ),
        Restaurant(
            id = "r5", name = "Kolkata Special",
            coverImageUrl = "https://images.unsplash.com/photo-1588644525273-f37b60d78512?q=80&w=2069&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            cuisines = listOf("Bengali", "Mughlai", "Rolls"),
            rating = 4.2, reviewCount = 210, deliveryTimeMinutes = "15-25 min",
            distanceKm = 1.2, priceForTwo = 300, priceLevel = "$$",
            menu = biryaniMenu, menuCategories = listOf("Recommended", "Main Course", "Starters")
        )
    )

    val addresses = mutableListOf(
        Address(
            id = "addr1", label = "Home",
            line1 = "123 Gourmet Lane, Suite 4B",
            line2 = "Culinary District, Food City, FC 90210",
            instructions = "Leave at the door.",
            type = AddressType.HOME
        ),
        Address(
            id = "addr2", label = "Work",
            line1 = "456 Corporate Blvd, Floor 12",
            line2 = "Business Park, Food City, FC 90215",
            type = AddressType.WORK
        )
    )

    val coupons = listOf(
        Coupon("SAVE100", "Flat \u20B9100 OFF", "Get \u20B9100 off on your order", 100, 499),
        Coupon("WELCOME50", "\u20B950 OFF for new users", "Applicable on your first order", 50, 199),
        Coupon("BIRYANI125", "\u20B9125 OFF on Biryani orders", "Valid on The Biryani House & partners", 125, 499),
        Coupon("FREESHIP", "Free Delivery", "No delivery fee on this order", 40, 0)
    )

    val paymentMethods = listOf(
        PaymentMethod(PaymentMethodType.UPI, "UPI", "PhonePe, Google Pay"),
        PaymentMethod(PaymentMethodType.CARD, "Credit/Debit Card", "Visa **** 1234"),
        PaymentMethod(PaymentMethodType.NETBANKING, "Net Banking", "All major banks"),
        PaymentMethod(PaymentMethodType.WALLET, "Wallet", "Culinary Wallet \u2022 \u20B9250"),
        PaymentMethod(PaymentMethodType.COD, "Cash on Delivery", "Pay at your doorstep")
    )

    val initialNotifications = mutableListOf(
        AppNotification("n1", "Order Delivered", "Your order from The Biryani House has been delivered. Enjoy!", "2h ago", NotificationType.ORDER, true),
        AppNotification("n2", "Payment Successful", "\u20B9360 was paid via UPI for order #CK1042.", "2h ago", NotificationType.PAYMENT, true),
        AppNotification("n3", "50% OFF this weekend!", "Use code SAVE100 on orders above \u20B9499.", "1d ago", NotificationType.OFFER, false),
        AppNotification("n4", "Your rider is on the way", "Rahul is 15 minutes away from your location.", "3d ago", NotificationType.DELIVERY, true),
        AppNotification("n5", "New restaurants near you", "Check out 5 new restaurants added in HSR Layout.", "5d ago", NotificationType.PROMO, true)
    )

    fun searchAll(query: String): Pair<List<Restaurant>, List<MenuItem>> {
        if (query.isBlank()) return emptyList<Restaurant>() to emptyList()
        val q = query.trim().lowercase()
        val matchedRestaurants = restaurants.filter {
            it.name.lowercase().contains(q) || it.cuisines.any { c -> c.lowercase().contains(q) }
        }
        val matchedFoods = restaurants.flatMap { it.menu }.distinctBy { it.id }
            .filter { it.name.lowercase().contains(q) || it.category.lowercase().contains(q) }
        return matchedRestaurants to matchedFoods
    }

    val recentSearches = mutableListOf("Chicken Biryani", "Pizza", "Sushi")
    val popularCuisines = listOf("South Indian", "Chinese", "Italian", "Mexican")
}
