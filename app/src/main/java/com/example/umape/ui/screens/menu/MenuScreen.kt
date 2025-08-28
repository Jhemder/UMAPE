package com.example.umape.ui.screens.menu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MenuScreen(
    userName: String,
    trainerLevel: Int = 1,
    totalCoins: Int = 0,
    onNavigateToGame: () -> Unit,
    onNavigateToCollection: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToMultiplayer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }

    // Animación de fondo
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F0F23),
                        Color(0xFF1A1A2E),
                        Color(0xFF16213E)
                    )
                )
            )
    ) {
        // Fondo animado
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(2.dp)
        ) {
            repeat(30) { index ->
                val angle = (animatedFloat + index * 12f) * Math.PI / 180
                val radius = size.minDimension / 2
                val centerX = size.width / 2
                val centerY = size.height / 2

                val x = centerX + radius * cos(angle).toFloat() * 0.3f
                val y = centerY + radius * sin(angle).toFloat() * 0.3f

                drawCircle(
                    color = Color(0xFFE94560).copy(alpha = 0.1f),
                    radius = 3f,
                    center = Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header con stats del jugador
            UserStatsCard(
                userName = userName,
                trainerLevel = trainerLevel,
                totalCoins = totalCoins
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Menú principal
            val menuItems = listOf(
                MenuItem("🎮 Continuar", "Última partida guardada", Icons.Default.PlayArrow, onNavigateToGame),
                MenuItem("✨ Nuevo Juego", "Aventura desde cero", Icons.Default.Add, onNavigateToGame),
                MenuItem("🏆 Colección", "Mis Uma Musume", Icons.Default.Star, onNavigateToCollection),
                MenuItem("🎰 Roll Shop", "Obtener nuevas Uma", Icons.Default.ShoppingCart, onNavigateToShop),
                MenuItem("🌐 Multijugador", "Carreras en red local", Icons.Default.People, onNavigateToMultiplayer),
                MenuItem("👤 Perfil", "Estadísticas y logros", Icons.Default.Person, onNavigateToProfile),
                MenuItem("⚙️ Configuración", "Ajustes del juego", Icons.Default.Settings, onNavigateToSettings)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(menuItems) { index, item ->
                    AnimatedMenuButton(
                        menuItem = item,
                        index = index,
                        isSelected = selectedIndex == index,
                        onSelect = { selectedIndex = index },
                        onDeselect = { selectedIndex = -1 }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserStatsCard(
    userName: String,
    trainerLevel: Int,
    totalCoins: Int
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A2E).copy(alpha = 0.9f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "¡Bienvenido de vuelta!",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = userName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE94560)
                    )
                    Text(
                        text = "Entrenador Nivel $trainerLevel",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💰",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = totalCoins.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                    }

                    Text(
                        text = "Tracen Academy",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedMenuButton(
    menuItem: MenuItem,
    index: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDeselect: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100L * index)
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn()
    ) {
        Card(
            onClick = {
                onSelect()
                menuItem.onClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable {
                    if (isSelected) onDeselect() else onSelect()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    Color(0xFFE94560).copy(alpha = 0.2f)
                else
                    Color(0xFF1A1A2E).copy(alpha = 0.8f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isSelected) 12.dp else 6.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = menuItem.title.split(" ").first(),
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = menuItem.title.substringAfter(" "),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFFE94560) else Color.White
                    )
                    Text(
                        text = menuItem.subtitle,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                Icon(
                    imageVector = menuItem.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFFE94560) else Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

private data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)