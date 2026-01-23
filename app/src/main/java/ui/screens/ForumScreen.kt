package com.example.capstone2.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.capstone2.data.ForumPost
import com.example.capstone2.data.ForumViewModel

data class ForumCategory(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumScreen(
    navController: NavController,
    viewModel: ForumViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    var selectedCategory by remember { mutableStateOf("all") }
    var showNewPostSheet by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    val posts by viewModel.allPosts.collectAsState(initial = emptyList())
    val currentUser by userViewModel.currentUser.collectAsState()
    val isAdmin = currentUser?.isAdmin == true

    val categories = listOf(
        ForumCategory("all", "All Posts", Icons.Default.Forum, Color(0xFF007236), "Everything"),
        ForumCategory("general", "General", Icons.Default.Chat, Color(0xFF2196F3), "Club news & chat"),
        ForumCategory("ranges", "Ranges", Icons.Default.GpsFixed, Color(0xFFFF5722), "Range talk"),
        ForumCategory("gear", "Gear & Reviews", Icons.Default.Build, Color(0xFF9C27B0), "Equipment"),
        ForumCategory("events", "Events", Icons.Default.Event, Color(0xFFFFEB3B), "Meetups"),
        ForumCategory("safety", "Safety", Icons.Default.Shield, Color(0xFFF44336), "Safety first"),
        ForumCategory("hunting", "Hunting", Icons.Default.Forest, Color(0xFF4CAF50), "Hunting talk"),
        ForumCategory("marketplace", "Buy/Sell", Icons.Default.Store, Color(0xFF00BCD4), "Marketplace")
    )

    // Filter out hidden posts for non-admins
    val filteredPosts = posts.filter { post ->
        (selectedCategory == "all" || post.category == selectedCategory) &&
                (searchQuery.isEmpty() || post.content.contains(searchQuery, ignoreCase = true) ||
                        post.author.contains(searchQuery, ignoreCase = true)) &&
                (isAdmin || !post.isHidden)
    }.sortedByDescending { it.timestamp }

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

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("MEMBER FORUM", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${posts.count { !it.isHidden }} discussions", fontSize = 11.sp, color = Color(0xFFCCCCCC))
            }

            IconButton(
                onClick = { showSearch = !showSearch },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(if (showSearch) Icons.Default.Close else Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
        }

        if (showSearch) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search posts...", color = Color(0xFF888888)) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444)
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategory == category.id,
                    onClick = { selectedCategory = category.id },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(category.icon, null, Modifier.size(16.dp), tint = if (selectedCategory == category.id) Color.White else category.color)
                            Spacer(Modifier.width(6.dp))
                            Text(category.name, fontSize = 12.sp)
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = category.color, selectedLabelColor = Color.White,
                        containerColor = Color(0xFF252525), labelColor = Color(0xFFCCCCCC)
                    )
                )
            }
        }

        Button(
            onClick = { showNewPostSheet = true },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Add, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("NEW POST", fontWeight = FontWeight.Bold)
        }

        if (filteredPosts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Forum, null, tint = Color(0xFF444444), modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No posts yet", color = Color(0xFF888888), fontSize = 16.sp)
                    Text("Be the first to start a discussion!", color = Color(0xFF666666), fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
                items(filteredPosts, key = { it.id }) { post ->
                    ForumPostCard(
                        post = post,
                        viewModel = viewModel,
                        categories = categories,
                        isAdmin = isAdmin
                    )
                }
            }
        }
    }

    if (showNewPostSheet) {
        NewPostBottomSheet(
            categories = categories.filter { it.id != "all" },
            onDismiss = { showNewPostSheet = false },
            onPost = { content, category, photoUri ->
                viewModel.addPost(currentUser?.fullName ?: "Anonymous", content, category, photoUri)
                showNewPostSheet = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPostBottomSheet(
    categories: List<ForumCategory>,
    onDismiss: () -> Unit,
    onPost: (String, String, String?) -> Unit
) {
    var postContent by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("general") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoUri = uri
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF252525),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Create New Post", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))

            Text("Category", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category.id,
                        onClick = { selectedCategory = category.id },
                        label = { Text(category.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = category.color, selectedLabelColor = Color.White,
                            containerColor = Color(0xFF333333), labelColor = Color(0xFFCCCCCC)
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Your Post", color = Color(0xFF90EE90), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = postContent,
                onValueChange = { postContent = it },
                placeholder = { Text("Share your thoughts...", color = Color(0xFF888888)) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444)
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF90EE90)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (photoUri != null) Icons.Default.CheckCircle else Icons.Default.AddAPhoto, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(if (photoUri != null) "Photo Added ✓" else "Add Photo")
            }

            if (photoUri != null) {
                Spacer(Modifier.height(12.dp))
                Box(Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = "Selected photo",
                        modifier = Modifier.fillMaxWidth().height(150.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { photoUri = null },
                        modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(28.dp).background(Color.Black.copy(alpha = 0.6f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { if (postContent.isNotBlank()) onPost(postContent, selectedCategory, photoUri?.toString()) },
                enabled = postContent.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007236), disabledContainerColor = Color(0xFF444444)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(Icons.Default.Send, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("POST", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
fun ForumPostCard(
    post: ForumPost,
    viewModel: ForumViewModel,
    categories: List<ForumCategory>,
    isAdmin: Boolean = false
) {
    var isLiked by remember { mutableStateOf(false) }
    var showReplies by remember { mutableStateOf(false) }
    var replyText by remember { mutableStateOf("") }
    var showReplyInput by remember { mutableStateOf(false) }
    var showReportDialog by remember { mutableStateOf(false) }
    var showAdminMenu by remember { mutableStateOf(false) }

    val replies by viewModel.getReplies(post.id).collectAsState(initial = emptyList())
    val replyCount by viewModel.getReplyCount(post.id).collectAsState(initial = 0)
    val category = categories.find { it.id == post.category }

    val timeAgo = remember(post.timestamp) {
        val diff = System.currentTimeMillis() - post.timestamp
        val minutes = diff / 60000
        val hours = minutes / 60
        val days = hours / 24
        when {
            days > 0 -> "${days}d ago"
            hours > 0 -> "${hours}h ago"
            minutes > 0 -> "${minutes}m ago"
            else -> "Just now"
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (post.isHidden) Color(0xFF3D1F1F) else Color(0xFF252525)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            // Hidden banner for admins
            if (post.isHidden && isAdmin) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFF5252).copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.VisibilityOff, null, tint = Color(0xFFFF5252), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("HIDDEN - Only visible to admins", color = Color(0xFFFF5252), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Flagged banner
            if (post.reportCount > 0 && isAdmin) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFF9800).copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Flag, null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("${post.reportCount} report(s)", color = Color(0xFFFF9800), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                if (category != null) {
                    Surface(shape = RoundedCornerShape(6.dp), color = category.color.copy(alpha = 0.2f)) {
                        Row(Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(category.icon, null, tint = category.color, modifier = Modifier.size(12.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(category.name, color = category.color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                Text(timeAgo, color = Color(0xFF888888), fontSize = 11.sp)
            }

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF007236)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(post.author.first().uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(post.author, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Member", color = Color(0xFF888888), fontSize = 11.sp)
                }
                Spacer(Modifier.weight(1f))

                // Admin menu or regular delete
                Box {
                    IconButton(onClick = { if (isAdmin) showAdminMenu = true else viewModel.deletePost(post) }) {
                        Icon(
                            if (isAdmin) Icons.Default.MoreVert else Icons.Default.Delete,
                            "Options",
                            tint = Color(0xFF666666),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showAdminMenu,
                        onDismissRequest = { showAdminMenu = false },
                        modifier = Modifier.background(Color(0xFF333333))
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (post.isHidden) "Unhide Post" else "Hide Post", color = Color.White) },
                            onClick = {
                                viewModel.toggleHidePost(post.id)
                                showAdminMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    if (post.isHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    null,
                                    tint = if (post.isHidden) Color(0xFF4CAF50) else Color(0xFFFF5252)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Clear Reports", color = Color.White) },
                            onClick = {
                                viewModel.clearReports(post.id)
                                showAdminMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50))
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Post", color = Color(0xFFFF5252)) },
                            onClick = {
                                viewModel.deletePost(post)
                                showAdminMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Delete, null, tint = Color(0xFFFF5252))
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Text(post.content, color = Color(0xFFEEEEEE), fontSize = 14.sp, lineHeight = 20.sp)

            // Show photo if exists
            if (!post.photoUri.isNullOrEmpty()) {
                Spacer(Modifier.height(12.dp))
                AsyncImage(
                    model = post.photoUri,
                    contentDescription = "Post photo",
                    modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFF333333))
            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                TextButton(onClick = {
                    isLiked = !isLiked
                    if (isLiked) viewModel.likePost(post.id)
                }) {
                    Icon(
                        if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null, tint = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF888888), modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("${post.likes + if (isLiked) 1 else 0}", color = if (isLiked) Color(0xFFFF6B6B) else Color(0xFF888888), fontSize = 12.sp)
                }

                TextButton(onClick = { showReplyInput = !showReplyInput }) {
                    Icon(Icons.Default.Reply, null, tint = Color(0xFF888888), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Reply", color = Color(0xFF888888), fontSize = 12.sp)
                }

                TextButton(onClick = { showReplies = !showReplies }) {
                    Icon(Icons.Default.ChatBubbleOutline, null, tint = if (showReplies) Color(0xFF90EE90) else Color(0xFF888888), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("$replyCount", color = if (showReplies) Color(0xFF90EE90) else Color(0xFF888888), fontSize = 12.sp)
                }

                // Report button (for non-admins)
                if (!isAdmin) {
                    TextButton(onClick = { showReportDialog = true }) {
                        Icon(Icons.Default.Flag, null, tint = Color(0xFF888888), modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Report", color = Color(0xFF888888), fontSize = 12.sp)
                    }
                }
            }

            if (showReplyInput) {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Write a reply...", color = Color(0xFF888888), fontSize = 12.sp) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF007236), unfocusedBorderColor = Color(0xFF444444)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (replyText.isNotBlank()) {
                            viewModel.addReply("You", replyText, post.id)
                            replyText = ""
                            showReplyInput = false
                            showReplies = true
                        }
                    }) {
                        Icon(Icons.Default.Send, "Send", tint = Color(0xFF007236))
                    }
                }
            }

            if (showReplies && replies.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                replies.forEach { reply -> ReplyCard(reply) }
            }
        }
    }

    // Report Dialog
    if (showReportDialog) {
        var reportReason by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showReportDialog = false },
            title = { Text("Report Post", color = Color.White) },
            text = {
                Column {
                    Text("Why are you reporting this post?", color = Color(0xFFCCCCCC), fontSize = 14.sp)
                    Spacer(Modifier.height(12.dp))

                    val reasons = listOf("Inappropriate content", "Spam", "Harassment", "Misinformation", "Other")
                    reasons.forEach { reason ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = reportReason == reason,
                                onClick = { reportReason = reason },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFFF6B6B),
                                    unselectedColor = Color(0xFF888888)
                                )
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(reason, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (reportReason.isNotBlank()) {
                            viewModel.reportPost(post.id)
                            showReportDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB22222)),
                    enabled = reportReason.isNotBlank()
                ) {
                    Text("REPORT")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReportDialog = false }) {
                    Text("CANCEL", color = Color(0xFF888888))
                }
            },
            containerColor = Color(0xFF252525)
        )
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
            days > 0 -> "${days}d"
            hours > 0 -> "${hours}h"
            minutes > 0 -> "${minutes}m"
            else -> "now"
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(8.dp)).padding(10.dp)
    ) {
        Box(
            modifier = Modifier.size(28.dp).clip(CircleShape).background(Color(0xFF005522)),
            contentAlignment = Alignment.Center
        ) {
            Text(reply.author.first().uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(reply.author, color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                Text(timeAgo, color = Color(0xFF666666), fontSize = 10.sp)
            }
            Spacer(Modifier.height(4.dp))
            Text(reply.content, color = Color(0xFFCCCCCC), fontSize = 12.sp, lineHeight = 16.sp)
        }
    }
}