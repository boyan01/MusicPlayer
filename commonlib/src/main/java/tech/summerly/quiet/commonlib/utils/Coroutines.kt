package tech.summerly.quiet.commonlib.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.experimental.*
import org.jetbrains.anko.attempt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

/*
 * author : yangbin10
 * date   : 2017/12/27
 * to make some async callback to support co-routines
 */

/**
 * for glide , load and return the picture.
 *
 * @param width target picture' width , default is original size
 * @param height target picture' height , default is original size
 * @return  if failed return null
 */
suspend fun <T : Any> GlideRequest<T>.loadAndGet(any: Any?,
                                                 width: Int = Target.SIZE_ORIGINAL,
                                                 height: Int = Target.SIZE_ORIGINAL)
        : T? = suspendCancellableCoroutine { continuation ->
    if (any == null) {
        continuation.resume(null)
        return@suspendCancellableCoroutine
    }
    val simpleTarget = object : SimpleTarget<T>(width, height) {
        override fun onResourceReady(resource: T, transition: Transition<in T>?) {
            continuation.resume(resource)
        }

        override fun onLoadFailed(errorDrawable: Drawable?) {
            continuation.resume(null)
        }
    }
    continuation.invokeOnCompletion {
        if (continuation.isCancelled) {
            simpleTarget.request?.clear()
        }
    }
    load(any).into(simpleTarget)
}

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


fun CoroutineDispatcher.submit(parent: Job? = null,
                               block: suspend CoroutineScope.() -> Unit)
        = launch(context = this, parent = parent, block = block)

