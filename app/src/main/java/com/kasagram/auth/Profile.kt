package com.kasagram.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kasagram.R


@Composable
fun ProfileScreen(user: User) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 1. ШАПКА (Аватар + Стати)
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = user.avatar_url ?: R.drawable.def_av,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape), // Modifier.clip робить аватарку круглою!
                contentScale = ContentScale.Crop
            )
            // Тут можна додати Row зі статистикою
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. БІО
        Text(text = "${user.first_name} ${user.last_name}", fontWeight = FontWeight.Bold)
        Text(text = user.bio ?: "")

        Spacer(modifier = Modifier.height(24.dp))

        // 3. СІТКА (LazyVerticalGrid)
        // Це як Column, але для таблиць
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // Рівно 3 колонки
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(12) { // Тимчасово малюємо 12 квадратів
                Box(
                    modifier = Modifier
                        .aspectRatio(1f) // Робить елемент ідеальним квадратом 1:1
                        .background(Color.Gray)
                )
            }
        }
    }
}