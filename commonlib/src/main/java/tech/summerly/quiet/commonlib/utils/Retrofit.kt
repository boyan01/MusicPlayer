package tech.summerly.quiet.commonlib.utils

import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import org.jetbrains.anko.attempt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/**
 * author : yangbin10
 * date   : 2017/12/21
 */
suspend fun <T> Call<T>.await(): T = suspendCancellableCoroutine { continuation ->
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            if (!continuation.isCompleted) {
                continuation.resumeWithException(t)
            }
        }

        override fun onResponse(call: Call<T>, response: Response<T?>) {
            if (!response.isSuccessful) {
                continuation.resumeWithException(HttpException(response))
                return
            }
            val body = response.body()
            if (body == null) {
                continuation.resumeWithException(NullPointerException("null body"))
                return
            }
            continuation.resume(body)
        }
    })
    continuation.invokeOnCompletion {
        if (continuation.isCancelled) {
            attempt { cancel() }
        }
    }
}