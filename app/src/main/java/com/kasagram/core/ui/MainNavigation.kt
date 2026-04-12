package com.kasagram.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Home
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
import com.kasagram.auth.data.AuthSession

// Описуємо пункт меню як об'єкт
sealed class NavItem(val title: String, val icon: ImageVector) {
    abstract val route: String

    object Home : NavItem("", Icons.Default.Home) {
        override val route = "index"
    }
    object Add : NavItem("", Icons.Default.AddBox) {
        override val route = "add_post"
    }
    object Messages : NavItem("", Icons.AutoMirrored.Filled.Chat) {
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
    ) {
        // Завжди показуємо Home
        NavigationBarItem(
            selected = currentRoute == NavItem.Home.route,
            onClick = { onNavigate(NavItem.Home.route) },
            icon = { Icon(NavItem.Home.icon, contentDescription = null) },
            label = { Text(NavItem.Home.title) },
            alwaysShowLabel = false
        )

        // Завжди показуємо Add
        NavigationBarItem(
            selected = currentRoute == Add.route,
            onClick = { onNavigate(Add.route) },
            icon = { Icon(Add.icon, contentDescription = null) },
            label = { Text(Add.title) },
            alwaysShowLabel = false
        )

        if (isAuthenticated) {
            NavigationBarItem(
                selected = currentRoute == Messages.route,
                onClick = { onNavigate(Messages.route) },
                icon = { Icon(Messages.icon, contentDescription = null) },
                alwaysShowLabel = false
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
                },
                alwaysShowLabel = false
            )

            // Profile
            NavigationBarItem(
                selected = currentRoute == Profile.route,
                onClick = { onNavigate(Profile.route) },
                icon = { Icon(Profile.icon, contentDescription = null) },
                alwaysShowLabel = false
            )

        } else {
            // Login
            NavigationBarItem(
                selected = currentRoute == "login",
                onClick = { onNavigate("login") },
                icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                alwaysShowLabel = false
            )
        }
    }
}
}