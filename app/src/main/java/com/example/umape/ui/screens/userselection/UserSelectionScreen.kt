package com.example.umape.ui.screens.userselection

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.umape.data.local.database.UserEntity
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSelectionScreen(
    users: List<UserEntity>,
    currentUser: UserEntity?,
    onUserSelected: (UserEntity) -> Unit,
    onCreateNewUser: () -> Unit,
    onBackToMenu: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        delay(200)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7ED321),
                        Color(0xFF5CB85C),
                        Color(0xFF4A9E4A)
                    )
                )
            )
    ) {
        // Fondo animado sutil
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            repeat(20) { index ->
                val angle = (animatedFloat + index * 18f) * Math.PI / 180
                val radius = size.minDimension / 4
                val centerX = size.width / 2
                val centerY = size.height / 2

                val x = centerX + radius * cos(angle).toFloat() * 0.3f
                val y = centerY + radius * sin(angle).toFloat() * 0.3f

                drawCircle(
                    color = Color.White.copy(alpha = 0.08f),
                    radius = 3f,
                    center = Offset(x, y)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Header con botón de regreso
            AnimatedVisibility(
                visible = showContent,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToMenu,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.9f),
                            contentColor = Color(0xFF4CAF50)
                        )
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Seleccionar Usuario",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Lista de usuarios
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón para crear nuevo usuario
                item {
                    AnimatedVisibility(
                        visible = showContent,
                        enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
                    ) {
                        CreateUserCard(onClick = onCreateNewUser)
                    }
                }

                // Lista de usuarios existentes
                itemsIndexed(users) { index, user ->
                    var itemVisible by remember { mutableStateOf(false) }

                    LaunchedEffect(showContent) {
                        if (showContent) {
                            kotlinx.coroutines.delay(100L * (index + 1))
                            itemVisible = true
                        }
                    }

                    AnimatedVisibility(
                        visible = itemVisible,
                        enter = slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        ) + fadeIn()
                    ) {
                        UserCard(
                            user = user,
                            isCurrentUser = currentUser?.id == user.id,
                            onClick = { onUserSelected(user) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateUserCard(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.1f),
                        RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "➕ Crear Nueva Cuenta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Registrar un nuevo entrenador",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    user: UserEntity,
    isCurrentUser: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = if (!isCurrentUser) onClick else { {} },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser)
                Color(0xFF4CAF50).copy(alpha = 0.1f)
            else
                Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentUser) 12.dp else 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        if (isCurrentUser) Color(0xFF4CAF50) else Color(0xFF4CAF50).copy(alpha = 0.1f),
                        RoundedCornerShape(32.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = if (isCurrentUser) Color.White else Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info del usuario
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCurrentUser) Color(0xFF2E7D32) else Color(0xFF2E7D32),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (isCurrentUser) {
                        Text(
                            text = "🏆 ACTIVO",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Text(
                    text = "Nivel ${user.trainerLevel} • ${user.totalCoins} monedas",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "🎮 ${user.gamesPlayed}",
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )
                    Text(
                        text = "🏆 ${user.racesWon}",
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )
                }
            }
        }
    }
}