package tech.summerly.quiet.commonlib.utils.support

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import kotlinx.coroutines.experimental.withTimeout
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

private const val TIMEOUT = 5000

/**
 * suspend coroutine and wait for live data value
 */
suspend fun <T> LiveData<T>.await(): T? = withTimeout(TIMEOUT) {
    return@withTimeout suspendCoroutine { cont: Continuation<T?> ->
        val o: Observer<T> = object : Observer<T> {
            override fun onChanged(t: T?) {
                cont.resume(t)
                removeObserver(this)
            }
        }
        observeForever(o)
    }
}