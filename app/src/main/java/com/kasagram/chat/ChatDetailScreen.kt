package com.kasagram.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kasagram.AuthSession
import com.kasagram.R

@Composable
fun ChatDetailScreen(chat: Chat, onUserClick: (Int) -> Unit) {
    val participant = chat.participants.find { it.id != AuthSession.currentUser.id }

    Column {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { participant?.let { onUserClick(it.id) } }
        ) {
            AsyncImage(
                model = participant?.avatar_url ?: R.drawable.def_av,
                contentDescription = "User avatar",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp),
                contentScale = ContentScale.Crop,

                placeholder = painterResource(R.drawable.loading_img),
                // 3. Якщо сталася помилка завантаження:
                error = painterResource(R.drawable.error_img)
            )
            Text(
                text = participant?.username ?: "grr",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )

        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    }
}

