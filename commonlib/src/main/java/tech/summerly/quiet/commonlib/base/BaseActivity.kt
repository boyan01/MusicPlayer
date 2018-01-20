@file:Suppress("MemberVisibilityCanBePrivate")

package tech.summerly.quiet.commonlib.base

import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.toast
import tech.summerly.quiet.commonlib.mvp.BaseView

/**
 *  Created by summer on 17-12-17
 * as backup
 */
open class BaseActivity : AppCompatActivity(), BaseView {
    protected val job = Job()


    protected val defaultCoroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
        throwable.message?.let {
            toast(it)
        }
    }

    fun asyncUI(block: suspend (() -> Unit)) = launch(UI + defaultCoroutineExceptionHandler) {
        block()
    }
}