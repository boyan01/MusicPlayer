package tech.summerly.quiet.commonlib.utils

import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import org.jetbrains.anko.attempt
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import tech.summerly.quiet.commonlib.LibModule
import java.io.IOException

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


private val defaultCoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    throwable.printStackTrace()
    throwable.message?.let {
        LibModule.toast(it)
    }
}

fun asyncUI(exceptionHandler: CoroutineExceptionHandler = defaultCoroutineExceptionHandler,
            block: suspend (() -> Unit)) = launch(UI + exceptionHandler) {
    block()
}

fun CoroutineDispatcher.submit(parent: Job? = null,
                               block: suspend CoroutineScope.() -> Unit) = launch(context = this, parent = parent, block = block)

/**
 * retry
 */
suspend fun <T> retryIO(repeatTimes: Int = 10, block: suspend () -> T): T? {
    var curDelay = 1000L
    repeat(repeatTimes) {
        try {
            return block()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }
        delay(curDelay)
        curDelay = (curDelay * 2).coerceAtMost(60_000L)
    }
    return null
}

suspend fun <T> retryNull(repeatTimes: Int = 10, block: suspend () -> T): T? {
    var curDelay = 1000L
    repeat(repeatTimes) {
        val result = block()
        if (result != null) {
            return result
        }
        delay(curDelay)
        curDelay = (curDelay * 2).coerceAtMost(60_000L)
    }
    return null
}
