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
import androidx.compose.ui.draw.clip
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
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var showMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Animación de fondo
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        showMenu = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Gradiente verde inspirado en la imagen
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7ED321), // Verde claro
                        Color(0xFF5CB85C), // Verde medio
                        Color(0xFF4A9E4A)  // Verde más oscuro
                    )
                )
            )
    ) {
        // Fondo animado sutil
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(30) { index ->
                val angle = (animatedFloat + index * 12f) * Math.PI / 180
                val radius = size.minDimension / 3
                val centerX = size.width / 2
                val centerY = size.height / 2

                val x = centerX + radius * cos(angle).toFloat() * 0.2f
                val y = centerY + radius * sin(angle).toFloat() * 0.2f

                drawCircle(
                    color = Color.White.copy(alpha = 0.1f),
                    radius = 2f,
                    center = Offset(x, y)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Panel izquierdo con opciones (más estrecho para horizontal)
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .padding(12.dp)
            ) {
                // Header del usuario
                AnimatedVisibility(
                    visible = showMenu,
                    enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
                ) {
                    UserHeaderCard(userName, trainerLevel, totalCoins)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Título del menú
                AnimatedVisibility(
                    visible = showMenu,
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
                ) {
                    Text(
                        text = "🏇 Main Menu",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Lista de opciones
                val menuItems = listOf(
                    MenuItem("📖 Historia", "Modo Aventura", Icons.Default.PlayArrow, onNavigateToGame),
                    MenuItem("🎮 Torneo Local", "Multijugador Local", Icons.Default.SportsEsports, onNavigateToGame),
                    MenuItem("🌐 Online", "Batalla en Línea", Icons.Default.Public, onNavigateToMultiplayer),
                    MenuItem("✨ Colección", "Mis Uma Musume", Icons.Default.Star, onNavigateToCollection),
                    MenuItem("🎰 Tienda", "Gacha & Objetos", Icons.Default.ShoppingCart, onNavigateToShop),
                    MenuItem("👤 Perfil", "Mi Progreso", Icons.Default.Person, onNavigateToProfile),
                    MenuItem("⚙️ Opciones", "Configuración", Icons.Default.Settings, onNavigateToSettings),
                    MenuItem("🚪 Salir", "Cerrar Sesión", Icons.Default.ExitToApp) {
                        showLogoutDialog = true
                    }
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    itemsIndexed(menuItems) { index, item ->
                        MenuOptionCard(
                            menuItem = item,
                            index = index,
                            isSelected = selectedIndex == index,
                            isVisible = showMenu,
                            onSelect = {
                                selectedIndex = if (selectedIndex == index) -1 else index
                            },
                            onExecute = {
                                selectedIndex = index
                                item.onClick()
                            }
                        )
                    }
                }
            }

            // Panel derecho con el "escenario" 3D simulado (más ancho para horizontal)
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFF5F5DC), // Beige claro
                                Color(0xFFE6E6FA), // Lavanda claro
                                Color(0xFFD3D3D3)  // Gris claro
                            )
                        )
                    )
            ) {
                // Simulación del escenario 3D
                ScenarioView(animatedFloat)

                // Botones de control en la esquina
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ControlButton("B", "Atrás") { /* TODO */ }
                    ControlButton("A", "Confirmar") { /* TODO */ }
                }
            }
        }

        // Diálogo de logout
        if (showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = {
                    showLogoutDialog = false
                    onLogout()
                },
                onDismiss = {
                    showLogoutDialog = false
                }
            )
        }
    }
}

@Composable
private fun UserHeaderCard(
    userName: String,
    trainerLevel: Int,
    totalCoins: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Entrenador Lv.$trainerLevel",
                    fontSize = 11.sp,
                    color = Color(0xFF666666)
                )
            }

            // Estadísticas en row horizontal (más compacto para horizontal)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("🏆", "2.800")
                StatItem("💰", totalCoins.toString())
                StatItem("💎", "8.270")
                StatItem("🎫", "0")
            }
        }
    }
}

@Composable
private fun StatItem(emoji: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = emoji, fontSize = 12.sp)
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
private fun MenuOptionCard(
    menuItem: MenuItem,
    index: Int,
    isSelected: Boolean,
    isVisible: Boolean,
    onSelect: () -> Unit,
    onExecute: () -> Unit
) {
    var itemVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(100L * index)
            itemVisible = true
        }
    }

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    AnimatedVisibility(
        visible = itemVisible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ) + fadeIn()
    ) {
        Card(
            onClick = onExecute,
            modifier = Modifier
                .fillMaxWidth()
                .scale(scale)
                .clickable { onSelect() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected)
                    Color.White.copy(alpha = 1f)
                else
                    Color.White.copy(alpha = 0.85f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isSelected) 6.dp else 3.dp
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono (más pequeño para horizontal)
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            Color(0xFF4CAF50).copy(alpha = 0.1f),
                            RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = null,
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = menuItem.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = menuItem.subtitle,
                        fontSize = 10.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Indicador de selección
                if (isSelected) {
                    Text(
                        text = "▶",
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ScenarioView(rotation: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Fondo del escenario
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Suelo tipo ajedrez
            val tileSize = 40f
            val tilesX = (size.width / tileSize).toInt()
            val tilesY = (size.height / tileSize).toInt()

            for (x in 0 until tilesX) {
                for (y in 0 until tilesY) {
                    val isLight = (x + y) % 2 == 0
                    drawRect(
                        color = if (isLight) Color(0xFFF0F0F0) else Color(0xFFE0E0E0),
                        topLeft = Offset(x * tileSize, y * tileSize),
                        size = androidx.compose.ui.geometry.Size(tileSize, tileSize)
                    )
                }
            }
        }

        // Personajes simulados
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🐎",
                fontSize = 48.sp,
                modifier = Modifier
                    .offset(x = (10 * cos(rotation * Math.PI / 180)).dp)
            )
            Text(
                text = "🏇",
                fontSize = 32.sp,
                modifier = Modifier
                    .offset(x = (-15 * sin(rotation * Math.PI / 180)).dp)
            )

            Text(
                text = "UMAPE Racing Academy",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "¡Selecciona una opción para comenzar!",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
private fun ControlButton(letter: String, description: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2E7D32)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.size(36.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Cerrar Sesión",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }
        },
        text = {
            Text(
                text = "¿Estás seguro de que quieres cerrar sesión?\n\n🐎 Tu progreso se guardará automáticamente",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5722)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "🚪 Salir",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "❌ Cancelar",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    )
}

private data class MenuItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)