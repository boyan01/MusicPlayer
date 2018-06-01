package tech.summerly.quiet.commonlib.base

import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import kotlinx.coroutines.experimental.Job
import tech.summerly.quiet.commonlib.mvp.BaseView

/**
 *  Created by summer on 17-12-17
 * as backup
 */
open class BaseActivity : AppCompatActivity(), BaseView {

    protected val job = Job()


    /**
     * request content view to dispatch windows insert
     */
    fun requestApplyInserts() {
        val content = findViewById<ViewGroup>(android.R.id.content) ?: return
        ViewCompat.requestApplyInsets(content)
    }

}