package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showRegister by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val currentUser by userViewModel.currentUser.collectAsState()

    // If logged in, navigate to appropriate screen
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            if (user.isAdmin) {
                navController.navigate("adminpanel") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                navController.navigate("profile") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF007236))
                    )
                )
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Text(
                text = if (showRegister) "Create Account" else "Log In",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text("🦌", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "KDFGC Member Portal",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                if (showRegister) "Create your account" else "Sign in to your account",
                color = Color(0xFF888888),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (showRegister) {
                RegisterForm(
                    userViewModel = userViewModel,
                    onSuccess = { showRegister = false },
                    onError = { errorMessage = it }
                )
            } else {
                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedLabelColor = Color(0xFF90EE90),
                        unfocusedLabelColor = Color(0xFF888888),
                        cursorColor = Color(0xFF90EE90),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; errorMessage = null },
                    label = { Text("Password") },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = Color(0xFF888888)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedLabelColor = Color(0xFF90EE90),
                        unfocusedLabelColor = Color(0xFF888888),
                        cursorColor = Color(0xFF90EE90),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it, color = Color(0xFFFF5722), fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank() && password.isNotBlank()) {
                            isLoading = true
                            scope.launch {
                                val success = userViewModel.login(email, password)
                                isLoading = false
                                if (!success) {
                                    errorMessage = "Invalid email or password"
                                }
                            }
                        } else {
                            errorMessage = "Please fill in all fields"
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("LOG IN", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Demo accounts hint
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Demo Accounts:", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Admin: admin@kdfgc.org / admin123", color = Color(0xFFCCCCCC), fontSize = 11.sp)
                        Text("Member: member@kdfgc.org / member123", color = Color(0xFFCCCCCC), fontSize = 11.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = { showRegister = !showRegister; errorMessage = null }) {
                Text(
                    if (showRegister) "Already have an account? Log In" else "Don't have an account? Register",
                    color = Color(0xFF90EE90)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RegisterForm(
    userViewModel: UserViewModel,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var memberNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    OutlinedTextField(
        value = fullName,
        onValueChange = { fullName = it },
        label = { Text("Full Name") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007236),
            unfocusedBorderColor = Color(0xFF444444),
            focusedLabelColor = Color(0xFF90EE90),
            unfocusedLabelColor = Color(0xFF888888),
            cursorColor = Color(0xFF90EE90),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = email,
        onValueChange = { email = it },
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007236),
            unfocusedBorderColor = Color(0xFF444444),
            focusedLabelColor = Color(0xFF90EE90),
            unfocusedLabelColor = Color(0xFF888888),
            cursorColor = Color(0xFF90EE90),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = memberNumber,
        onValueChange = { memberNumber = it },
        label = { Text("Member Number") },
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007236),
            unfocusedBorderColor = Color(0xFF444444),
            focusedLabelColor = Color(0xFF90EE90),
            unfocusedLabelColor = Color(0xFF888888),
            cursorColor = Color(0xFF90EE90),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = password,
        onValueChange = { password = it },
        label = { Text("Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007236),
            unfocusedBorderColor = Color(0xFF444444),
            focusedLabelColor = Color(0xFF90EE90),
            unfocusedLabelColor = Color(0xFF888888),
            cursorColor = Color(0xFF90EE90),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = confirmPassword,
        onValueChange = { confirmPassword = it },
        label = { Text("Confirm Password") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF007236),
            unfocusedBorderColor = Color(0xFF444444),
            focusedLabelColor = Color(0xFF90EE90),
            unfocusedLabelColor = Color(0xFF888888),
            cursorColor = Color(0xFF90EE90),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = {
            when {
                fullName.isBlank() || email.isBlank() || password.isBlank() -> onError("Please fill in all fields")
                password != confirmPassword -> onError("Passwords don't match")
                password.length < 6 -> onError("Password must be at least 6 characters")
                else -> {
                    isLoading = true
                    scope.launch {
                        val success = userViewModel.register(email, password, fullName, memberNumber)
                        isLoading = false
                        if (success) {
                            onSuccess()
                        } else {
                            onError("Email already exists")
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(vertical = 14.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        } else {
            Text("CREATE ACCOUNT", fontWeight = FontWeight.Bold)
        }
    }
}