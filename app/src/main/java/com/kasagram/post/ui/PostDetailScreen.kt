package com.kasagram.post.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.kasagram.post.Comment
import com.kasagram.post.Post
import com.kasagram.post.PostDetailViewModel
import com.kasagram.post.ui.components.CustomImage


@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel, // Передаємо створену у NavHost в'юмодель
    onLikeClick: () -> Unit,
    onDeletePost: (String) -> Unit,
    onSendComment: (String, Int?) -> Unit
) {
    val post = viewModel.post
    val isLoading = viewModel.isLoading

    // Центруємо індикатор завантаження
    if (isLoading && post == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (post != null) {
        // Передаємо коментарі саме з в'юмоделі
        PostContent(post, onLikeClick, onDeletePost, onSendComment, viewModel.comments)
    } else if (viewModel.errorMessage != null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = viewModel.errorMessage!!)
        }
    }
}

@Composable
fun PostContent(post: Post, onLikeClick: () -> Unit, onDeletePost: (String) -> Unit, onSendComment: (String, Int?) -> Unit, comments: List<Comment>) {
    var replyingTo by remember { mutableStateOf<Comment?>(null) }
    var commentText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = {
            MessageInputField(
                value = commentText, // Виправлено з chatMessage
                onValueChange = { commentText = it },
                onSendClick = {
                    onSendComment(commentText, replyingTo?.id)
                    commentText = ""
                    replyingTo = null
                },
                placeholder = "Add a comment...",
                replyingTo = replyingTo?.user?.username, // Передаємо тільки String
                onCancelReply = { replyingTo = null }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            item {
                CustomImage(
                    model =  post.mediaUrl,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                    loadingSize = 15.dp
                )
            }

//            item {
//                // Додаємо опис поста та кнопки лайків (PostHeaderSection)
//                PostHeaderSection(post, onLikeClick, onDeletePost)
//            }

            items(comments) { comment ->
                // Виводимо коментар та його відповіді
                CommentItem(
                    comment = comment,
                    onReplyClick = { replyingTo = comment }
                )
            }
        }
    }
}


@Composable
fun MessageInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    placeholder: String = "Type a message...",
    replyingTo: String? = null, // Якщо null — рядок реплаю не малюється
    onCancelReply: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        // Рядок реплаю (з'являється тільки якщо передано ім'я)
        if (replyingTo != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Replying to @$replyingTo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier
                        .size(16.dp)
                        .clickable { onCancelReply?.invoke() }
                )
            }
        }

        // Основне поле вводу
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(placeholder, color = Color.Gray)
                    }
                    innerTextField()
                }
            )

            IconButton(
                onClick = onSendClick,
                enabled = value.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (value.isNotBlank()) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment, onReplyClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        Row {
            CustomImage(
                model =  comment.user.avatarUrl,
                modifier = Modifier.size(36.dp).clip(CircleShape),
                loadingSize = 18.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(comment.user.username) }
                        append(" ${comment.content}")
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text("Just now", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "Reply",
                        modifier = Modifier.clickable { onReplyClick() },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        comment.replies.forEach { reply ->
            Row(modifier = Modifier.padding(start = 48.dp, top = 12.dp)) {
                CustomImage(
                    model = reply.user.avatarUrl,
                    contentDescription = "user avatar",
                    modifier = Modifier.size(24.dp).clip(CircleShape),
                    loadingSize = 12.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(reply.user.username) }
                        append(" ${reply.content}")
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}