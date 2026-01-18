package com.example.capstone2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForumScreen() {
    val scrollState = rememberScrollState()
    var newPostText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF003D1F), Color(0xFF007236))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "💬 MEMBER FORUM",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Connect with fellow members",
                    fontSize = 12.sp,
                    color = Color(0xFFCCCCCC)
                )
            }
        }

        // New Post Input
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                OutlinedTextField(
                    value = newPostText,
                    onValueChange = { newPostText = it },
                    placeholder = { Text("Share something with the club...", color = Color(0xFF888888)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF007236),
                        unfocusedBorderColor = Color(0xFF444444),
                        cursorColor = Color(0xFF90EE90)
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { newPostText = "" },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("POST", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Posts List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ForumPost(
                author = "Mike Thompson",
                time = "2 hours ago",
                content = "Great turnout at the pistol competition yesterday! Congrats to all the winners. 🎯",
                likes = 12,
                comments = 4
            )

            ForumPost(
                author = "Sarah Chen",
                time = "5 hours ago",
                content = "Anyone interested in forming a weekend archery group? Looking to practice every Saturday morning.",
                likes = 8,
                comments = 7
            )

            ForumPost(
                author = "Dave Wilson",
                time = "1 day ago",
                content = "Reminder: The outdoor rifle range will be closed next Tuesday for maintenance. Plan accordingly!",
                likes = 24,
                comments = 2
            )

            ForumPost(
                author = "Lisa Park",
                time = "2 days ago",
                content = "Just completed my RPAL course here. Excellent instructors and very thorough training. Highly recommend!",
                likes = 31,
                comments = 9
            )

            ForumPost(
                author = "John Miller",
                time = "3 days ago",
                content = "Looking to buy a used .22 rifle for my son to learn on. Anyone have recommendations or something for sale?",
                likes = 5,
                comments = 12
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ForumPost(
    author: String,
    time: String,
    content: String,
    likes: Int,
    comments: Int
) {
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(likes) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF007236)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = author.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = author,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = time,
                        color = Color(0xFF888888),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = content,
                color = Color(0xFFDDDDDD),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(modifier = Modifier.height(8.dp))

            // Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = {
                        isLiked = !isLiked
                        likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                    }
                ) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$likeCount",
                        color = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF888888),
                        fontSize = 12.sp
                    )
                }

                TextButton(onClick = { }) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$comments", color = Color(0xFF888888), fontSize = 12.sp)
                }

                TextButton(onClick = { }) {
                    Icon(
                        Icons.Default.Share,
                        contentDescription = null,
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Share", color = Color(0xFF888888), fontSize = 12.sp)
                }
            }
        }
    }
}