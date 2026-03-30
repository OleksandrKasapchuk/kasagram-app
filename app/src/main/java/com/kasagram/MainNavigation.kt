package com.kasagram

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
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
sealed class NavItem(val title: String, val icon: ImageVector) {
    abstract val route: String

    object Home : NavItem("", Icons.Default.Home) {
        override val route = "index"
    }
    object Add : NavItem("", Icons.Default.AddBox) {
        override val route = "add_post"
    }
    object Messages : NavItem("", Icons.Default.Chat) {
        override val route = "chat_list"
    }
    object Notifications : NavItem("", Icons.Default.Notifications) {
        override val route = "notifications"
    }
    object Profile : NavItem("", Icons.Default.Person) {
        override val route: String
            get() = "profile/${AuthSession.userId}"
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
            selected = currentRoute == Add.route,
            onClick = { onNavigate(Add.route) },
            icon = { Icon(Add.icon, contentDescription = null) },
            label = { Text(Add.title) }
        )

        if (isAuthenticated) {
            NavigationBarItem(
                selected = currentRoute == Messages.route,
                onClick = { onNavigate(Messages.route) },
                icon = { Icon(Messages.icon, contentDescription = null) }
            )

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
                selected = currentRoute == Profile.route,
                onClick = { onNavigate(Profile.route) },
                icon = { Icon(Profile.icon, contentDescription = null) }
            )
            // Logout
            NavigationBarItem(
                selected = currentRoute == "logout",
                onClick = { onNavigate("logout") },
                icon = { Icon(Icons.Default.Logout, contentDescription = null) }
            )

        } else {
            // Login
            NavigationBarItem(
                selected = currentRoute == "login",
                onClick = { onNavigate("login") },
                icon = { Icon(Icons.Default.Login, contentDescription = null) }
            )
        }
    }
}
}