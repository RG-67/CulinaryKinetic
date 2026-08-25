package com.culinarykinetic.app.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.culinarykinetic.app.ui.components.PrimaryButton
import com.culinarykinetic.app.ui.theme.*

private data class OnboardPage(
    val imageUrl: String,
    val title: String,
    val subtitle: String
)

private val pages = listOf(
    OnboardPage(
        "https://images.unsplash.com/photo-1414235077428-338989a2e8c0?w=900&q=80",
        "Discover restaurants near you",
        "Find the best restaurants and cuisines in your city with ease."
    ),
    OnboardPage(
        "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?w=900&q=80",
        "Order your favorite food",
        "Satisfy your cravings with just a few taps."
    ),
    OnboardPage(
        "https://images.unsplash.com/photo-1526367790999-0150786686a2?w=900&q=80",
        "Track your delivery live",
        "Real-time updates on your food journey from the kitchen to your door."
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .background(BrandCream)
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            val data = pages[page]
            Column(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    coil.compose.AsyncImage(
                        model = data.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                    if (page != pages.size - 1) {
                        Text(
                            "Skip",
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(20.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(InkBlack.copy(alpha = 0.35f))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .then(Modifier)
                        )
                    }
                }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(BrandCream)
                        .padding(horizontal = 28.dp, vertical = 32.dp)
                ) {
                    Text(
                        data.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        data.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = SubtleGray
                    )
                    Spacer(Modifier.height(24.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        pages.indices.forEach { idx ->
                            Box(
                                Modifier
                                    .height(6.dp)
                                    .width(if (idx == pagerState.currentPage) 22.dp else 6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(if (idx == pagerState.currentPage) BrandOrange else DividerGray)
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    PrimaryButton(
                        modifier = Modifier.navigationBarsPadding(),
                        text = if (page == pages.size - 1) "Get Started" else "Continue",
                        trailingIcon = androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward,
                        onClick = {
                            if (page == pages.size - 1) {
                                onFinished()
                            } else {
                                scope.launch { pagerState.animateScrollToPage(page + 1) }
                            }
                        }
                    )
                }
            }
        }
    }
}
