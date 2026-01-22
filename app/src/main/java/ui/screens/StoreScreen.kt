package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder

@Composable
fun StoreScreen(navController: NavController) {
    var cartCount by remember { mutableStateOf(0) }

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
                text = "🛒 KDFGC STORE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            if (cartCount > 0) {
                Badge(
                    containerColor = Color(0xFFFF5722),
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Text("$cartCount")
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text("RANGE PUNCH CARDS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            StoreItem(
                name = "Indoor Range: ADULT (18+) 10 Use Punch Card",
                regularPrice = "$120.00",
                memberPrice = "$100.00",
                description = "10 visits to the indoor range. Valid for 1 year from purchase.",
                uses = 10,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            StoreItem(
                name = "Indoor Range: JUNIOR (17 and under) 10 Use Punch Card",
                regularPrice = "$80.00",
                memberPrice = "$60.00",
                description = "10 visits to the indoor range for youth members. Valid for 1 year.",
                uses = 10,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            StoreItem(
                name = "Precision Rifle Shooting Punch Card (PRS)",
                regularPrice = "$150.00",
                memberPrice = "$120.00",
                description = "10 sessions at the precision rifle range. Includes target stands.",
                uses = 10,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("SHOTGUN SPORTS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            StoreItem(
                name = "Shotgun Sports: 10 Round Punch Card (Trap)",
                regularPrice = "$100.00",
                memberPrice = "$80.00",
                description = "Punch Card good for 10 Rounds of Trap Shooting. Does not include firearm rental or shells.",
                uses = 10,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            StoreItem(
                name = "Shot Gun Prepaid Targets (25 Clays)",
                regularPrice = "$50.00",
                memberPrice = "$40.00",
                description = "Prepaid targets for shotgun sports. 25 clay targets.",
                uses = 25,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("TARGET CARDS", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            StoreItem(
                name = "KDFGC 1500 Pre-Paid Target Card",
                regularPrice = "$25.00",
                memberPrice = "$20.00",
                description = "Prepaid target card worth $15.00 credit for indoor range targets.",
                uses = 1,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("GUEST PASSES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            StoreItem(
                name = "Prepaid Adult Guest Pass",
                regularPrice = "$30.00",
                memberPrice = "$25.00",
                description = "Single-use guest pass for adult visitors. Must be accompanied by member.",
                uses = 1,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            StoreItem(
                name = "Prepaid Junior Guest Pass",
                regularPrice = "$20.00",
                memberPrice = "$15.00",
                description = "Single-use guest pass for junior visitors (17 and under). Must be accompanied by member.",
                uses = 1,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text("ACCESSORIES", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            StoreItem(
                name = "Chamber Flags",
                regularPrice = "$5.00",
                memberPrice = "$5.00",
                description = "Safety chamber flags. Required for all firearms on the range.",
                uses = 1,
                onBuy = { name, price ->
                    cartCount++
                    val encoded = URLEncoder.encode(name, "UTF-8")
                    navController.navigate("checkout/$encoded/$price")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun StoreItem(
    name: String,
    regularPrice: String,
    memberPrice: String,
    description: String,
    uses: Int,
    onBuy: (String, String) -> Unit
) {
    val priceValue = memberPrice.replace("$", "")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (uses > 1) {
                    Badge(
                        containerColor = Color(0xFF007236)
                    ) {
                        Text("$uses uses", fontSize = 10.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                color = Color(0xFFAAAAAA),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Regular: $regularPrice",
                        color = Color(0xFF888888),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "Member: $memberPrice",
                        color = Color(0xFF90EE90),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = { onBuy(name, priceValue) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("BUY", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}