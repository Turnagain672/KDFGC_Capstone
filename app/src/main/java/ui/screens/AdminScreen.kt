package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class StoreProduct(
    val id: Int,
    val name: String,
    val regularPrice: String,
    val memberPrice: String,
    val description: String,
    val category: String
)

@Composable
fun AdminScreen(navController: NavController) {
    var showAddDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<StoreProduct?>(null) }

    var products by remember { mutableStateOf(listOf(
        StoreProduct(1, "Indoor Range: ADULT 10 Use Punch Card", "120.00", "100.00", "10 visits to the indoor range.", "Range Cards"),
        StoreProduct(2, "Indoor Range: JUNIOR 10 Use Punch Card", "80.00", "60.00", "10 visits for youth members.", "Range Cards"),
        StoreProduct(3, "Shotgun Sports: 10 Round Punch Card", "100.00", "80.00", "10 Rounds of Trap Shooting.", "Shotgun"),
        StoreProduct(4, "Chamber Flags", "5.00", "5.00", "Safety chamber flags.", "Accessories"),
        StoreProduct(5, "Prepaid Adult Guest Pass", "30.00", "25.00", "Single-use guest pass.", "Guest Passes"),
        StoreProduct(6, "Prepaid Junior Guest Pass", "20.00", "15.00", "Junior guest pass.", "Guest Passes")
    )) }

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
                        colors = listOf(Color(0xFF8B0000), Color(0xFFB22222))
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
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ADMIN PANEL", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Store Management", color = Color(0xFFFFCCCC), fontSize = 12.sp)
            }
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("STORE PRODUCTS (${products.size})", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            products.forEach { product ->
                AdminProductCard(
                    product = product,
                    onEdit = {
                        selectedProduct = product
                        showEditDialog = true
                    },
                    onDelete = {
                        products = products.filter { it.id != product.id }
                    }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ADMIN ACTIONS", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { navController.navigate("events") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Event, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Manage Events")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { navController.navigate("profile") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Manage Members")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddDialog) {
        AddEditProductDialog(
            product = null,
            onDismiss = { showAddDialog = false },
            onSave = { newProduct ->
                products = products + newProduct.copy(id = (products.maxOfOrNull { it.id } ?: 0) + 1)
                showAddDialog = false
            }
        )
    }

    if (showEditDialog && selectedProduct != null) {
        AddEditProductDialog(
            product = selectedProduct,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProduct ->
                products = products.map { if (it.id == updatedProduct.id) updatedProduct else it }
                showEditDialog = false
            }
        )
    }
}

@Composable
fun AdminProductCard(
    product: StoreProduct,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Member: \$${product.memberPrice}", color = Color(0xFF90EE90), fontSize = 12.sp)
                Text(product.category, color = Color(0xFF888888), fontSize = 11.sp)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF2196F3), modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFFF5722), modifier = Modifier.size(20.dp))
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Product?", color = Color.White) },
            text = { Text("Are you sure you want to delete '${product.name}'?", color = Color(0xFFCCCCCC)) },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteConfirm = false }) {
                    Text("DELETE", color = Color(0xFFFF5722))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
    }
}

@Composable
fun AddEditProductDialog(
    product: StoreProduct?,
    onDismiss: () -> Unit,
    onSave: (StoreProduct) -> Unit
) {
    var name by remember { mutableStateOf(product?.name ?: "") }
    var regularPrice by remember { mutableStateOf(product?.regularPrice ?: "") }
    var memberPrice by remember { mutableStateOf(product?.memberPrice ?: "") }
    var description by remember { mutableStateOf(product?.description ?: "") }
    var category by remember { mutableStateOf(product?.category ?: "Range Cards") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Add Product" else "Edit Product", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Product Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = regularPrice,
                        onValueChange = { regularPrice = it },
                        label = { Text("Regular $") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF007236),
                            unfocusedBorderColor = Color(0xFF444444),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                    OutlinedTextField(
                        value = memberPrice,
                        onValueChange = { memberPrice = it },
                        label = { Text("Member $") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF007236),
                            unfocusedBorderColor = Color(0xFF444444),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && memberPrice.isNotBlank()) {
                        onSave(StoreProduct(
                            id = product?.id ?: 0,
                            name = name,
                            regularPrice = regularPrice,
                            memberPrice = memberPrice,
                            description = description,
                            category = category
                        ))
                    }
                }
            ) {
                Text("SAVE", color = Color(0xFF90EE90))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL", color = Color(0xFF888888))
            }
        },
        containerColor = Color(0xFF252525)
    )
}