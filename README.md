# Culinary Kinetic — Jetpack Compose Food Delivery Demo

A complete, fully-navigable **frontend-only** food delivery Android app built with Kotlin + Jetpack Compose + Material 3, based on the provided Stitch designs. Everything runs on local mock/demo data — no backend, Firebase, or payment gateway required.

## How to open & run

1. Open **Android Studio** (Koala/2024.1 or newer recommended).
2. `File > Open`, select the `CulinaryKinetic` folder.
3. Let Android Studio sync Gradle (it will auto-generate the Gradle wrapper jar on first sync if missing — click "OK" if prompted to use the bundled Gradle version, or install Gradle 8.7+ and run `gradle wrapper` once from a terminal in this folder).
4. Run the `app` configuration on an emulator or device (min SDK 24 / Android 7.0+).

> No API keys, backend URLs, or config are required — the app is 100% self-contained demo data.

## What's implemented

**Onboarding & Auth**: Splash → 3-page Onboarding → Phone entry (+ Email/Google demo buttons + "Skip sign-in") → OTP verification (any 6 digits work).

**Discovery**: Home (banner carousel, categories, filters, recommended restaurants, popular dishes) → Search (recent/popular/trending, live filtering) → Restaurant Listing (category results with sort/filter) → Restaurant Details (menu categories, offers) → Food Details (size + add-on customization with live price).

**Cart → Checkout → Payment**: Cart (quantity edit, coupon entry) → Coupons/Offers screen → Address selection/add/edit → Checkout (order summary, bill breakdown, payment method) → Payment method picker → simulated Payment Processing → Order Success — or Payment Failed with retry.

**Orders & Tracking**: Order Success → Live Order Tracking (simulated map, ETA, timeline, delivery partner card, manual "advance status" demo control — statuses also auto-progress every ~6s) → Order Details (invoice) → Orders list (Active/Past tabs) → Reorder (restores cart) → Review/Rating screen.

**Account**: Favorites (restaurants + dishes tabs), Offers, Notifications (read/unread), Profile (loyalty points, quick links), Edit Profile, Payment Methods, Settings (notification/appearance/account toggles), About, Help Center (issue categories + chat/topic submission).

**Empty & loading states**: empty cart, empty favorites/orders, no search results, payment processing/failure — all implemented as real screens/states, not stubs.

## Architecture

- **State**: a single `AppViewModel` (`state/AppViewModel.kt`) holds cart, favorites, addresses, coupons, orders (with a coroutine-driven status state machine), notifications, and search/settings state — shared across the whole nav graph via one `viewModel()` instance in `NavGraph.kt`.
- **Navigation**: `ui/navigation/Routes.kt` + `NavGraph.kt`, a single `NavHost` with a bottom bar shown only on Home/Search/Orders/Favorites/Profile.
- **Mock data**: `mock/MockData.kt` — realistic restaurants, menus, addresses, coupons, payment methods and notifications with ₹ pricing.
- **Design system**: `ui/theme/` — Color, Type, Shape/Dimens, Theme, matching the orange/cream Stitch palette.
- **Reusable components**: `ui/components/` — cards, chips, buttons, price rows, empty states, bottom nav.

## Notes for the next phase (not implemented, by design)

Retrofit/networking, Room/local DB, Firebase, real payment SDKs, and real Maps/GPS were intentionally left out per the brief — swap `AppViewModel`'s in-memory state and `MockData` for real repositories when you're ready to wire up a backend.
