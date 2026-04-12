package com.kasagram.post

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasagram.core.data.RetrofitClient
import com.kasagram.core.viewmodel.BaseViewModel
import com.kasagram.post.data.LikeResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


class PostViewModel : BaseViewModel() {
    var posts by mutableStateOf<List<Post>>(emptyList())

    private var nextPageUrl: String? = null
    private var currentPage = 1

    fun fetchPosts(isFirstPage: Boolean = true) {
        if (isLoading) return
        if (!isFirstPage && nextPageUrl == null) return // Більше немає що вантажити
        launchWithLoading {
            val pageToLoad = if (isFirstPage) 1 else currentPage + 1
            val response = RetrofitClient.postApi.getPosts(pageToLoad)

            posts = if (isFirstPage) {
                response.results
            } else {
                posts + response.results // Додаємо нові пости до старих
            }

            nextPageUrl = response.next
            currentPage = pageToLoad
        }
    }

    fun updatePostLike(postId: Int, response: LikeResponse) {
        posts = posts.map { post ->
            if (post.id == postId) {
                post.copy(isLiked = response.liked, likesCount = response.likesCount)
            } else post
        }
    }
}

class PostDetailViewModel: BaseViewModel() {
    var post by mutableStateOf<Post?>(null)
    var comments by mutableStateOf<List<Comment>> (emptyList())

    fun loadPost(postId: Int) {
        if (isLoading) return
        launchWithLoading {
            val response = RetrofitClient.postApi.getPostDetail(postId)
            post = response
            comments = response.comments
        }
    }

    fun updatePostLike(response: LikeResponse) {
        post = post?.copy(
            likesCount = response.likesCount,
            isLiked = response.liked
        )
    }
}


class CreatePostViewModel(application: Application) : AndroidViewModel(application) {
    var isUploading by mutableStateOf(false)
        private set // Тільки ViewModel може його змінювати
    // Використовуємо getApplication() прямо в методах, щоб не було витоку пам'яті
    private val context get() = getApplication<Application>().applicationContext

    fun uploadPost(data: Map<String, String>, onComplete: () -> Unit) {
        val caption = data["content"] ?: ""
        // У твоєму UI ключ був "image", перевір чи збігається
        val imageUriString = data["image"] ?: return

        viewModelScope.launch {
            isUploading = true
            try {
                val uri = imageUriString.toUri()
                val contentBody = caption.toRequestBody("text/plain".toMediaTypeOrNull())

                // 1. Готуємо картинку
                val imagePart = prepareImagePart("media", uri)

                // 2. Викликаємо API через твій RetrofitClient
                // (Припускаю, що postApi вже має метод createPost)
                RetrofitClient.postApi.createPost(contentBody, imagePart)

                // 3. Якщо помилки не було — просто кажемо, що ми закінчили
                // NavController перекине нас на індекс, і той сам оновить список
                onComplete()

            } catch (e: Exception) {
                // Виведи помилку в лог, щоб розуміти, що не так
                e.printStackTrace()
            } finally {
                isUploading = false // Завершуємо (успішно чи з помилкою)
            }
        }
    }

    private fun prepareImagePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val inputStream = context.contentResolver.openInputStream(fileUri)
        // Створюємо файл у кеші
        val file = File(context.cacheDir, "temp_upload_${System.currentTimeMillis()}.jpg")

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }
}


class LikeViewModel: BaseViewModel() {
    fun likePost(postId: Int, onResult: (LikeResponse) -> Unit){
        launchWithLoading {
            val response = RetrofitClient.postApi.likePost(postId)
            onResult(response)
        }
    }
}