package com.example.umape.ui.screens.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.umape.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var showContent by remember { mutableStateOf(false) }

    // Animación del progreso
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300, easing = EaseInOutCubic),
        label = "progress"
    )

    // Proceso de carga simple
    LaunchedEffect(Unit) {
        showContent = true
        delay(500)

        // Simulación de carga en incrementos
        for (i in 1..10) {
            progress = i / 10f
            delay(400) // Cada paso toma 400ms
        }

        delay(300)
        onLoadingComplete()
    }

    // Diseño súper simple
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // GIF real con borde circular verde
            AnimatedVisibility(
                visible = showContent,
                enter = scaleIn() + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF4CAF50), CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    // Para usar un GIF real, coloca el archivo en res/drawable
                    // y descomenta la siguiente línea:

                    AsyncImage(
                        model = R.drawable.loading, // Tu archivo GIF aquí
                        contentDescription = "Loading",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // Barra de progreso simple
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Texto de carga
                    Text(
                        text = "Cargando...",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Barra de progreso
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .width(200.dp)
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Porcentaje
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}