package com.kasagram

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// Описуємо пункт меню як об'єкт
sealed class NavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : NavItem("", Icons.Default.Home, "index")
    object Add : NavItem("", Icons.Default.AddBox, "add_post")
    object Messages : NavItem("", Icons.Default.Chat, "chat_list")
    object Notifications : NavItem("", Icons.Default.Notifications, "notifications")
    object Profile : NavItem("", Icons.Default.Person, "profile/${AuthSession.currentUser.id}")
}

@Composable
fun KasagramBottomBar(
    isAuthenticated: Boolean,
    unreadCount: Int,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 8.dp
    ) {
        // Завжди показуємо Home
        NavigationBarItem(
            selected = currentRoute == NavItem.Home.route,
            onClick = { onNavigate(NavItem.Home.route) },
            icon = { Icon(NavItem.Home.icon, contentDescription = null) },
            label = { Text(NavItem.Home.title) }
        )

        // Завжди показуємо Add
        NavigationBarItem(
            selected = currentRoute == NavItem.Add.route,
            onClick = { onNavigate(NavItem.Add.route) },
            icon = { Icon(NavItem.Add.icon, contentDescription = null) },
            label = { Text(NavItem.Add.title) }
        )

        if (isAuthenticated) {
            // Messages (аналог твого {% else %} в шаблоні)
            NavigationBarItem(
                selected = currentRoute == NavItem.Messages.route,
                onClick = { onNavigate(NavItem.Messages.route) },
                icon = { Icon(NavItem.Messages.icon, contentDescription = null) }
            )

            // Notifications з "бейджиком" (unread_notification_count)
            NavigationBarItem(
                selected = currentRoute == NavItem.Notifications.route,
                onClick = { onNavigate(NavItem.Notifications.route) },
                icon = {
                    BadgedBox(badge = {
                        if (unreadCount > 0) {
                            Badge { Text(unreadCount.toString()) }
                        }
                    }) {
                        Icon(NavItem.Notifications.icon, contentDescription = null)
                    }
                }
            )

            // Profile
            NavigationBarItem(
                selected = currentRoute == NavItem.Profile.route,
                onClick = { onNavigate(NavItem.Profile.route) },
                icon = { Icon(NavItem.Profile.icon, contentDescription = null) }
            )
        } else {
            // Login (якщо не авторизований)
            NavigationBarItem(
                selected = currentRoute == "login",
                onClick = { onNavigate("login") },
                icon = { Icon(Icons.Default.Login, contentDescription = null) },
                label = { Text("Login") }
            )
        }
    }
}