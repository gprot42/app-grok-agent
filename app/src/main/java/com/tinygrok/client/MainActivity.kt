package com.tinygrok.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tinygrok.client.ui.theme.TinyGrokTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TinyGrokTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = true,
                                onClick = { navController.navigate("chat") { popUpTo("chat") } },
                                icon = { Icon(Icons.Filled.Chat, contentDescription = "Chat") },
                                label = { Text("Chat") }
                            )
                            NavigationBarItem(
                                selected = false,
                                onClick = { navController.navigate("settings") { popUpTo("settings") } },
                                icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "chat",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("chat") { ChatScreen(navController) }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}
