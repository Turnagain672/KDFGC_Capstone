package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLDecoder

class CardNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

class ExpiryDateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

fun getQuantityForItem(itemName: String): Int {
    return when {
        itemName.contains("10 Use", ignoreCase = true) -> 10
        itemName.contains("10 Round", ignoreCase = true) -> 10
        itemName.contains("25 Clays", ignoreCase = true) -> 25
        itemName.contains("Guest Pass", ignoreCase = true) -> 1
        itemName.contains("Chamber Flag", ignoreCase = true) -> 1
        itemName.contains("1500", ignoreCase = true) -> 1
        itemName.contains("Target Card", ignoreCase = true) -> 1
        else -> 1
    }
}

@Composable
fun CheckoutScreen(
    navController: NavController,
    itemName: String,
    price: String,
    userViewModel: UserViewModel
) {
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var billingAddress by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var isComplete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val currentUser by userViewModel.currentUser.collectAsState()

    val displayName = try {
        URLDecoder.decode(itemName, "UTF-8")
    } catch (e: Exception) {
        itemName.replace("-", " ").replaceFirstChar { it.uppercase() }
    }

    val quantity = getQuantityForItem(displayName)

    val isFormValid = cardNumber.length == 16 && expiryDate.length == 4 && cvv.length == 3 &&
            cardholderName.isNotBlank() && billingAddress.isNotBlank() &&
            city.isNotBlank() && postalCode.isNotBlank()

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
                text = "Secure Checkout",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = Color(0xFF90EE90),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        if (isComplete) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text("Payment Successful!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = displayName,
                    color = Color(0xFF90EE90),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("$$price CAD", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                if (currentUser != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2F))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("✅", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "$quantity uses added to your account!",
                                color = Color(0xFF90EE90),
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    "A confirmation email has been sent.\nPlease bring your receipt to the club office.",
                    color = Color(0xFFCCCCCC),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (currentUser != null) {
                    Button(
                        onClick = { navController.navigate("myaccount") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("VIEW MY PASSES", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = { navController.navigate("store") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentUser != null) Color(0xFF444444) else Color(0xFF007236)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("CONTINUE SHOPPING", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { navController.navigate("home") },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("GO TO HOME", color = Color(0xFF888888))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                if (currentUser == null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF3D2F1E))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⚠️", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Not logged in", color = Color(0xFFFFCC80), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("Log in to save passes to your account", color = Color(0xFFCCCCCC), fontSize = 11.sp)
                            }
                            TextButton(onClick = { navController.navigate("login") }) {
                                Text("LOG IN", color = Color(0xFFFFCC80), fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ORDER SUMMARY", color = Color(0xFF888888), fontSize = 11.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            displayName,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Includes $quantity uses",
                            color = Color(0xFF90EE90),
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Color(0xFF333333))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", color = Color(0xFFCCCCCC), fontSize = 14.sp)
                            Text("$$price CAD", color = Color(0xFF90EE90), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A2F))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF90EE90), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("256-bit SSL Encrypted • Secure Payment", color = Color(0xFF90EE90), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("CARD INFORMATION", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = cardholderName,
                    onValueChange = { cardholderName = it },
                    label = { Text("Cardholder Name") },
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
                    value = cardNumber,
                    onValueChange = { if (it.length <= 16 && it.all { c -> c.isDigit() }) cardNumber = it },
                    label = { Text("Card Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = CardNumberTransformation(),
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

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { if (it.length <= 4 && it.all { c -> c.isDigit() }) expiryDate = it },
                        label = { Text("MM/YY") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = ExpiryDateTransformation(),
                        modifier = Modifier.weight(1f),
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
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) cvv = it },
                        label = { Text("CVV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f),
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
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("BILLING ADDRESS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = billingAddress,
                    onValueChange = { billingAddress = it },
                    label = { Text("Street Address") },
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

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        modifier = Modifier.weight(1f),
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
                    OutlinedTextField(
                        value = postalCode,
                        onValueChange = { postalCode = it.uppercase() },
                        label = { Text("Postal Code") },
                        modifier = Modifier.weight(1f),
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
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isFormValid) {
                            isProcessing = true
                            scope.launch {
                                delay(2000)
                                currentUser?.let { user ->
                                    userViewModel.addPurchase(
                                        userId = user.id,
                                        itemName = displayName,
                                        quantity = quantity,
                                        price = price
                                    )
                                }
                                isProcessing = false
                                isComplete = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFormValid) Color(0xFF007236) else Color(0xFF444444)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    enabled = isFormValid && !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("PROCESSING...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("PAY $$price CAD", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("CANCEL", color = Color(0xFF888888))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}