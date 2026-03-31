package com.kasagram.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.RetrofitClient
import kotlinx.coroutines.launch


class PostViewModel : ViewModel() {
    var posts by mutableStateOf<List<Post>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    private var nextPageUrl: String? = null
    private var currentPage = 1

    fun fetchPosts(isFirstPage: Boolean = true) {
        if (isLoading) return
        if (!isFirstPage && nextPageUrl == null) return // Більше немає що вантажити

        viewModelScope.launch {
            isLoading = true
            try {
                val pageToLoad = if (isFirstPage) 1 else currentPage + 1
                val response = RetrofitClient.postApi.getPosts(pageToLoad)

                if (isFirstPage) {
                    posts = response.results
                } else {
                    posts = posts + response.results // Додаємо нові пости до старих
                }

                nextPageUrl = response.next
                currentPage = pageToLoad
            } catch (e: Exception) {
                errorMessage = "Не вдалося завантажити пости: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}