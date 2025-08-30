package com.example.umape.ui.screens.login

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
    onLoginComplete: (String, String, Boolean) -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("es") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val isLandscape = screenWidth > screenHeight

    // Animación sutil para el fondo
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

    val scrollState = rememberScrollState()

    // Fondo verde similar al MenuScreen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7ED321), // Verde claro
                        Color(0xFF5CB85C), // Verde medio
                        Color(0xFF4A9E4A)  // Verde oscuro
                    )
                )
            )
    ) {
        // Fondo animado muy sutil
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

        if (isLandscape) {
            // Layout horizontal: dos columnas
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Columna izquierda: Logo y bienvenida
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WelcomeSection(isCompact = true)
                }

                // Columna derecha: Formulario
                Column(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.Center
                ) {
                    LoginForm(
                        playerName = playerName,
                        password = password,
                        isRegisterMode = isRegisterMode,
                        selectedLanguage = selectedLanguage,
                        passwordVisible = passwordVisible,
                        isLoading = isLoading,
                        isCompact = true,
                        onPlayerNameChange = { playerName = it },
                        onPasswordChange = { password = it },
                        onModeChange = { isRegisterMode = it },
                        onLanguageChange = { selectedLanguage = it },
                        onPasswordVisibilityChange = { passwordVisible = it },
                        onSubmit = {
                            if (playerName.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                onLoginComplete(playerName, password, isRegisterMode)
                            }
                        }
                    )
                }
            }
        } else {
            // Layout vertical: una sola columna
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                WelcomeSection()

                LoginForm(
                    playerName = playerName,
                    password = password,
                    isRegisterMode = isRegisterMode,
                    selectedLanguage = selectedLanguage,
                    passwordVisible = passwordVisible,
                    isLoading = isLoading,
                    onPlayerNameChange = { playerName = it },
                    onPasswordChange = { password = it },
                    onModeChange = { isRegisterMode = it },
                    onLanguageChange = { selectedLanguage = it },
                    onPasswordVisibilityChange = { passwordVisible = it },
                    onSubmit = {
                        if (playerName.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            onLoginComplete(playerName, password, isRegisterMode)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun WelcomeSection(isCompact: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (isCompact) 20.dp else 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo simple
            Text(
                text = "🏇",
                fontSize = if (isCompact) 48.sp else 64.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "UMAPE",
                fontSize = if (isCompact) 28.sp else 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Racing Championship",
                fontSize = if (isCompact) 14.sp else 16.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            if (!isCompact) {
                Text(
                    text = "¡Bienvenido entrenador! Prepárate para la carrera definitiva",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginForm(
    playerName: String,
    password: String,
    isRegisterMode: Boolean,
    selectedLanguage: String,
    passwordVisible: Boolean,
    isLoading: Boolean,
    isCompact: Boolean = false,
    onPlayerNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onModeChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    onSubmit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(if (isCompact) 20.dp else 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Toggle entre Login y Registro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ModeButton(
                    text = "Entrar",
                    isSelected = !isRegisterMode,
                    onClick = { onModeChange(false) },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompact
                )
                ModeButton(
                    text = "Registrarse",
                    isSelected = isRegisterMode,
                    onClick = { onModeChange(true) },
                    modifier = Modifier.weight(1f),
                    isCompact = isCompact
                )
            }

            // Campos del formulario
            OutlinedTextField(
                value = playerName,
                onValueChange = onPlayerNameChange,
                label = {
                    Text(
                        if (isRegisterMode) "Nombre del Entrenador" else "Usuario",
                        fontSize = if (isCompact) 14.sp else 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF999999),
                    focusedLabelColor = Color(0xFF4CAF50),
                    unfocusedLabelColor = Color(0xFF666666)
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = {
                    Text(
                        "Contraseña",
                        fontSize = if (isCompact) 14.sp else 16.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                        Icon(
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            tint = Color(0xFF666666)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color(0xFF999999),
                    focusedLabelColor = Color(0xFF4CAF50),
                    unfocusedLabelColor = Color(0xFF666666)
                )
            )

            if (isRegisterMode) {
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
                        label = {
                            Text(
                                "Idioma",
                                fontSize = if (isCompact) 14.sp else 16.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Language,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color(0xFF999999),
                            focusedLabelColor = Color(0xFF4CAF50),
                            unfocusedLabelColor = Color(0xFF666666)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        languages.forEach { (code, name) ->
                            DropdownMenuItem(
                                text = { Text(name, color = Color(0xFF333333)) },
                                onClick = {
                                    onLanguageChange(code)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Botón de acción
            Button(
                onClick = onSubmit,
                enabled = playerName.isNotBlank() && password.isNotBlank() && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 44.dp else 48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    disabledContainerColor = Color(0xFF4CAF50).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(if (isCompact) 20.dp else 24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        if (isRegisterMode) "Crear Cuenta" else "Iniciar Sesión",
                        fontSize = if (isCompact) 14.sp else 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(if (isCompact) 36.dp else 40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF4CAF50) else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(
            text,
            fontSize = if (isCompact) 12.sp else 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}