package com.example.kdfgc_capstone.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("user_prefs")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MembershipScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userNameKey = stringPreferencesKey("user_name")
    var userName by remember { mutableStateOf("") }
    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        val savedName = context.dataStore.data.map { it[userNameKey] ?: "" }.first()
        userName = savedName
        textFieldValue = TextFieldValue(savedName)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Membership", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = textFieldValue,
            onValueChange = { textFieldValue = it },
            label = { Text("Your Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            scope.launch {
                context.dataStore.edit { prefs -> prefs[userNameKey] = textFieldValue.text }
                userName = textFieldValue.text
            }
        }) {
            Text("Save Name")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (userName.isNotEmpty()) {
            Text("Welcome, $userName!")
        }
    }
}