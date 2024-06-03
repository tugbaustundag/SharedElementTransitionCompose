package com.smality.sharedelementtransition

import android.os.Bundle
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.smality.sharedelementtransition.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedElementTransitionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SharedContentScreen()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedContentScreen() {
    var showDetails by remember {
        mutableStateOf(false)
    }

    SharedTransitionLayout {
        AnimatedContent(
            showDetails,
            label = "basic_transition"
        ) { targetState ->
            if (!targetState) {
                //mainContent
                MainContent(
                    onShowDetails = { showDetails = true },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                )
            } else {
                //detailsContent
                DetailsContent(
                    onBack = { showDetails = false },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MainContent(
    onShowDetails: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .border(
                1.dp, Color.Gray.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .background(
                LavenderLight,
                RoundedCornerShape(8.dp)
            )
            .clickable {
                onShowDetails()
            }
            .padding(8.dp)
    ) {
        with(sharedTransitionScope) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                painter =
                painterResource(id = R.drawable.tugba),
                contentDescription = null
            )

            Text(
                text = "Tuğba Üstündağ", fontSize = 21.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = animatedVisibilityScope
                ).padding(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(
        modifier = modifier
            .padding(top = 50.dp, start = 16.dp, end = 16.dp)
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .background(RoseLight, RoundedCornerShape(8.dp))
            .clickable {
                onBack()
            }
            .padding(8.dp)
    ) {
        with(sharedTransitionScope) {
            Image(
                painter = painterResource(id = R.drawable.tugba),
                contentDescription = "Tuğba Üstündağ",
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "image"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                "Tuğba Üstündağ", fontSize = 28.sp,
                modifier = Modifier.sharedBounds(
                    rememberSharedContentState(key = "title"),
                    animatedVisibilityScope = animatedVisibilityScope
                ).padding(10.dp)
            )
            Text(
                "Web/Mobil uygulama geliştirme ve yazılım eğitmenliği alanlarında 2008’den bu yana toplamda yaklaşık 12 yıldır çalışmaktayım. Mobil uygulama geliştirme konusunda yoğunlukla Android uygulamalar geliştirmekteyim. Aynı zamanda Web yazılım alt yapısı olarak, Opencart ve WordPress ortamlarında çeşitli geliştirme ve güncellemeler yapmaktayım.\n" +
                        "\n" +
                        "Danışmanlık ve eğitim alanlarında ise; kurumsal firma ve kuruluşlarda Android Yazılım ağırlıklı olmak üzere eğitimler vermekteyim."
            )
        }
    }
}