package tech.summerly.quiet.commonlib.base

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import kotlinx.coroutines.experimental.Job
import tech.summerly.quiet.commonlib.mvp.BaseView

/**
 *  Created by summer on 17-12-17
 * as backup
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), BaseView {
    protected val job = Job()
}