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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.capstone2.data.ForumPost
import com.example.capstone2.data.ForumViewModel

@Composable
fun ForumScreen(viewModel: ForumViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    var newPostText by remember { mutableStateOf("") }

    val posts by viewModel.allPosts.collectAsState(initial = emptyList())

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
                    onClick = {
                        if (newPostText.isNotBlank()) {
                            viewModel.addPost("You", newPostText)
                            newPostText = ""
                        }
                    },
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
            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No posts yet.\nBe the first to share something!",
                        color = Color(0xFF888888),
                        fontSize = 14.sp
                    )
                }
            } else {
                posts.forEach { post ->
                    ForumPostCard(
                        post = post,
                        viewModel = viewModel,
                        onLike = { viewModel.likePost(post.id) },
                        onDelete = { viewModel.deletePost(post) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ForumPostCard(
    post: ForumPost,
    viewModel: ForumViewModel,
    onLike: () -> Unit,
    onDelete: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var showReplies by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    var showReplyInput by remember { mutableStateOf(false) }

    val replies by viewModel.getReplies(post.id).collectAsState(initial = emptyList())
    val replyCount by viewModel.getReplyCount(post.id).collectAsState(initial = 0)

    val timeAgo = remember(post.timestamp) {
        val diff = System.currentTimeMillis() - post.timestamp
        val minutes = diff / 60000
        val hours = minutes / 60
        val days = hours / 24
        when {
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF252525))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Author Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF007236)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.author.first().toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = post.author,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = timeAgo,
                            color = Color(0xFF888888),
                            fontSize = 11.sp
                        )
                    }
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Text(
                text = post.content,
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
                        if (isLiked) onLike()
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
                        text = "${post.likes + if (isLiked) 1 else 0}",
                        color = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF888888),
                        fontSize = 12.sp
                    )
                }

                TextButton(
                    onClick = { showReplyInput = !showReplyInput }
                ) {
                    Icon(
                        Icons.Default.Reply,
                        contentDescription = null,
                        tint = Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reply", color = Color(0xFF888888), fontSize = 12.sp)
                }

                TextButton(
                    onClick = { showReplies = !showReplies }
                ) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = if (showReplies) Color(0xFF90EE90) else Color(0xFF888888),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$replyCount",
                        color = if (showReplies) Color(0xFF90EE90) else Color(0xFF888888),
                        fontSize = 12.sp
                    )
                }
            }

            // Reply Input
            if (showReplyInput) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Write a reply...", color = Color(0xFF888888), fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF007236),
                            unfocusedBorderColor = Color(0xFF444444),
                            cursorColor = Color(0xFF90EE90)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (replyText.isNotBlank()) {
                                viewModel.addReply("You", replyText, post.id)
                                replyText = ""
                                showReplyInput = false
                                showReplies = true
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color(0xFF007236)
                        )
                    }
                }
            }

            // Replies Section
            if (showReplies && replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFF333333))
                Spacer(modifier = Modifier.height(8.dp))

                replies.forEach { reply ->
                    ReplyCard(reply = reply)
                }
            }
        }
    }
}

@Composable
fun ReplyCard(reply: ForumPost) {
    val timeAgo = remember(reply.timestamp) {
        val diff = System.currentTimeMillis() - reply.timestamp
        val minutes = diff / 60000
        val hours = minutes / 60
        val days = hours / 24
        when {
            days > 0 -> "$days day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0xFF005522)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = reply.author.first().toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = reply.author,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = timeAgo,
                    color = Color(0xFF888888),
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = reply.content,
                color = Color(0xFFCCCCCC),
                fontSize = 13.sp,
                lineHeight = 18.sp
            )
        }
    }
}