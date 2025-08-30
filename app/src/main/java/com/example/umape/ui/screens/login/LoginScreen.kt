package com.example.umape.ui.screens.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginComplete: (String, String, Boolean) -> Unit // (nombre, contraseña, esRegistro)
) {
    var playerName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("es") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    // Animaciones épicas
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedFloat by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val scaleAnimation by animateFloatAsState(
        targetValue = if (playerName.isNotBlank()) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    // Fondo con video simulado (partículas épicas)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fondo negro base para el "video"
    ) {
        // Simulación de video de fondo con partículas dinámicas
        EpicVideoBackground(animatedFloat)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo épico tipo DBZ
            Card(
                modifier = Modifier
                    .scale(scaleAnimation)
                    .animateContentSize(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏇⚡",
                        fontSize = 56.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Text(
                        text = "UMAPE",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700), // Dorado épico
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Ultimate Racing Championship",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle entre Login y Registro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButton(
                    text = "ENTRAR",
                    isSelected = !isRegisterMode,
                    onClick = { isRegisterMode = false }
                )
                TabButton(
                    text = "REGISTRARSE",
                    isSelected = isRegisterMode,
                    onClick = { isRegisterMode = true }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario animado
            AnimatedVisibility(
                visible = !isLoading,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Column {
                    // Campo de nombre
                    OutlinedTextField(
                        value = playerName,
                        onValueChange = { playerName = it },
                        label = {
                            Text(
                                if (isRegisterMode) "Nombre del Entrenador" else "Nombre de Usuario",
                                color = Color(0xFFFFD700)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f)
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", color = Color(0xFFFFD700)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color(0xFFFFD700)
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFD700),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f)
                        )
                    )

                    if (isRegisterMode) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Selector de idioma solo en registro
                        var expanded by remember { mutableStateOf(false) }
                        val languages = mapOf(
                            "es" to "🇪🇸 Español",
                            "en" to "🇺🇸 English",
                            "ja" to "🇯🇵 日本語"
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = languages[selectedLanguage] ?: "🇪🇸 Español",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Idioma", color = Color(0xFFFFD700)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFD700),
                                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White.copy(alpha = 0.9f)
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.background(Color.Black.copy(alpha = 0.9f))
                            ) {
                                languages.forEach { (code, name) ->
                                    DropdownMenuItem(
                                        text = { Text(name, color = Color.White) },
                                        onClick = {
                                            selectedLanguage = code
                                            expanded = false
                                        },
                                        colors = MenuDefaults.itemColors(
                                            textColor = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // Botón épico estilo DBZ
                    Button(
                        onClick = {
                            if (playerName.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                onLoginComplete(playerName, password, isRegisterMode)
                            }
                        },
                        enabled = playerName.isNotBlank() && password.isNotBlank() && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .animateContentSize(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFD700),
                            disabledContainerColor = Color(0xFFFFD700).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 12.dp,
                            pressedElevation = 6.dp
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(28.dp),
                                color = Color.Black,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text(
                                if (isRegisterMode) "⚡ CREAR LEYENDA ⚡" else "🚀 ENTRAR AL TORNEO 🚀",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                Color(0xFFFFD700).copy(alpha = 0.9f)
            else
                Color.Black.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Black else Color.White
        )
    }
}

@Composable
private fun EpicVideoBackground(rotation: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Simulación de video con múltiples capas de efectos
        drawVideoEffect(rotation)
    }
}

private fun DrawScope.drawVideoEffect(rotation: Float) {
    // Efecto de energía tipo DBZ
    val particleCount = 80
    repeat(particleCount) { index ->
        val angle = (rotation + index * 360f / particleCount) * Math.PI / 180
        val radius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        val speed = 0.5f + (index % 3) * 0.3f
        val x = centerX + radius * cos(angle).toFloat() * speed
        val y = centerY + radius * sin(angle).toFloat() * speed

        // Partículas doradas
        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = 0.4f),
            radius = 1.5f + (index % 3),
            center = Offset(x, y)
        )

        // Partículas azules
        val blueAngle = (-rotation + index * 180f / particleCount) * Math.PI / 180
        val blueX = centerX + radius * cos(blueAngle).toFloat() * 0.7f
        val blueY = centerY + radius * sin(blueAngle).toFloat() * 0.7f

        drawCircle(
            color = Color(0xFF0080FF).copy(alpha = 0.3f),
            radius = 2f,
            center = Offset(blueX, blueY)
        )
    }

    // Líneas de energía
    repeat(12) { index ->
        val lineAngle = (rotation * 2 + index * 30f) * Math.PI / 180
        val startRadius = 50f
        val endRadius = size.minDimension / 3

        val startX = size.width / 2 + startRadius * cos(lineAngle).toFloat()
        val startY = size.height / 2 + startRadius * sin(lineAngle).toFloat()
        val endX = size.width / 2 + endRadius * cos(lineAngle).toFloat()
        val endY = size.height / 2 + endRadius * sin(lineAngle).toFloat()

        drawLine(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFD700).copy(alpha = 0.6f),
                    Color.Transparent
                )
            ),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2f
        )
    }
}